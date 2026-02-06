package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import listeners.TestListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;

@Listeners(TestListener.class)
public class BaseTest {
    protected static ThreadLocal<WebDriver> threadDriver = new ThreadLocal<>();


    @BeforeMethod
    public void setUp(Method method){
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--incognito");
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        WebDriver driver = new ChromeDriver(options);
        threadDriver.set(driver);
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
    }

    public WebDriver getDriver() {
        return threadDriver.get();
    }


    @AfterMethod
    public void tearDown(ITestResult result){
        if (getDriver() != null && result.getStatus() != ITestResult.FAILURE) {
            getDriver().quit();
        }
    }
}


