package org.webp.intro.spring.security.manual.selenium.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.webp.misc.testutils.selenium.PageObject;

import java.util.List;

public class IndexPO extends PageObject {

    public IndexPO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    public IndexPO(PageObject other){
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Login Example");
    }

    public void toStartingPage() {
        getDriver().get(host+":"+port);
        waitForPageToLoad();
        doLogout();
    }

    public boolean isLoggedIn(){
        /*
            Bulunmama ihtimali olan elemanlarla çalışırken "findElement" yerine "findElements"
            kullanmalıyız çünkü eleman bulunmazsa "findElement" exception fırlatacaktır.
         */
        return getDriver().findElements(By.id("logoutForm:logoutButton")).size() > 0;
    }

    public boolean isLoggedOut(){
        return getDriver().findElements(By.id("loginButton")).size() > 0 ;
    }

    public void doLogout(){
        if(isLoggedIn()){
            clickAndWait("logoutForm:logoutButton");
        }
    }

    /*
        Login eylemi bizi yeni bir sayfaya yönlendireceğinden bu metotda da bir Page Object göndeririz.
        Eğer sayfa geçişi gerçekleşmediyse null değeri gönderilir.
     */
    public LoginPO doLogin(){

        if(isLoggedIn()){
            return null;
        }

        clickAndWait("loginButton");

        LoginPO po = new LoginPO(this);

        return po;
    }

    public int getNumberOfPosts(){

        return getDriver()
                .findElements(By.xpath("//div[@id = 'posts']//div[@class = 'post']"))
                .size();
    }

    public int getNumberOfPostsThatCanBeDeleted(){

        return getDriver()
                .findElements(By.xpath("//div[@id = 'posts']//div[@class = 'post']//div[@class = 'deleteButton']"))
                .size();
    }

    public boolean createPost(String text){

        List<WebElement> input = getDriver().findElements(By.id("postForm:postText"));
        if(input.isEmpty()){
            //eğer login gerçekleşmediyse olabilir
            return false;
        }

        setText("postForm:postText", text);
        clickAndWait("postForm:createPost");

        return true;
    }
}
