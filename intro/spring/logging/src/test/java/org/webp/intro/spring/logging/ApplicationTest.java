package org.webp.intro.spring.logging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = NONE)
public class ApplicationTest {


   @Test
   public void test(){

       /*
            Burada gerçekte bir şey test etmeyeceğiz sadece Spring başladığı zaman
            logları görmek için çalıştırıyoruz.
            Testlerde bazı sınıfların DEBUG seviyesinde olması gibi farklı ayarlara
            sahip olabiliriz. Bu ayarlama logback-test.xml dosyasında gerçekleştirilir. Bu
            dosyanın logback.xml'e göre önceliği bulunur.
       */
   }
}