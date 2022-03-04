package org.webp.intro.jee.ejb.time.businesslayer;

import javax.annotation.PostConstruct;
import javax.ejb.*;

@Singleton
@Startup //hızlıca başlangıçta başlat, ilk erişimi bekleme
public class AuthorBot {

    public static final String BAR = "Bar";
    public static final String MON = "Mon";
    public static final String THU = "Thu";

    public static final String POST_CONSTRUCT = "AuthorBot_init()";


    @EJB
    private NewsEJB newsEJB;


    @PostConstruct
    private void init(){
        /*
            Doğrudan constructor'da değil post constructor'da bulunmalı çünkü
            EJB kullandığımız için başta inject edilmeli
         */
        newsEJB.createNews(POST_CONSTRUCT, "@PostConstruct is called after a bean has been created");
    }

    /*
        Varsayılan değerlere dikkat edin. seconds/minutes/hours
        için varsayılan değer 0'dır * değil
     */

    @Schedule(second = "*/2" , minute="*", hour="*", persistent=false)
    public void createBar(){
        if(canCreateNews()) {
            newsEJB.createNews(BAR, "Some text");
        }
    }


    @Schedule(second = "*/3", minute="*", hour="*", dayOfWeek = "Thu", persistent=false)
    public void createSun(){
        if(canCreateNews()) {
            newsEJB.createNews(THU, "Some text");
        }
    }

    @Schedule(second = "*/2", minute="*", hour="*", dayOfWeek = "Mon", persistent=false)
    public void createMon(){
        if(canCreateNews()) {
            newsEJB.createNews(MON, "Some text");
        }
    }

    private boolean canCreateNews(){
        if(newsEJB.getAllNews().size() >= 10){
            return false;
        }
        return true;
    }
}
