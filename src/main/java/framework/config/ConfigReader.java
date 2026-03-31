package framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class ConfigReader {
    private final Properties properties = new Properties();
    private final String environment;

    public ConfigReader(String environment) {
        this.environment = (environment == null || environment.isBlank()) ? "dev" : environment;
        loadProperties();
    }

    private void loadProperties() {
        String fileName = String.format("config-%s.properties", environment);
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IllegalStateException("Không tìm thấy file config: " + fileName);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Không đọc được file config", e);
        }
    }

    public String getBaseUrl() {
        return firstNonBlank(System.getenv("BASE_URL"), System.getProperty("base.url"), properties.getProperty("base.url"));
    }

    public String getUsername() {
        return firstNonBlank(System.getenv("APP_USERNAME"), System.getProperty("app.username"), properties.getProperty("app.username"));
    }

    public String getPassword() {
        return firstNonBlank(System.getenv("APP_PASSWORD"), System.getProperty("app.password"), properties.getProperty("app.password"));
    }

    public int getTimeoutSeconds() {
        return Integer.parseInt(firstNonBlank(System.getProperty("timeout"), properties.getProperty("timeout.seconds"), "10"));
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}
