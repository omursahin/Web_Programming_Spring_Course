package org.webp.intro.spring.testing.coverage.jacoco.backend;

/**
 * Bu modülde çağırılmayan ve test edilmeyen örnek bir sınıf,
 * Ancak bu sınıf frontend modülünde çağırılmaktadır. Böylelikle
 * "transitive" coverage nasıl çalışıyor görebiliriz.
 */
public class DefaultDataValue {


    public static String get(){
        return "No data";
    }
}
