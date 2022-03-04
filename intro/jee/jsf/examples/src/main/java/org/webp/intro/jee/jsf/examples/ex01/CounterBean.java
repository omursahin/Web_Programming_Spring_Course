package org.webp.intro.jee.jsf.examples.ex01;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named // JSF xhtml dosyalası tarafından erişilebilmek için gerekir
@SessionScoped  // bu bean ne kadar yaşayacak belirtilir, örneğin bu durumda kullanıcı session süresince
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
