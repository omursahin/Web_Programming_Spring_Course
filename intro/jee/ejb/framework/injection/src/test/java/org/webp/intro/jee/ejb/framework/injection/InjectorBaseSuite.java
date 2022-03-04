package org.webp.intro.jee.ejb.framework.injection;

import org.junit.jupiter.api.Test;
import org.webp.intro.jee.ejb.framework.injection.data.BasicInjectionClass;
import org.webp.intro.jee.ejb.framework.injection.data.CompositeInjectionClass;
import org.webp.intro.jee.ejb.framework.injection.data.EmptyClass;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Arayüz injection'ı için testler oluşturmakta kullanmak üzere bir abstract sınıf kullanıyoruz.
 */
public abstract class InjectorBaseSuite {

    /**
     * Neyi test etmek istiyorsanız onun için bu metotlar kullanılabilir.
     *
     * Not: Bazı test senaryoları başarısız olduğundan (bunların başarısız olması beklenir)
     * isimleri "*TestNot" ile biter. Böylelikle compile/package işlemleri sırasında Maven bu testleri
     * çalıştırmayacaktır
     *
     */
    protected abstract Injector getInjector();



    @Test
    public void testNullClass(){
        Injector injector = getInjector();
        assertThrows(IllegalArgumentException.class, () -> injector.createInstance(null));
    }

    @Test
    public void testString(){
        Injector injector = getInjector();
        String s = injector.createInstance(String.class);
        assertNotNull(s);
    }

    @Test
    public void testEmptyClass(){
        Injector injector = getInjector();
        EmptyClass emptyClass = injector.createInstance(EmptyClass.class);
        assertNotNull(emptyClass);
    }

    @Test
    public void testBasicInjectionClass(){
        Injector injector = getInjector();
        BasicInjectionClass basicInjectionClass = injector.createInstance(BasicInjectionClass.class);
        assertNotNull(basicInjectionClass);

        EmptyClass injected = basicInjectionClass.getInjectedEmptyClass();
        assertNotNull(injected);

        EmptyClass nonInjected = basicInjectionClass.getNonInjectedEmptyClass();
        assertNull(nonInjected); //Burası null olmalıydı
    }

    @Test
    public void testCompositeInjectionClass(){
        Injector injector = getInjector();
        CompositeInjectionClass compositeInjectionClass = injector.createInstance(CompositeInjectionClass.class);
        assertNotNull(compositeInjectionClass);

        BasicInjectionClass injectedBasicInjectionClass = compositeInjectionClass.getBasicInjectionClass();
        assertNotNull(injectedBasicInjectionClass);

        EmptyClass injectedEmptyClass = injectedBasicInjectionClass.getInjectedEmptyClass();
        assertNotNull(injectedEmptyClass);
    }


    /*
        - diziler
        - temel değerler
        - arayüzler
        - vs.
        gibi pek çok durum
     */
}
