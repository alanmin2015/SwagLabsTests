package tests;

import base.BaseTest;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.Cookie;
import pages.LoginPage;
import java.lang.reflect.*;
import java.time.Duration;

public class AuthenticationTest extends BaseTest {
    private static Cookie sessionCookie;

    @DataProvider(name = "loginData")
    public Object[][] getData(){
        return new Object[][]{
                {"standard_user", "secret_sauce", true},
                {"locked_out_user", "secret_sauce", false},
                {"problem_user", "secret_sauce", true },
                {"performance_glitch_user", "secret_sauce", true},
                {"error_user", "secret_sauce", true},
                {"visual_user", "secret_sauce", true}
        };
    }

    @BeforeMethod
    public void specificSetUp(Method method){
        if (method.getName().equals("testLogout")){
            getDriver().get("https://www.saucedemo.com/");
            if(sessionCookie == null) {
                performManualLogin();
                sessionCookie = getDriver().manage().getCookieNamed("session-username");
            }else{
                getDriver().manage().addCookie(sessionCookie);
                getDriver().get("https://www.saucedemo.com/inventory.html");
            }
        }else{
            getDriver().get("https://www.saucedemo.com/");
        }
    }

    private void performManualLogin(){
        getDriver().get("https://www.saucedemo.com/");
        getDriver().findElement(By.id("user-name")).sendKeys("standard_user");
        getDriver().findElement(By.id("password")).sendKeys("secret_sauce");
        getDriver().findElement(By.id("login-button")).click();
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password, boolean expectSuccess){
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login(username, password);

        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(3));

        if(expectSuccess){
            wait.until(ExpectedConditions.urlContains("inventory.html"));
            Assert.assertTrue(getDriver().getCurrentUrl().contains("inventory.html"));
            validateNoBrokenImages();
        } else {
            WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[@data-test='error']")));
            Assert.assertEquals(errorMsg.getText(), "Epic sadface: Sorry, this user has been locked out.", "Error message did not appear for locked user!");
        }
    }

    private void validateNoBrokenImages(){
        java.util.List<WebElement> images = getDriver().findElements(By.cssSelector(".inventory_item_img img"));
        for (WebElement img: images){
            String imageSrc = img.getAttribute("src");
            if (imageSrc != null) {
                Assert.assertFalse(imageSrc.contains("sl-404"),
                        "Broken image detected: " + imageSrc);
            } else {
                System.out.println("Warning: src attribute was null for an image element.");
            }
        }
    }

    @Test
    public void testLogout() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(3));
        wait.until(ExpectedConditions.and(
                ExpectedConditions.urlContains("inventory.html"),
                ExpectedConditions.visibilityOfElementLocated(By.className("title"))
        ));

        getDriver().findElement(By.xpath("//button[text()='Open Menu']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link"))).click();

        Assert.assertEquals(getDriver().getCurrentUrl(), "https://www.saucedemo.com/");
        boolean isLoginButtonPresent = getDriver().findElement(By.id("login-button")).isDisplayed();
        Assert.assertTrue(isLoginButtonPresent,"Login button was not found after logout!");
    }
}
