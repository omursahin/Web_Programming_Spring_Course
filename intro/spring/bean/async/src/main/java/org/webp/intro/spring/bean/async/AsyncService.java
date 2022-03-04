package org.webp.intro.spring.bean.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;


@Service
public class AsyncService {

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
        Not: @EnableAsync ayarlaması yapmak gerekir
     */

    @Async
    public void compute(){

        try {
            //2 saniye bekleterek hesaplama simülasyonu yapıyoruz
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
        }

        //x'i değiştirerek metodu tamamlıyoruz
        x = 42;
    }

    /*
        Eğer bizim için sonuç önemliyse, Future nesnesi ile async metot tamamlanana kadar
        bekleyebiliriz
     */
    @Async
    public Future<String> asyncResult(){

        try {
            //2 saniye bekleterek hesaplama simülasyonu yapıyoruz
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
        }

        return new AsyncResult<>("foo");
    }
}
