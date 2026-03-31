package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {
    private final By completeHeader = By.cssSelector(".complete-header");

    public CheckoutCompletePage(WebDriver driver, int timeoutSeconds) {
        super(driver, timeoutSeconds);
    }

    public String getCompleteHeader() {
        return text(completeHeader);
    }
}
