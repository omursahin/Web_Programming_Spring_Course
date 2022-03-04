package org.webp.intro.spring.bean.jpa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Not: Spring JPA kodunu test etmek için gerekli bazı araçlar sağlar. Bunlardan biri de
 * testleri transaction kullanarak ve her bir test sonunda rollback yapılabilir şekilde yürütmeyi
 * sağlamasıdır. Ancak bunun yerine transactionların commit edildiği uygun entegrasyon testleri
 * daha fazla tercih edilmektedir. Eğer bir side-effect oluşursa da bunu manuel olarak düzeltmek
 * daha faydalı olabilir.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CounterServiceTest {

    @Autowired
    private CounterService service;


    @Test
    public void testCreateAndIncrement(){

        long id = service.createNewCounter();
        long x = service.getValueForCounter(id);

        service.increment(id);

        //cache'den değil veritabanından okuduğundan emin olmak için
        service.clearCache();

        long y = service.getValueForCounter(id);

        assertEquals(x + 1, y);
    }

    @Test
    public void testIncrementNotInATransaction(){

        long id = service.createNewCounter();
        long x = service.getValueForCounter(id);

        /*
           UYARI: bu yazma operasyonu transaction içerisinde değil.
           Ancak exception fırlatmayacaktır yalnızca em cache'inde gerçekleşir.
         */
        service.incrementNotInTransaction(id);

        //cache'den değil veritabanından okuduğundan emin olmak için
        service.clearCache();

        long y = service.getValueForCounter(id);

        //veritabanındaki değer değiştirilmemiş
        assertEquals(x, y);
    }
}
