package org.webp.intro.spring.logging;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ServiceY {

    private static final Logger log = LoggerFactory.getLogger(ServiceY.class);

    public void callService(int a, String s){

        /*
            DEBUG ve INFO modlarında string birleştirme uygun değildir.
            Çünkü çoğu zaman bu modlar aktif edilmez böylece de gereksiz yere
            ekstra string birleştirmesi yapılarak CPU boşa harcanmış olur
        */
        log.debug("BAD: calling Y with '" + a + "' and '"+ s + "' as input");

        /*
            Bu string birleştirme ile ilgili problemi engellemek için interpolation kullanılabilir
            SLF4J {} ile belirtilmiş yerleri gönderilen parametreler ile doldurur.
        */
        log.debug("GOOD: calling Y with '{}' and '{}' as input", a, s);


        throw new RuntimeException("Something weird happened");
    }
}
