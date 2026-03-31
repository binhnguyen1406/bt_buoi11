package tests;

import framework.base.BaseTest;
import framework.pages.InventoryPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(groups = {"smoke", "regression"})
    @Feature("Đăng nhập hệ thống")
    @Story("UC-001: Đăng nhập bằng tài khoản hợp lệ")
    @Description("Kiểm thử đăng nhập với username/password hợp lệ")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginSuccess() {
        InventoryPage inventoryPage = new InventoryPage(getDriver(), configReader.getTimeoutSeconds());

        io.qameta.allure.Allure.step("Mở trang đăng nhập", () -> loginPage.open(configReader.getBaseUrl()));
        io.qameta.allure.Allure.step("Nhập thông tin đăng nhập hợp lệ", () ->
                loginPage.login(configReader.getUsername(), configReader.getPassword()));
        io.qameta.allure.Allure.step("Xác nhận chuyển đến Inventory", () ->
                Assert.assertEquals(inventoryPage.getTitle(), "Products"));
    }

    @Test(groups = {"regression"})
    @Feature("Đăng nhập hệ thống")
    @Story("UC-002: Từ chối đăng nhập với sai mật khẩu")
    @Description("Kiểm thử hiển thị lỗi khi người dùng nhập sai password")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginWithInvalidPassword() {
        io.qameta.allure.Allure.step("Mở trang đăng nhập", () -> loginPage.open(configReader.getBaseUrl()));
        io.qameta.allure.Allure.step("Nhập password sai", () ->
                loginPage.login(configReader.getUsername(), "invalid_password"));
        io.qameta.allure.Allure.step("Xác nhận có thông báo lỗi", () ->
                Assert.assertTrue(loginPage.getErrorMessage().toLowerCase().contains("username and password")));
    }
}
