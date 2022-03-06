package org.webp.intro.jee.jsf.crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.webp.misc.testutils.selenium.SeleniumDriverHandler;

import java.util.List;
import java.util.stream.Collectors;

/*
    XPath syntax için bakınız:
    https://www.w3schools.com/xml/xpath_syntax.asp
 */


/*
    GitHub'ta Java projeleri taranıp Maven veya Gradle kullanıp kullanmadıklarını inceleyelim

    Not: bu yalnızca örnek bir uygulamadır, Github bunun için bir REST-api sunmaktadır.

    Not 2: Buradaki kodlar GitHub tarafından dönen HTML sayfalarına bağımlıdır.
    Eğer bir değişiklik meydana gelirse kod da hata verecektir.
 */
public class GitCrawler {

    private static final String github = "https://github.com/";



    public static void main(String[] args) {


        WebDriver driver = SeleniumDriverHandler.getFirefoxDriver();

        try {
            crawl(driver);
        } catch (Exception e){
            System.out.println("ERROR: "+e.getMessage());
            e.printStackTrace();
        }

        driver.close();
    }

    private static void openPagedSearchResult(WebDriver driver, int page){
        //En fazla Star'a sahip Java projeleri
        String search =  "search?l=&o=desc&p="+page+"&q=language%3AJava&ref=advsearch&s=stars&type=Repositories&utf8=✓";

        driver.get(github + search);

        sleep(6000);
    }

    private static void crawl(WebDriver driver) {

        for(int i=1; i <= 100; i++) {
            System.out.println("Page: "+i);
            openPagedSearchResult(driver, i);
            scanCurrentPage(driver, i);

            if(i==100){
                //arama en fazla 100 sayfa döner, devam etmek için yeni aramaya ihtiyaç var
                break;
            }
            nextPage(driver, i);
        }
    }

    private static WebElement getElement(WebDriver driver, By by, int current){

        WebElement element = null;

        while(true) {
            try {
                element = driver.findElement(by);
            } catch (Exception e){
                //Github aramayı engellerse gerçekleşebilir
                try {
                    long time = 60_000;
                    System.out.println("Cannot find -> "+by.toString()+"\n Going to wait for "+time+"ms");
                    Thread.sleep(time);
                    openPagedSearchResult(driver, current);
                } catch (InterruptedException e1) {
                }
                continue;
            }
            break;
        }

        return element;
    }

    private static void nextPage(WebDriver driver, int current) {

        WebElement next = getElement(driver, By.xpath("//a[@class='next_page']"), current);
        next.click();
        waitForPageToLoad(driver);

        sleep(6_000);
    }

    private static void sleep(int ms){
        /*
            Evet Java'nın güzelliği exception'ları yakalayabiliyor olmamız.
         */
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }

    private static void scanCurrentPage(WebDriver driver, int page) {
        String current = driver.getCurrentUrl();

        /*
            Not: GitHub'ta geçmişte layout değişiklikleri gerçekleştiğinde düzenlememişz gerekir
            Bu değişiklikler neredeyse her yıl gerçekleşir...
         */
        //String xpath = "//h3[@class='repo-list-name']/a";
        //String xpath = "//div[@class='container']//ul//h3//a";
        //String xpath = "//ul[contains(@class,'repo-list')]//h3//a";
        String xpath = "//ul[contains(@class,'repo-list')]//div[contains(@class,'f4 text-normal')]//a";

        List<WebElement> projects = driver.findElements(By.xpath(xpath));
        List<String> names = projects.stream().map(p -> p.getText()).collect(Collectors.toList());

        System.out.println("Going to analyze "+names.size()+" projects in this page at: "+current);

        while (!names.isEmpty()) {

            String name = names.remove(0);

            By byName = By.xpath(xpath + "[text()='" + name + "']");
            WebElement a = getElement(driver, byName, page);
            a.click();
            Boolean loaded = waitForPageToLoad(driver);

            if (loaded) {

                /*
                    Maven mi yoksa Gradle mi kontrol et

                    Note: bu kontrol robust değildir:
                    - build dosyası root'da olmayabilir
                    - sayfa yüklenirken hata meydana gelirse diye bir kontrol yok
                 */
                List<WebElement> maven = driver.findElements(By.xpath("//div[contains(@class,'content')]//a[@title='pom.xml']"));
                if (!maven.isEmpty()) {
                    System.out.println("" + name + " uses Maven");
                } else {
                    List<WebElement> gradle =  driver.findElements(By.xpath("//div[contains(@class,'content')]//a[@title='build.gradle']"));
                    if (!gradle.isEmpty()) {
                        System.out.println("" + name + " uses Gradle");
                    } else {
                        System.out.println("" + name + " undefined build system");
                    }
                }
            }

            /*
                    GitHub'a çok fazla istek atmayınız, dakika başına 10 adet yeterlidir.
                    Hatırlatma: Eğer GitHub üzerinde arama yapacaksanız Github ile support@github.com üzerinden
                    iletişime geçilmesi gerekir.
                    https://github.com/robots.txt
            */
            sleep(6000);

            driver.get(current);
            waitForPageToLoad(driver);

            sleep(6000);
        }
    }

    private static Boolean waitForPageToLoad(WebDriver driver) {

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, 10);

        //sayfa tamamen yüklendiğinde ve hazır olduğunda "true" dönene kadar verilen JS'yi yürütmeye devam edin
        return wait.until((ExpectedCondition<Boolean>) input -> {
            String res = jsExecutor.executeScript("return /loaded|complete/.test(document.readyState);").toString();
            return Boolean.parseBoolean(res);
        });
    }

}
