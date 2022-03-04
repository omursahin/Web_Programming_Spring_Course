package org.webp.intro.spring.testing.coverage.instrumentation;

import java.util.Objects;

public class ClassName {

    /**
     * byte kodda "." yerine "/" kullanılır
     * ör
     * org.bar.Foo, org/bar/Foo olur
     */
    private final String bytecodeName;

    /**
     * genellikle Foo.class.getName() ile dönen sonuç,
     * örnek
     * org.bar.Foo
     */
    private final String fullNameWithDots;

    public static ClassName get(Class<?> klass){
        return new ClassName(klass);
    }

    public static ClassName get(String name) {
        return new ClassName(name);
    }

    public ClassName(Class<?> klass){
        this(Objects.requireNonNull(klass).getName());
    }

    public ClassName(String name){
        Objects.requireNonNull(name);

        if(name.contains("/") && name.contains(".")){
            throw new IllegalArgumentException("Do not know how to handle name: " +name);
        }

        if(name.contains("/")){
            bytecodeName = name;
            fullNameWithDots = name.replace("/", ".");
        } else {
            bytecodeName = name.replace(".", "/");
            fullNameWithDots = name;
        }
    }

    /**
        byte kod instruction'larında kullanılan sınıf adı.
        Yani foo.bar.Hello tanımı foo/bar/Hello olur
     */
    public String getBytecodeName() {
        return bytecodeName;
    }

    /**
     * Örnek, foo.bar.Hello
     * @return
     */
    public String getFullNameWithDots() {
        return fullNameWithDots;
    }

    public String getAsResourcePath(){
        return bytecodeName + ".class";
    }

    @Override
    public String toString(){
        return "[" + this.getClass().getSimpleName()+": "+fullNameWithDots+"]";
    }
}
