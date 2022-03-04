package org.webp.intro.jee.ejb.singleton;


import javax.ejb.Singleton;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class Counter {

    /*
        Singleton bir sınıfa erişim JEE container'ı tarafından otomatik olarak senkronize
        edilir (detayları daha sonra). Ancak multi thread bir kod ile uğraşırken performans
        çok fazla etkilenmediği sürece her zaman daha fazla senkronizasyon eklemek daha iyidir.
     */
    private final AtomicInteger counter = new AtomicInteger(0);


    public void increment(){
        /*
            Gerçekleşen:
            ----------------
            int counter = 0;
            counter = counter + 1
            ----------------
            güvenli bir eylem değildir çünkü "counter + 1" yaparken bir diğer thread
            nanosaniyeler içinde değiştirmiş olabilir. Bu yüzden "counter = ..." eskimiş
            bir değer kullanabilir
         */
        counter.incrementAndGet();
    }

    public int get(){
        return counter.get();
    }
}
