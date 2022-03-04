package org.webp.intro.jee.ejb.framework.proxy.se.se;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class CallLog implements InvocationHandler {

    //multithread problemlerini engellemek için yalnızca bir atomic integer değeri kullanıyoruz
    private final AtomicInteger counter = new AtomicInteger(0);

    //orijinal örneğimiz
    private final Object instance;

    public CallLog(Object instance) {
        this.instance = instance;
    }

    /*
        Bu metot her bir proxy sınıftan metot çağırıldığında çağırılacak
     */
    @Override
    public Object invoke(
            Object proxy, // proxy sınıfın referansı
            Method method, // proxy'de çağırılan metodun tanımı
            Object[] args // proxy'deki çağırılan metodun girdileri
    ) throws Throwable {

        /*
            orijinal örnek çağırılmadan önce çalıştırılacak olan kod.

            Her bir çağrıda sayacı artır ve konsola yaz.
            Yani, metodun kaç defa ne sıklıkla çağırıldığını takip edeceğiz.
         */
        counter.incrementAndGet();
        System.out.println("INTERCEPTED CALL TO: "+method.getName());

        /*
            not: gerçek çağrı instance içerisinde gerçekleşir proxy'de değil. Bu Method descriptor kullanılarak
            reflection ile yapılır.

            Yani, şöyle bir şey:

            foo.someMethod(x)

            reflection kullanarak:

            methodDescriptor("someMethod").invoke(foo, x)
            olur
         */
        return method.invoke(instance, args);
    }

    public int getInvocationCount(){
        return counter.get();
    }
}
