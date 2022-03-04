package org.webp.intro.spring.jsf.ex01;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named // JSF xhtml dosyaları tarafından erişilebilmesi için gerekli
@SessionScoped  // bu bean ne kadar var olmaya devam edecek onu belirtiyoruz, bu senaryoda kullanıcı session'ı boyunca devam eder
public class CounterBean implements Serializable{

    private int counter;

    public int getCounter(){
        return counter;
    }

    public void increaseCounter(){
        counter++;
    }

    public void decreaseCounter(){
        if(counter > 0){
            counter--;
        }
    }

    public void reset(){
        counter = 0;
    }
}
