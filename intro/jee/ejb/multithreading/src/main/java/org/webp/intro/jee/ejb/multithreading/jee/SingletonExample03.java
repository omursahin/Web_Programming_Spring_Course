package org.webp.intro.jee.ejb.multithreading.jee;

import org.webp.intro.jee.ejb.multithreading.Counter;

import javax.ejb.AccessTimeout;
import javax.ejb.Singleton;
import java.util.concurrent.TimeUnit;

@Singleton
public class SingletonExample03 implements Counter {

    private int x;

    /*
        Burada lock bırakılana kadar bekleyeceğiz. Örneğin bir başka paralel olarak bu metodu
        çalıştırıyor olabilir. Ancak bu bekleme sonsuza kadar olmayacaktır. Bir timeout belirtebiliriz
     */
    @AccessTimeout(value = 2 , unit = TimeUnit.MILLISECONDS)
    @Override
    public void incrementCounter() {
        x = x + 1;
    }

    @Override
    public int getCounter() {
        return x;
    }
}
