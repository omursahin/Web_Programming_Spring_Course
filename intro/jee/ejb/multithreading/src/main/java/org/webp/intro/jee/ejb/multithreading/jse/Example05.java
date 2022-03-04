package org.webp.intro.jee.ejb.multithreading.jse;

import org.webp.intro.jee.ejb.multithreading.Counter;

public class Example05 implements Counter {

    private volatile int x;

    /*
        Synchronized doğrudan metoda konabilir ve bu da "synchronized(this)" eşdeğeridir
     */

    @Override
    public synchronized void incrementCounter() {
        x = x + 1;
    }

    @Override
    public int getCounter() {
        return x;
    }
}