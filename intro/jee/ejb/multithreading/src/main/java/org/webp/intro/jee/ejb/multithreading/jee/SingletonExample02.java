package org.webp.intro.jee.ejb.multithreading.jee;

import org.webp.intro.jee.ejb.multithreading.Counter;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

@Lock(LockType.WRITE)  // varsayılan: bütün metotlar synchronized
@Singleton
public class SingletonExample02 implements Counter {

    private int x;

    @Lock(LockType.READ) //varsayılanı değiştir: eş zamanlı erişime izin ver
    @Override
    public void incrementCounter() {
        x = x + 1;
    }

    @Override
    public int getCounter() {
        return x;
    }
}
