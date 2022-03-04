package org.webp.intro.spring.bean.configuration.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MyServiceTest {

    @Autowired
    private MyServiceImpA impA;

    @Autowired
    private MyServiceImpB impB;

    //belirsizliği çözmek için değişken adı bean adı ile aynı
    @Autowired
    private MyService myServiceImpA;

    //bir diğer belirsizliği çözme yöntemi @Qualifier kullanımıdır
    @Autowired
    @Qualifier("myServiceImpB")
    private MyService foo;

    /*
        Not: "MyServiceImpB" "myServiceImpB" kullanıldığına dikkat edin.
        Yani ilk harf küçük olacak.
        Bir bean annotation ile oluşturuluyorsa ilk harf küçük olacak şekilde sınıf adı ile
        aynı olur.
     */


    @Test
    public void testWiring(){
        /*
            not: aslında burası gereksizdir çünkü henüz inject edilmemiştir.
            Bu yüzden spring başlatırken exception fırlatacak ve bu test başlatılmayacaktır.
         */
        assertNotNull(impA);
        assertNotNull(impB);
        assertNotNull(myServiceImpA);
        assertNotNull(foo);

        //hatırlayın bunlar singleton nesneler
        assertEquals(impA, myServiceImpA);
        assertEquals(impB, foo);
    }
}