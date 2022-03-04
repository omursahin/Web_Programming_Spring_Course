package org.webp.intro.spring.jsf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    /**
     * Paketlenmiş Java uygulamalarında bu metot giriş noktası (entry point) olmalıdır.
     * Ayrıca bu debug için de kullanışlıdır, IDE'de sağ tıkla bu sınıfı seçerek bütün uygulamayı
     * başlatabilirsiniz.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
