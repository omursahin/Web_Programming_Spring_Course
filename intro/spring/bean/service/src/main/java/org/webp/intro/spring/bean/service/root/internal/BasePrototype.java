package org.webp.intro.spring.bean.service.root.internal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service
//scope'u varsayılan olan Singleton'dan değiştir
@Scope("prototype")
public class BasePrototype {

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
