package tests;

import framework.base.BaseTest;
import framework.pages.CartPage;
import framework.pages.InventoryPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    @Test(groups = {"smoke", "regression"})
    @Feature("Giỏ hàng")
    @Story("UC-010: Thêm sản phẩm vào giỏ")
    @Description("Kiểm thử thêm 1 sản phẩm vào giỏ hàng thành công")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddItemToCart() {
        InventoryPage inventoryPage = new InventoryPage(getDriver(), configReader.getTimeoutSeconds());
        CartPage cartPage = new CartPage(getDriver(), configReader.getTimeoutSeconds());

        io.qameta.allure.Allure.step("Đăng nhập với tài khoản hợp lệ", this::loginWithValidUser);
        io.qameta.allure.Allure.step("Thêm Backpack vào giỏ", inventoryPage::addBackpackToCart);
        io.qameta.allure.Allure.step("Xác nhận badge = 1", () -> Assert.assertEquals(inventoryPage.getCartBadgeValue(), "1"));
        io.qameta.allure.Allure.step("Mở giỏ hàng", inventoryPage::openCart);
        io.qameta.allure.Allure.step("Xác nhận item xuất hiện trong giỏ", () -> Assert.assertTrue(cartPage.hasItem()));
    }

    @Test(groups = {"regression"})
    @Feature("Giỏ hàng")
    @Story("UC-011: Nhiều sản phẩm trong giỏ")
    @Description("Kiểm thử thêm 2 sản phẩm vào giỏ hàng")
    @Severity(SeverityLevel.NORMAL)
    public void testAddTwoItemsToCart() {
        InventoryPage inventoryPage = new InventoryPage(getDriver(), configReader.getTimeoutSeconds());

        io.qameta.allure.Allure.step("Đăng nhập với tài khoản hợp lệ", this::loginWithValidUser);
        io.qameta.allure.Allure.step("Thêm Backpack và Bike Light", () -> {
            inventoryPage.addBackpackToCart();
            inventoryPage.addBikeLightToCart();
        });
        io.qameta.allure.Allure.step("Xác nhận badge = 2", () -> Assert.assertEquals(inventoryPage.getCartBadgeValue(), "2"));
    }
}
