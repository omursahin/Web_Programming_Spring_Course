package org.webp.intro.jee.jpa.lock.concurrency;

public interface Counter {

    Long getId();

    int getCounter();

    void setCounter(int counter);

    /*
        Hatirlatma: Java 8 ile birlikte arayuzler varsayilan implementasyona sahip olabilirler
     */
    default void increment(){
        int x = getCounter();
        setCounter(x + 1);
    }
}
