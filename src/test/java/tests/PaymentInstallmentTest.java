package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PaymentInstallmentTest {

    @Test(groups = {"payment"})
    @Feature("Thanh toán VPBank")
    @Story("UC-PAY-001: Validate package payment suite")
    @Description("Smoke test placeholder cho pipeline Sprint 5")
    @Severity(SeverityLevel.CRITICAL)
    public void testPaymentSuiteIsConfigured() {
        String apiKey = System.getenv("VPBANK_API_KEY");
        Assert.assertNotNull(apiKey, "VPBANK_API_KEY chưa được cấu hình trong CI/CD");
        Assert.assertFalse(apiKey.isBlank(), "VPBANK_API_KEY không được để trống");
    }
}
