package org.webp.intro.jee.ejb.multithreading.jse;

import org.webp.intro.jee.ejb.multithreading.Counter;

public class Example04  implements Counter {

    /**
     * "volatile" yok
     */
    private int x;

    @Override
    public void incrementCounter() {
        synchronized (this) {
            x = x + 1;
        }
    }

    @Override
    public int getCounter() {
        return x;
    }
}