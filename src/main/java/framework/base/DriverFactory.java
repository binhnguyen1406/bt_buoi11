package framework.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public final class DriverFactory {
    private DriverFactory() {
    }

    public static WebDriver createDriver(String browser) {
        String selectedBrowser = browser == null || browser.isBlank() ? "chrome" : browser.toLowerCase();
        String gridUrl = System.getProperty("grid.url");
        if (gridUrl != null && !gridUrl.isBlank()) {
            return createRemoteDriver(selectedBrowser, gridUrl);
        }
        return createLocalDriver(selectedBrowser);
    }

    private static WebDriver createLocalDriver(String browser) {
        boolean headless = isCI() || Boolean.parseBoolean(System.getProperty("headless", "false"));
        return switch (browser) {
            case "firefox" -> createFirefoxDriver(headless);
            default -> createChromeDriver(headless);
        };
    }

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
            System.out.println("[Driver] Chạy Chrome HEADLESS (CI mode)");
        } else {
            options.addArguments("--start-maximized");
            System.out.println("[Driver] Chạy Chrome bình thường (Local mode)");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }

    private static WebDriver createRemoteDriver(String browser, String gridUrl) {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setBrowserName(browser);

    if ("chrome".equalsIgnoreCase(browser)) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        caps.merge(options);
    }

    if ("firefox".equalsIgnoreCase(browser)) {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("-headless");
        caps.merge(options);
    }

    try {
        URL gridEndpoint = new URL(gridUrl.endsWith("/wd/hub") ? gridUrl : gridUrl + "/wd/hub");
        RemoteWebDriver driver = new RemoteWebDriver(gridEndpoint, caps);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        System.out.printf("[Grid] Session tạo thành công trên: %s | Browser: %s | SessionID: %s%n",
                gridUrl, browser, driver.getSessionId());
        return driver;
    } catch (MalformedURLException e) {
        throw new RuntimeException("Grid URL không hợp lệ: " + gridUrl, e);
    }
}

    private static boolean isCI() {
        return System.getenv("CI") != null;
    }
}
