package appline.rosgosstrah.newdmstest;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class GetNewDMSScenarioTest extends BaseTest {

    @Test
    public void exampleScenario() {
        // Выбрать пункт меню - "Меню"
        String insuranceButtonXPath = "//a[contains(text(),'Меню') and @data-toggle='dropdown']";
        List<WebElement> insuranceButtonList = driver.findElements(By.xpath(insuranceButtonXPath));
        waitUtilElementToBeClickable(insuranceButtonList.get(0));
        if (!insuranceButtonList.isEmpty()) {
            insuranceButtonList.get(0).click();
        }

        // Выбрать пункт подменю - "ДМС"
        String rgsDmsButtonPath = "//a[contains(text(),'ДМС')]";
        WebElement rgsDmsButton = driver.findElement(By.xpath(rgsDmsButtonPath));
        waitUtilElementToBeClickable(rgsDmsButton);
        rgsDmsButton.click();

        // Проверка открытия страницы "ДМС"
        Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
                "ДМС 2020 | Рассчитать стоимость добровольного медицинского " +
                        "страхования и оформить ДМС в Росгосстрах", driver.getTitle());

        // Нажать кнопку "Отправить заявку"
        String rgsSendRequest = "//a[contains(text(),'Отправить заявку')]";
        WebElement sendRequest = driver.findElement(By.xpath(rgsSendRequest));
        waitUtilElementToBeClickable(sendRequest);
        sendRequest.click();

        // Проверка открытия "Заявки"
        String rgsSendRequestPageTitle = "//b[contains(text(),'Заявка на добровольное медицинское страхование')]";
        WebElement rgsSendRequestTitleText = driver.findElement(By.xpath(rgsSendRequestPageTitle));
        waitUtilElementToBeVisible(By.xpath(rgsSendRequestPageTitle));
        Assert.assertEquals("Заголовок отсутствует/не соответствует требуемому",
                "Заявка на добровольное медицинское страхование", rgsSendRequestTitleText.getText());

//      Заполниние полей данными
        String lName = "Иванов";
        String fName = "Иван";
        String mName = "Иванович";
        String emailVal = "qwertyqwerty";
        String commentVal = "Я согласен на обработку";
        String phoneVal = " (971) 111-11-11";
        String contDateVal = "07.12.2020";
        String regionVal = "Москва"; //но при проверки на заполненность необходимо сравнивать с кодом региона "77"
        //код региона есть на сайте при просмотре кода

        // Фамилия
        String fieldXPath = "//input[contains(@data-bind, 'value:LastName')]";
        final WebElement lastName = driver.findElement(By.xpath(fieldXPath));
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "value"))), lName);

        // Имя
        fieldXPath = "//input[contains(@data-bind, 'value:FirstName')]";
        final WebElement firstName = driver.findElement(By.xpath(fieldXPath));
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "value"))), fName);

        // Отчество
        fieldXPath = "//input[contains(@data-bind, 'value:MiddleName')]";
        final WebElement middleName = driver.findElement(By.xpath(fieldXPath));
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "value"))), mName);

        // Почта
        fieldXPath = "//input[contains(@data-bind, 'value: Email')]";
        final WebElement eMail = driver.findElement(By.xpath(fieldXPath));
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "value"))), emailVal);

        // Комментарий
        fieldXPath = "//textarea[contains(@data-bind, 'value: Comment')]";
        final WebElement comment = driver.findElement(By.xpath(fieldXPath));
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "value"))), commentVal);

        // Номер телефона
        fieldXPath = "//input[contains(@data-bind, 'value: Phone')]";
        final WebElement phone = driver.findElement(By.xpath(fieldXPath));
        try {
            fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "value"))), phoneVal);

        } catch (ComparisonFailure e) {
            System.out.println(" ");
        }

        // Предпочитаемая дата контакта*
        fieldXPath = "//input[@name='ContactDate']";
        final WebElement contactDate = driver.findElement(By.xpath(fieldXPath));
        fillInputField(driver.findElement(By.xpath(String.format(fieldXPath, "value"))), contDateVal);

        // Согласие на обработку
        String acceptButtonPath = "//input[contains(@data-bind, 'checked')]";
        WebElement acceptButton = driver.findElement(By.xpath(acceptButtonPath));
        acceptButton.click();

        // Регион
        fieldXPath = "//select[contains(@data-bind, 'value:Region')]";
        final WebElement selectRegion = driver.findElement(By.xpath(fieldXPath));
        new Select(selectRegion).selectByVisibleText(regionVal);

        // Проверка на заполнение соответствующими значениями
        Assert.assertEquals("Поле не заполнено " + lastName.toString(), lastName.getAttribute("value"), lName);
        Assert.assertEquals("Поле не заполнено " + firstName.toString(), firstName.getAttribute("value"), fName);
        Assert.assertEquals("Поле не заполнено " + middleName.toString(), middleName.getAttribute("value"), mName);
        Assert.assertEquals("Поле не заполнено " + eMail.toString(), eMail.getAttribute("value"), emailVal);
        Assert.assertEquals("Поле не заполнено " + comment.toString(), comment.getAttribute("value"), commentVal);
        Assert.assertEquals("Поле не заполнено " + phone.toString(), phone.getAttribute("value"), "+7" + phoneVal);
        Assert.assertEquals("Поле не заполнено " + contactDate.toString(), contactDate.getAttribute("value"), contDateVal);
        Assert.assertEquals("Поле не заполнено " + selectRegion.toString(), "77", selectRegion.getAttribute("value"));
        Assert.assertTrue("AcceptButton is not selected", acceptButton.isSelected());

        // Нажать кнопку "Отправить"
        String sendButtonXPath = "//button[contains(text(), 'Отправить')]";
        WebElement sendButton = driver.findElement(By.xpath(sendButtonXPath));
        waitUtilElementToBeClickable(sendButton);
        sendButton.click();

        // Проверка выхода ошибки у поля электронной почты
        String errorAlertXPath = "//span[contains(text(), 'Введите адрес электронной почты')]";
        WebElement errorAlert = driver.findElement(By.xpath(errorAlertXPath));
        waitUtilElementToBeVisible(errorAlert);
        Assert.assertEquals("Проверка ошибки у alert на странице не была пройдено",
                "Введите адрес электронной почты", errorAlert.getText());
    }


    private void scrollToElementJs(WebElement element) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private void waitUtilElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    private void waitUtilElementToBeVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void waitUtilElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    private void fillInputField(WebElement element, String value) {
        scrollToElementJs(element);
        waitUtilElementToBeClickable(element);
        element.click();
        element.sendKeys(value);
        Assert.assertEquals("Поле было заполнено некорректно",
                value, element.getAttribute("value"));
    }
}