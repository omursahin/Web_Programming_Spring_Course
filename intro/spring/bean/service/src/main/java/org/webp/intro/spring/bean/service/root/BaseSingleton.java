package org.webp.intro.spring.bean.service.root;

import org.springframework.stereotype.Service;

/**
 * Varsayılan olarak bir @Service EJB'deki @Singleton ile benzerdir ancak metotları ile otomatik olarak
 * senkronize olmaz
 * but WITHOUT automated synchronization on its methods
 */
@Service
public class BaseSingleton {

    private int counter;

    public void increment(){
        int x = getCounter();
        setCounter(x + 1);
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
