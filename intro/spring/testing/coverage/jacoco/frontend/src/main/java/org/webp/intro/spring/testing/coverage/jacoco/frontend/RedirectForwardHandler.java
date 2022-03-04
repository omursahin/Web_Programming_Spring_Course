package org.webp.intro.spring.testing.coverage.jacoco.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/*
    localhost:8080'a erişmek istediğimizde URL "localhost:8080/"
    olarak değiştirilir. Varsayılan olarak da pek çok sunucu
    "/" kaynağı için "/index.html" dosyasını gönderir. Ancak
    burada ana sayfamız "home.xhtml" dosyasıdır ve index.html
    bulunmamaktadır.

    Bu yüzden bu bean'de biz manüel olarak "/" kök dizinini ana sayfamıza
    forward ediyoruz (302 redirect değil) ve adres satırımız "localhost:8080/"
     olarak kalmaya devam ediyor.

    Not: bu HTTP isteğinin ele alınması @Controller
    ve @RequestMapping aracılığı ile olur.
    Ayrıntılarını Spring'te REST API tasarlayacağımız "Web Programlama 2" dersinde göreceğiz.
 */

@Controller
public class RedirectForwardHandler {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String forward(){
        return "forward:home.xhtml";
    }
}
