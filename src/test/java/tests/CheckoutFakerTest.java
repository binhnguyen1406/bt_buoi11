package tests;

import framework.base.BaseTest;
import framework.pages.CartPage;
import framework.pages.CheckoutCompletePage;
import framework.pages.CheckoutOverviewPage;
import framework.pages.CheckoutYourInformationPage;
import framework.pages.InventoryPage;
import framework.utils.TestDataFactory;
import framework.utils.UserData;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckoutFakerTest extends BaseTest {

    @Test(groups = {"regression"})
    @Feature("Checkout")
    @Story("UC-020: Checkout thành công")
    @Description("Kiểm thử luồng checkout hoàn chỉnh với dữ liệu JSON")
    @Severity(SeverityLevel.CRITICAL)
    public void testCheckoutSuccess() {
        InventoryPage inventoryPage = new InventoryPage(getDriver(), configReader.getTimeoutSeconds());
        CartPage cartPage = new CartPage(getDriver(), configReader.getTimeoutSeconds());
        CheckoutYourInformationPage informationPage = new CheckoutYourInformationPage(getDriver(), configReader.getTimeoutSeconds());
        CheckoutOverviewPage overviewPage = new CheckoutOverviewPage(getDriver(), configReader.getTimeoutSeconds());
        CheckoutCompletePage completePage = new CheckoutCompletePage(getDriver(), configReader.getTimeoutSeconds());
        UserData userData = TestDataFactory.defaultCheckoutUser();

        io.qameta.allure.Allure.step("Đăng nhập", this::loginWithValidUser);
        io.qameta.allure.Allure.step("Thêm sản phẩm vào giỏ", inventoryPage::addBackpackToCart);
        io.qameta.allure.Allure.step("Đi đến giỏ hàng", inventoryPage::openCart);
        io.qameta.allure.Allure.step("Đi đến checkout", cartPage::clickCheckout);
        io.qameta.allure.Allure.step("Nhập thông tin người nhận", () ->
                informationPage.fillInformation(userData.getFirstName(), userData.getLastName(), userData.getPostalCode()));
        io.qameta.allure.Allure.step("Hoàn tất checkout", overviewPage::finishOrder);
        io.qameta.allure.Allure.step("Xác nhận đơn hàng thành công", () ->
                Assert.assertTrue(completePage.getCompleteHeader().toLowerCase().contains("thank you")));
    }
}
