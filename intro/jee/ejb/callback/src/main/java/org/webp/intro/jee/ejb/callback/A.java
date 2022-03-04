package org.webp.intro.jee.ejb.callback;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;

@Stateful
public class A {

    @EJB
    private B b;

    private String valueOfB;

    public A(){
        /*
            JEE container henüz dependency injection yapmadığı için 'b' deki herhangi bir metodu
            henüz burada çağıramıyoruz

            Not: "assert" kontrolü burada varsayılan olarak kapalıdır.
            Eğer "-ea" (enable assertions) parametresini JVM'e gönderirseniz açılacaktır.
         */
        assert b == null;
    }

    @PostConstruct //Burası EJB constuctor metodu çağırıldıktan sonra çalıştırılır
    public void init(){
        //Burada b değerini inject edildiği için çekebiliriz
        valueOfB = b.getValue();
    }

    public String getValue(){
        return valueOfB;
    }

    /*
         Ayrıca aşağıdaki metotlar da bulunmaktadır:
         - @PreDestroy: JEE container'ı bu örneği silmeden önce çağırılır
         - @PrePassivate: yalnızca @Stateful içindir ve JEE örneği diske kaydetmeye karar vermeden önce çağırır
         - @PostActivate: yalnızca @Stateful içindir, JEE, örneği diskten çağırıp sürdürdüğünde çağırılır
     */
}
