package org.webp.intro.jee.ejb.framework.proxy.se.manual;

/**
    Proxy sınıfta temel fikir orijinal sınıfı genişletmektir (burada Foo).
    Bu yüzden tam olarak aynı metotlara sahip olacaktır. Her bir çağrı sonucunda
    proxy yapmış olduğumuz sınıfın özel örneğine çağrı devredileecektir. Buradaki
    amacımız bu eylem gerçekleşmeden önce ve sonra istemiş olduğumuz eylemleri
    (transaction başlatma ve commit gibi) gerçekleştirebiliriz.

    Bu türde proxy sınıflar Java EE container'ı tarafından otomatik olarak oluşturulmalıdır
    Gerçek örnek FooProxy türünde olsa da, istemci (ör kodumuz) yalnızca Foo'nun referansını görebilmelidir.

    Not: Java EE'nin nasıl yaptığı oldukça karmaşıktır çünkü bytecode manipülasyonu ile gerçekleştirir
 */
public class FooProxy extends Foo{

    private final Foo original;

    public FooProxy(Foo original) {
        this.original = original;
    }


    @Override
    public String someMethod(){

        // öncesinde bir şeyler yap
        // ör bir transaction başlat

        String result = original.someMethod();

        //ardından bir şeyler yap
        //ör transaction'ı commit et
        return result;
    }
}
