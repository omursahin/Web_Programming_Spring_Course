package org.webp.intro.spring.testing.selenium.jsftests.selenium;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.webp.intro.spring.testing.selenium.jsftests.selenium.po.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public abstract class SeleniumTestBase {

    protected abstract WebDriver getDriver();

    protected abstract String getServerHost();

    protected abstract int getServerPort();


    private HomePO home;

    @BeforeEach
    public void initTest(){
        home = new HomePO(getDriver(), getServerHost(), getServerPort());

        home.toStartingPage();
        System.out.println(home.isOnPage());
        assertTrue(home.isOnPage(), "Failed to start from Home Page");
    }





    //--- Ex 00 ----------------------------------------------------------------

    @Test
    public void testEx00Date(){

        Ex00PO po = home.toEx00Page();
        LocalDate date = po.getDate();

        assertTrue(date.equals(LocalDate.now()));
    }

    //--- Ex 01 ----------------------------------------------------------------

    @Test
    public void testEx01Incr() {
        Ex01PO po = home.toEx01Page();

        int x = po.getCounterValue();
        po.clickPlus();
        int res = po.getCounterValue();
        assertEquals(x + 1, res);
    }

    @Test
    public void testEx01FailedDecr() {
        Ex01PO po = home.toEx01Page();

        po.clickReset();
        int res = po.getCounterValue();
        assertEquals(0, res);
        po.clickMinus();
        res = po.getCounterValue();
        assertEquals(0, res);
    }

    @Test
    public void testEx01Decr() {
        Ex01PO po = home.toEx01Page();

        int x = po.getCounterValue();
        po.clickPlus();
        assertEquals(x + 1, (int) po.getCounterValue());
        po.clickMinus();
        assertEquals(x, (int) po.getCounterValue());
    }

    @Test
    public void testEx01IncrAndDecr() {
        Ex01PO po = home.toEx01Page();

        po.clickReset(); //0
        po.clickMinus(); //etki olmamalı
        po.clickPlus();  // 1
        po.clickPlus();  // 2
        po.clickMinus(); // 1
        po.clickPlus();  // 2
        assertEquals(2, (int) po.getCounterValue());
    }

    //--- Ex 02 ----------------------------------------------------------------

    @Test
    public void testEx02HomeLink(){

        assertTrue(home.isOnPage());

        Ex02PO ex02 = home.toEx02Page();
        assertFalse(home.isOnPage());
        assertTrue(ex02.isOnPage());

        ex02.clickBackHome();
        assertTrue(home.isOnPage());
        assertFalse(ex02.isOnPage());

        /*
            İlk 2 örnek (ex00 ve ex01) hariç diğerleri için de aynı testler yazılabilirler.
            İlk 2 örnek template kullanmadığından id'ye sahip anasayfa bağlantısı bulunmamaktadır.
         */
    }


    //--- Ex 03 ----------------------------------------------------------------


    private final int MAX = 10; // tablodaki maximum comment sayısı

    @Test
    public void testEx03Create(){
        Ex03PO po = home.toEx03Page();

        String text = "testCreate() foo"; //yalnızca bu test için tekil bir şey

        int n = po.getNumberOfComments(); //bu noktada "n" hakkında varsayımda bulunulamaz
        po.createNewComment(text);

        int k = po.getNumberOfComments();

        //ya maximum sayıda ya da 1 artırılmış sayıda olmalı
        if(n==MAX){
            assertEquals(n , k);
        } else {
            assertEquals(n+1, k);
        }

        //yeni yorum her zaman en üstte, position 0'da olmalı
        assertEquals(text, po.getCommentText(0));
    }


    @Test
    public void testEx03Delete(){
        Ex03PO po = home.toEx03Page();

        String baseText = "testDelete() ";

        //tabloyu yeni yorumlarla doldur
        for(int i=0; i<MAX; i++) {
            int index = (MAX-1) - i; //9,8,7,..,0 yaparak en üstte 0,1,2,... olmasını sağlıyoruz
            po.createNewComment(baseText + index);
        }

        //ilk ve sonuncusu doğru mu kontrol ediyoruz
        assertEquals(0, po.extractIndex(0));
        assertEquals(9, po.extractIndex(9));

        po.deleteComment(0);
        //hepsinin pozizyonu kaydırılmış olmalı
        assertEquals(1, po.extractIndex(0));
        assertEquals(9, po.extractIndex(8));

        po.deleteComment(1);
        assertEquals(1, po.extractIndex(0)); // bunun pozisyonu aynı kalmalı çünkü bundan sonra geleni sildik
        assertEquals(3, po.extractIndex(1));
        assertEquals(9, po.extractIndex(7));
    }



    //--- Ex 04 ----------------------------------------------------------------

    @Test
    public void testEx04Request(){

        Ex04PO po = home.toEx04Page();

        po.clickRequestReset();
        assertEquals(0, po.getRequestCounter());

        po.clickRequestPlus();
        assertEquals(1, po.getRequestCounter());

        //değişmemeli
        po.clickRequestPlus();
        assertEquals(1, po.getRequestCounter());

        po.clickRequestPlus();
        assertEquals(1, po.getRequestCounter());

        //Session için olanı çağırmak, Request için olanı sıfırlayacaktır
        po.clickSessionPlus();
        assertEquals(0, po.getRequestCounter());
    }

    @Test
    public void testEx04Session(){

        Ex04PO po = home.toEx04Page();

        po.clickSessionReset();
        assertEquals(0, po.getSessionCounter());

        po.clickSessionPlus();
        assertEquals(1, po.getSessionCounter());

        po.clickSessionPlus();
        assertEquals(2, po.getSessionCounter());

        po.clickSessionPlus();
        assertEquals(3, po.getSessionCounter());

        po.clickSessionMinus();
        assertEquals(2, po.getSessionCounter());

        //Request için olanı çağırmanın Session'da etkisi olmayacaktır
        po.clickRequestPlus();
        assertEquals(2, po.getSessionCounter());
    }

    //--- Ex 05 ----------------------------------------------------------------

    @Test
    public void testEx05Params(){

        Ex05MainPO main = home.toEx05Page();

        Ex05ParamsPO params = main.clickOnNoParamsLink();
        assertEquals(0, params.getCounterOfParams());

        main = params.clickOnBackToMain();
        params = main.clickOnOneParamLink();
        assertEquals(1, params.getCounterOfParams());

        main = params.clickOnBackToMain();
        params = main.clickOnParamNoValueLink();
        assertEquals(1, params.getCounterOfParams());

        main = params.clickOnBackToMain();
        params = main.clickOnMultiParamsLink();
        assertEquals(3, params.getCounterOfParams());
    }

    //--- Ex 06 ----------------------------------------------------------------

    @Test
    public void testEx06Redirection(){

        Ex06MainPO main = home.toEx06Page();
        Ex06ResultPO result = main.clickForward();

        int x = result.getDisplayedCounter();

        main = result.clickBackToMain();
        result = main.clickForward();
        assertEquals(x+1, result.getDisplayedCounter());

        main = result.clickBackToMain();
        result = main.clickRedirect();
        assertEquals(x+2, result.getDisplayedCounter());

        result.refresh();
        //değişim yok
        assertEquals(x+2, result.getDisplayedCounter());


        main = result.clickBackToMain();
        result = main.clickForward();
        assertEquals(x+3, result.getDisplayedCounter());

        result.refresh();
        //POST tekrarlanıyor, counter artacak
        assertEquals(x+4, result.getDisplayedCounter());
    }
}
