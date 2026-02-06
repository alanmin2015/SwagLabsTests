package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class InventoryPage {
    WebDriver driver;

    private By sortDropdown = By.className("product_sort_container");
    private By itemPrices = By.className("inventory_item_price");
    private By cartButton = By.className("shopping_cart_link");
    private By bikeLightAddToCart = By.id("add-to-cart-sauce-labs-bike-light");
    private By backpackAddToCart = By.id("add-to-cart-sauce-labs-backpack");

    private By checkoutButton = By.id("checkout");
    private By firstNameField = By.id("first-name");
    private By lastNameField = By.id("last-name");
    private By zipCodeField = By.id("postal-code");
    private By continueButton = By.id("continue");
    private By finishButton = By.id("finish");
    private By successHeader = By.xpath("//h2[text()='Thank you for your order!']");

    public InventoryPage(WebDriver driver){
        this.driver = driver;
    }

    public void sortByPriceLow() {
        Select select = new Select(driver.findElement(sortDropdown));
        select.selectByValue("lohi");
    }

    public List<WebElement> getAllPriceElements() {
        return driver.findElements(itemPrices);
    }

    public void addShoppingCart(){
        driver.findElement(bikeLightAddToCart).click();
        driver.findElement(backpackAddToCart).click();
    }

    public void goToCart(){
        driver.findElement(cartButton).click();
    }

    public void fillCheckoutInfo(String firstName, String lastName, String zip) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(zipCodeField).sendKeys(zip);
        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
    }

    public void clickFinish() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.elementToBeClickable(finishButton)).click();
    }

    public boolean isOrderSuccessful() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successHeader)).isDisplayed();
    }

}

