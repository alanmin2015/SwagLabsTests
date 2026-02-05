import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.Cookie;
import java.lang.reflect.*;
import java.time.Duration;

public class LoginTest {
    WebDriver driver;
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
    public void setUp(Method method){
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        if (method.getName().equals("testLogout")){
            driver.get("https://www.saucedemo.com/");
            if(sessionCookie == null) {
                performManualLogin();
                sessionCookie = driver.manage().getCookieNamed("session-username");
            }else{
                driver.manage().addCookie(sessionCookie);
                driver.get("https://www.saucedemo.com/inventory.html");
            }
        }else{
            driver.get("https://www.saucedemo.com/");
        }
    }

    private void performManualLogin(){
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
    }


    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password, boolean expectSuccess){
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        if(expectSuccess){
            wait.until(ExpectedConditions.urlContains("inventory.html"));
            Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"));
        } else {
            WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[@data-test='error']")));
            Assert.assertEquals(errorMsg.getText(), "Epic sadface: Sorry, this user has been locked out.", "Error message did not appear for locked user!");
        }
    }

    @Test
    public void testLogout() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.and(
                ExpectedConditions.urlContains("inventory.html"),
                ExpectedConditions.visibilityOfElementLocated(By.className("title"))
        ));

        driver.findElement(By.xpath("//button[text()='Open Menu']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link"))).click();

        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/");
        boolean isLoginButtonPresent = driver.findElement(By.id("login-button")).isDisplayed();
        Assert.assertTrue(isLoginButtonPresent,"Login button was not found after logout!");
}

    @AfterMethod
    public void tearDown(){
        if(driver != null){
            driver.quit();
        }
    }
}
