package org.webp.intro.spring.testing.coverage.instrumentation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * bu metottaki sınıflar hangi test nereyi çalıştırıyor/kapsıyor izlemek için
 * SUT'a inject edilir
 */
public class ExecutionTracer {

    /**
     * UYARI:
     * Genel bir kural olarak değiştirilebilir statik değişkenleri kullanmamalısınız.
     * (final temel kısıtlamalar veya immutable nesneler kullanılabilir).
     * Buradaki kulalndığımız oldukça özel bir durumdur.
     *
     *
     * Key -> class name
     * </ br>
     * Value -> kapsanması durumunu takip edebileceğimiz satır numaralarını mapleyen boolean değer
     */
    private static final Map<String, Map<Integer, Boolean>> coverage =
            new ConcurrentHashMap<>(65536);


    public static void reset() {
        coverage.values().stream().forEach(m ->
                    m.keySet().stream().forEach(k -> m.put(k, false))
                );
    }


    public static final String EXECUTED_LINE_METHOD_NAME = "executedLine";
    /**
     * bir metodu tekil olarak tanımlamamız gerkmektedir. Bunu adını ve parametre tiplerini
     * kullanarak yapıyoruz
     * JVM'de tip tanımlayıcıların kendi sembolleri bulunur:
     * L<class>; ->  verilen sınıfın nesnesi
     * I -> int
     * V -> void
     * (XY)Z -> X ve Y metodun girdi tipleri, Z ise return tipidir
     *
     * böylelikle "(Ljava/lang/String;I)V" anlamı String ve int girdi, void ise çıktı değeridir
     */
    public static final String EXECUTED_LINE_DESCRIPTOR = "(Ljava/lang/String;I)V";

    /**
     * belirli bir satırın çalıştırıldığının raporu
     * instrumented sınıfa inject etmek istediğimiz metot budur
     */
    public static void executedLine(String className, int line) {

        coverage.get(className).put(line, true);
    }


    public static void initTarget(String className, int line){

        coverage.computeIfAbsent(className, k -> new ConcurrentHashMap<>())
                .put(line, false);
    }

    public static double getCoverage(String name){

        String bytecodeName = ClassName.get(name).getBytecodeName();

        if(! coverage.containsKey(bytecodeName)){
            System.out.println("Class "+name+" was not instrumented");
            return 0;
        }

        Map<Integer, Boolean> map = coverage.get(bytecodeName);
        return (double) map.values().stream().filter(v -> v).count() /  (double) map.size();
    }
}
