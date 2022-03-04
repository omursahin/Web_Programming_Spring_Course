package org.webp.intro.jee.ejb.stateful;

import javax.ejb.EJB;
import javax.ejb.Singleton;

@Singleton
public class A {

    /*
        Hatırlatma: Bu inject edilen EJB'ler mevcut örnek değil proxy'lerdir.
        Ancak, @Stateful her seferind aynı örneği gösterirken @Stateless her bir çağrıda
        farklı örnek gönderir
     */

    @EJB
    private StatefulCounter statefulCounter;
    @EJB
    private StatelessCounter statelessCounter;


    public void increment(){
        statefulCounter.increment();
        statelessCounter.increment();
    }

    public int getStateful(){
        return statefulCounter.get();
    }

    public int getStateless(){
        return statelessCounter.get();
    }
}
