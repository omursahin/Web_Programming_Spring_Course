package org.webp.intro.spring.testing.coverage.instrumentation;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Instrumentator {

    private final List<String> prefixes;

    public Instrumentator(String packagePrefixesToCover) {
        Objects.requireNonNull(packagePrefixesToCover);

        prefixes = Arrays.stream(
                packagePrefixesToCover.split(","))
                .map(s -> s.trim())
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        if (prefixes.isEmpty()) {
            throw new IllegalArgumentException("You have to specify at least one non-empty prefix, e.g. 'com.yourapplication'");
        }


    }

    /**
     * Adını kullanarak instrumented sınıfın raw byte'larını al {@code className}
     *
     * @param classLoader
     * @param className
     * @param reader
     * @return
     */
    public byte[] transformBytes(ClassLoader classLoader, ClassName className, ClassReader reader) {
        Objects.requireNonNull(classLoader);
        Objects.requireNonNull(className);
        Objects.requireNonNull(reader);

        if (!canInstrumentForCoverage(className)) {
            throw new IllegalArgumentException("Cannot instrument " + className);
        }


        int asmFlags = ClassWriter.COMPUTE_FRAMES;
        ClassWriter writer = new ComputeClassWriter(asmFlags);
        ClassVisitor cv = writer;

        //this is very we hook our instrumentation
        cv = new CoverageClassVisitor(cv, className);

        //frame okumaktan kaçınıyoruz çünkü bunları tekrar hesaplayacağız
        int readFlags = ClassReader.SKIP_FRAMES;

        /*
            Bu "Visitor Pattern" kullanımıdır. Özetle bütün sınıf tanımlarını tarıyoruz ve "writer" bufferındaki
            her bir elemanı yazdırıyoruz. Bu türde bir buffer ayrıca bizim instrumentation'ımızı da ekleyebilir.
            Daha sonra bu işlemi tamamladığımızda byte[] data'ya dönüştürüyoruz
         */
        reader.accept(cv, readFlags);

        return writer.toByteArray();
    }


    public boolean canInstrumentForCoverage(ClassName className) {

        return prefixes.stream()
                .anyMatch(s -> className.getFullNameWithDots().startsWith(s));
    }
}