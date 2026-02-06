package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.InventoryPage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryTest extends BaseTest {
    @BeforeMethod
    public void setUpInventoryTest(){
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.name("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("inventory.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("header_label")));
    }

    @Test
    public void testProductSorting(){
        InventoryPage inventoryPage = new InventoryPage(driver);

        inventoryPage.sortByPriceLow();
        List<WebElement> priceElements= inventoryPage.getAllPriceElements();
        List<Double> actualPrices = new ArrayList<>();

        for (WebElement element: priceElements){
            String priceText = element.getText().replace("$", "");
            actualPrices.add(Double.parseDouble(priceText));
        }
        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedPrices);

        Assert.assertEquals(actualPrices, expectedPrices, "Prices are not sorted correctly!");
    }

    @Test
    public void testE2EShoppingCart(){
        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.addShoppingCart();
        inventoryPage.goToCart();
        inventoryPage.fillCheckoutInfo("Alan", "Test", "M1M 1M1 ");
        inventoryPage.clickFinish();
        Assert.assertTrue(inventoryPage.isOrderSuccessful(),"Order process is failed");
    }
}
