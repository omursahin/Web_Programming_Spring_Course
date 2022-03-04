package org.webp.intro.spring.bean.profile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.webp.intro.spring.bean.jpa.CounterService;

import static org.junit.jupiter.api.Assertions.assertEquals;


public abstract class DbTestBase {

    @Autowired
    private CounterService service;

    @Value("${spring.jpa.database}")
    private String databaseName;


    protected abstract String getExpectedDatabaseName();

    @Test
    public void testConfiguration(){
        assertEquals(getExpectedDatabaseName(), databaseName);
    }

    @Test
    public void testCreateAndIncrement(){

        long id = service.createNewCounter();
        long x = service.getValueForCounter(id);

        service.increment(id);

        //cache'den değil veritabanından okuduğumuzdan emin olmak için
        service.clearCache();

        long y = service.getValueForCounter(id);

        assertEquals(x + 1, y);
    }
}
