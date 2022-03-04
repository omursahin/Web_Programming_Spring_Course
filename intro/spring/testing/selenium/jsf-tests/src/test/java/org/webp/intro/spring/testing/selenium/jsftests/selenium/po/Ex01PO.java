package org.webp.intro.spring.testing.selenium.jsftests.selenium.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webp.misc.testutils.selenium.PageObject;

/**
 * Page Object'e buradan bakabilirsiniz:
 *
 * http://martinfowler.com/bliki/PageObject.html
 *
 */
public class Ex01PO extends PageObject {


    public Ex01PO(PageObject po) {
        super(po);
    }



    @Override
    public boolean isOnPage(){
        return getDriver().getTitle().contains("Basit sayaç");
    }

    /*
        HTML doğrudan değiştirilmek istenirse (örneğin kaynak kodlara erişim),
        en kolay yaklaşım XPath ile id'si verilen elemana doğrudan erişmektir.

        Not: Bazı ID'ler JSF tarafından değiştirilebilirler. Örneğin form'lar,
        form id'si ve buton/diğer elemanlar'ın id'leri gibi değerlerle birleştirilirler.
     */

    public void clickPlus(){
        WebElement button = getDriver().findElement(By.id("form:plusButtonId"));
        button.click();
        waitForPageToLoad();
    }


    public void clickMinus(){
        WebElement button = getDriver().findElement(By.id("form:minusButtonId"));
        button.click();
        waitForPageToLoad();
    }

    public void clickReset(){
        WebElement button = getDriver().findElement(By.id("form:resetButtonId"));
        button.click();
        waitForPageToLoad();
    }

    public Integer getCounterValue(){
        WebElement text = getDriver().findElement(By.id("form:counterTextId"));
        String value = text.getText();
        try{
            return Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
    }
}
