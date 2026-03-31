package framework.base;

import framework.config.ConfigReader;
import framework.pages.LoginPage;
import framework.utils.ScreenshotUtil;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.time.Duration;

public abstract class BaseTest {
    private static final ThreadLocal<org.openqa.selenium.WebDriver> DRIVER = new ThreadLocal<>();
    protected ConfigReader configReader;
    protected LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "env"})
    public void setUp(@Optional("chrome") String browser,
                      @Optional("dev") String env,
                      Method method) {
        configReader = new ConfigReader(env);
        DRIVER.set(DriverFactory.createDriver(browser));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(configReader.getTimeoutSeconds()));
        loginPage = new LoginPage(getDriver(), configReader.getTimeoutSeconds());
        Allure.parameter("browser", browser);
        Allure.parameter("env", env);
        Allure.parameter("testMethod", method.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (getDriver() != null && result.getStatus() == ITestResult.FAILURE) {
            byte[] screenshot = ScreenshotUtil.captureScreenshotBytes(getDriver());
            Allure.addAttachment("Screenshot", "image/png", new ByteArrayInputStream(screenshot), ".png");
            ScreenshotUtil.saveScreenshot(getDriver(), result.getMethod().getMethodName());
        }

        if (getDriver() != null) {
            getDriver().quit();
            DRIVER.remove();
        }
    }

    protected org.openqa.selenium.WebDriver getDriver() {
        return DRIVER.get();
    }

    protected void loginWithValidUser() {
        loginPage.open(configReader.getBaseUrl());
        loginPage.login(configReader.getUsername(), configReader.getPassword());
    }

    @Attachment(value = "Failure detail", type = "text/plain")
    protected String attachFailureText(String message) {
        return message;
    }
}
