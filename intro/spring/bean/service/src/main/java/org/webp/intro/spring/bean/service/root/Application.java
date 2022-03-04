package org.webp.intro.spring.bean.service.root;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot uygulamasının başlangıç noktası
 * Yalnızca bir sınıf @SpringBootApplication olarak annotate edilebilir
 * Eğer daha fazla bulunursa Spring çalışma zamanında problem çıkarak ve exception fırlatacaktır
 *
 * Varsayılan olarak Spring mevcut paket içindeki bütün dosyaları tarar
 * Classpath'de bulduklarıyla da "bean" oluşturur.
 *
 * Farklı türde beanler olsa da EJB'deki proxy sınıflar gibi benzer
 * konsepte sahiptir
 */
@SpringBootApplication
public class Application {
}
