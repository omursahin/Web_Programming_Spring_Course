package org.webp.intro.spring.logging;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ServiceX {

    /*
        Logger'lar isimlendirilmelidir. Genel uygulama ise hangi sınıfta kullanılıyorsa
        o sınıfın adının verilmesidir. Böylelikle her sınıfın kendina ait logger'ı olacaktır.
        Bunun bazı avantajları bulunmaktadır:
        - Logger içerisinde isim bulunduğu için log hangi sınıftan geliyor kolaylıkla bulunabilir
        - Debug etmek istediğimiz sınıfa daha düşük (DEBUG gibi) log seviyesi ayarlanabilir
        - Paket prefix'leri kullanılarak bir paket içindeki bütün loggerlar için log seviyesi ayarlanabilir.
          Örneğin Spring'in ne yaptığını incelemek isterseniz org.springframework'ün seviyesini DEBUG
          yapabilirsiniz.
          Log seviyeleri logback.xml dosyasından ayarlanabilir
     */
    private static final Logger log = LoggerFactory.getLogger(ServiceX.class);

    @Autowired
    private ServiceY y;

    @PostConstruct
    public void init(){

        log.debug("Initializing Service X");

        try{
            y.callService(42, "foo");
        }catch (Exception e){
            /*
                not: hata olması durumunda (daha az olacağı için (en azından umut ettiğimiz bu))
                string birleştirme sorun olmayacaktır ancak her zaman hataları kaydetmeliyiz.
             */
            log.error("Error when calling service Y: " + e.getMessage());
        }

        log.info("Service X is initialized");
    }
}
