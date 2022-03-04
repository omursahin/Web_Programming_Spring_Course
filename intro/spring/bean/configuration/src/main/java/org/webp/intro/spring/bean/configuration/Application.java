package org.webp.intro.spring.bean.configuration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    /*
        Not: @SpringBootApplication aynı zamanda @Configuration'ı da çağırır.
        Annotation'ların ayrıca annotation'ları da olabilir
     */

    @Bean
    public NotAService notAServiceFoo(){
        return new NotAService("foo");
    }

    @Bean
    public NotAService notAServiceBar(){
        return new NotAService("bar");
    }
}
