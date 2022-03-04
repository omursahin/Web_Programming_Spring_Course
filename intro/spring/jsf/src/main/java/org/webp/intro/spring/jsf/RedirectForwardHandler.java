package org.webp.intro.spring.jsf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/*
    HTTP isteklerini ele alacağımız özel bean. Özellikle burada "/" dizine gelen istekleri
    durdurup index.html'e yönlendiriyoruz.
 */
@Controller
public class RedirectForwardHandler {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String forward(){
        return "forward:index.html";
    }
}
