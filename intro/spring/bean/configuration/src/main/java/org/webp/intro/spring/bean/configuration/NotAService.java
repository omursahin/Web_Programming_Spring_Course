package org.webp.intro.spring.bean.configuration;


//Not: burada annotation eksik, yalnızca basit bir Java sınıfı
public class NotAService {

    private final String value;


    public NotAService(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
