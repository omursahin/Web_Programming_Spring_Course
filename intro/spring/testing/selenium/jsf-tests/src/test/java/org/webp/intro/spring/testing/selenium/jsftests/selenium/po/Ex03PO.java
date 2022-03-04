package org.webp.intro.spring.testing.selenium.jsftests.selenium.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.webp.misc.testutils.selenium.PageObject;

import java.util.List;

public class Ex03PO extends TemplatePO {


    public Ex03PO(PageObject po) {
        super(po);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("JSTL (JSP Standard Tag Library) Örneği");
    }


    public int extractIndex(int position){
        String comment = getCommentText(position);
        return Integer.parseInt(comment.split(" ")[1]);
    }

    public void createNewComment(String comment){
        WebElement textArea = getDriver().findElement(By.id("createForm:createText"));
        textArea.clear();
        textArea.sendKeys(comment);
        WebElement button = getDriver().findElement(By.id("createForm:createButton"));
        button.click();

        waitForPageToLoad();
    }

    public int getNumberOfComments(){
        /*
            Biraz zorlayıcı: burada XPath kullanmak gerekir

            https://www.w3schools.com/xml/xpath_syntax.asp

            Buradaki temel fikir HTML/XML web sayfası üzerinde bir sorgu tanımlamamız ve
            bu sorguyu karşılayan bütün elemanların referanslarını almaktır.
            Burada:

            //table  :  document içindeki herhangi bir "table" tagı
            [@id='commentTable'] : <table> tagları arasında yalnızca id'si 'commentTable' olanı al
            //tbody : seçilen tablo(lar) içinde sadece <tbody>'leri al
            //tr[string-length(text()) > 0] : text içeriği boş olmayan <tr>'leri (table row) al

            Böylelikle bir sayfada kaç adet yorum bulunuyor kontrol edebiliriz. Var olan tabloda kaç adet satır bulunuyor
            kontrol etmemiz yeterlidir.

            Not: Bir XPath doğru çalışıyor mu denemenin en kolay yolu doğrudan tarayıcıda denemektir. Bunun için Chrome kullanıyor
            iseniz "Developer tools" içerisinde Elements'i seçip arama yapabilirsiniz.
         */
        List<WebElement> elements = getDriver().findElements(
                By.xpath("//table[@id='commentTable']//tbody//tr[string-length(text()) > 0]"));
        return elements.size();
    }

    public String getCommentText(int position){

        String htmlPos = "" + (position + 1);// XPath 1'den başlar, 0'dan değil

        WebElement comment = getDriver().findElement(
                By.xpath("//table[@id='commentTable']//tbody//tr["+htmlPos+"]/td[2]"));

        return comment.getText();
    }


    public void deleteComment(int position){
        String htmlPos = "" + (position + 1);// XPath 1'den başlar, 0'dan değil

        WebElement button = getDriver().findElement(
                By.xpath("//table[@id='commentTable']//tbody//tr["+htmlPos+"]/td[3]/form/input[@value = 'Delete']"));
        button.click();

        waitForPageToLoad();
    }
}
