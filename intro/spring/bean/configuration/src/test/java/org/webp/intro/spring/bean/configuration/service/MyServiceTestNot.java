package org.webp.intro.spring.bean.configuration.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)@SpringBootTest
public class MyServiceTestNot {

    /*
        Bu belirsizdir çünkü MyService türünde inject edilebilir
        iki örnek bulunmaktadır bu yüzden adını belirtmemiz gerekir.

        Not: Eğer IntelliJ Ultimate Edition kullanıyorsanız bu değişken warning
        olarak işaretlenir.
     */

    @Autowired
    private MyService ambiguous;


    @Test
    public void testFailSpringInitialization(){
        /*
            Bu test hata verir.
            Sınıf adının sonunda *Not olduğuna dikkat edin.
            Böylelikle maven build sırasında bu testi çalıştırmayacaktır.
         */
    }
}
