package org.webp.intro.spring.deployment;

import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class CounterController {

    private static final String COUNTER_NAME = "MAIN_COUNTER";

    @Autowired
    private CounterService service;


    public void increaseCounter(){
        service.increment(COUNTER_NAME);
    }

    public long getCounter(){
        return service.getValueForCounter(COUNTER_NAME);
    }
}
