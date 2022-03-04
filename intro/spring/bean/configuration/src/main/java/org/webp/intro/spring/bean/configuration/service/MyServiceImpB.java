package org.webp.intro.spring.bean.configuration.service;

import org.springframework.stereotype.Service;

@Service
public class MyServiceImpB implements MyService {
    @Override
    public String getValue() {
        return "B";
    }
}
