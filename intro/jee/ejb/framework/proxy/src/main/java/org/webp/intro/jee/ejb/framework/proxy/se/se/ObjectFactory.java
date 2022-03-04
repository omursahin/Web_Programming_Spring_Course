package org.webp.intro.jee.ejb.framework.proxy.se.se;


import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;


public class ObjectFactory {

    private final List<CallLog> callLogs;

    public ObjectFactory(){
        callLogs = new ArrayList<>();
    }

    /**
        Proxy yapılmış sınıfın çağrı sayısını dön
     */
    public int getTotalInvocationCount(){
        return  callLogs.stream()
                .mapToInt(CallLog::getInvocationCount)
                .sum();
    }

    /*
        Not: sadece bir interface için proxy oluşturabildiğimiz için burada somut sınıfı parametre olarak geçmemiz gerekiyor.
     */
    public <I>  I createInstance(Class<I> klassInterface, Class<? extends I> klassConcrete) {

        try {
            //somut sınıfın bir örneğini oluştur
            I instance = klassConcrete.newInstance();

            //proxy sınıftaki fonksiyonelliği geliştirmek için bir handler oluştur
            CallLog callLog = new CallLog(instance);
            // bu handlerları takip et
            callLogs.add(callLog);

            //proxy sınıf oluştur
            Object proxy = Proxy.newProxyInstance(
                    klassInterface.getClassLoader(), // sınıf yükleyici
                    new Class[]{klassInterface}, // proxy oluşturduğumuz interface
                    callLog); // proxy sınıfın fonksiyonelliğini artıran handler

            return (I) proxy;

        } catch (Exception e) {
            System.out.println("Failed to instantiate "+klassConcrete.getName()+" : "+e.toString());
            return null;
        }
    }
}
