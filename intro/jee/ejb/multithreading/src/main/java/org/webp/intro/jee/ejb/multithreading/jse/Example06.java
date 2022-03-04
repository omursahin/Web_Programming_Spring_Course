package org.webp.intro.jee.ejb.multithreading.jse;

import org.webp.intro.jee.ejb.multithreading.Counter;

public class Example06 implements Counter {

    private int x;

    private final Object lock = new Object();

    @Override
    public void incrementCounter() {

        /*
            Eğer synchronized bloğu kullanmak isterseniz pratikte "this" kullanımı
            çok da uygun değildir çünkü çünkü sınıf dışından erişilebilir durumdadır.
            Bu da deadlock'a yol açabilir. Dahili bir private nesne ile bu engellenebilir.
            Böylelikle yalnızca bu sınıf içindeki kodlar deadlock'a sebep olabilir.
         */

        synchronized (lock) {
            x = x + 1;
        }
    }

    @Override
    public int getCounter() {
        return x;
    }
}
