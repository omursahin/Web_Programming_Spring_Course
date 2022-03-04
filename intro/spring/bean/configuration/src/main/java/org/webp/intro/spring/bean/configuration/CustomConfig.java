package org.webp.intro.spring.bean.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.webp.intro.spring.bean.configuration.service.MyService;

@Configuration
public class CustomConfig {

    @Bean
    public NotAService notAServiceA(@Autowired MyService myServiceImpA){
        return new NotAService(myServiceImpA.getValue());
    }
}
