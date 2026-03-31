package framework.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonReader {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonReader() {
    }

    public static <T> T read(String resourceName, Class<T> clazz) {
        try (InputStream inputStream = JsonReader.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new IllegalStateException("Không tìm thấy resource: " + resourceName);
            }
            return OBJECT_MAPPER.readValue(inputStream, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Không đọc được JSON: " + resourceName, e);
        }
    }
}
