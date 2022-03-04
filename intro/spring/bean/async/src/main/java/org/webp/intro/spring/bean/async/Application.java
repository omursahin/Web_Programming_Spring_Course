package org.webp.intro.spring.bean.async;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync // ÖNEMLİ: yoksa @Async çalışmayacaktır
@SpringBootApplication
public class Application {
}
