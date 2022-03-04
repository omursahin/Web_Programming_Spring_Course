package org.webp.intro.jee.ejb.async;

import javax.ejb.Asynchronous;
import javax.ejb.Singleton;

@Singleton
public class A {

    private int x;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }


    /*
        çağırılan metoda gelecek olan return değeri önemli olmadığı
        maliyetli işlemleri yaparken kullanışlıdır.
        Farklı bir threadde çalıştırılarak hesaplama yapar.
     */
    @Asynchronous
    public void compute(){

        try {
            //2 saniye bekleterek hesaplamayı simüle ediyoruz
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
        }

        //Metot tamamlandığında x değeri değişecek
        x = 42;
    }
}
