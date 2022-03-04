package org.webp.intro.spring.testing.coverage.instrumentation;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LineCovMethodVisitor extends MethodVisitor {

    private final String className;
    private final String methodName;


    public LineCovMethodVisitor(MethodVisitor mv,
                                String className,
                                String methodName,
                                String descriptor) {
        super(Opcodes.ASM5, mv);

        this.className = className;
        this.methodName = methodName;
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        super.visitLineNumber(line, start);


        /*
            Bu metot "visitor"'ın LINENUMBER ifadesine her erişmesinde çağırılır

            Her bir satır sonrasına instrumentation'ımız satırın geçmesi/çalıştırılmasını
            takip edebileceğimiz satır eklenir.

            Burada bir satırı tekil olarak tanımlayan 2 elemanı
            stack'e push ediyoruz. Daha sonra bu 2 elemanı girdi olarak pop eden ExecutionTracer
            çağırılıyor
         */

        this.visitLdcInsn(className);
        this.visitLdcInsn(line);

        mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                ClassName.get(ExecutionTracer.class).getBytecodeName(),
                ExecutionTracer.EXECUTED_LINE_METHOD_NAME,
                ExecutionTracer.EXECUTED_LINE_DESCRIPTOR,
                ExecutionTracer.class.isInterface()); //false

        /*
            Ortalama kapsam hesaplanırken gerektiğinden
            bütün sınıf dosyaları taranırken satır da kaydedilir.
         */
        ExecutionTracer.initTarget(className, line);
    }


    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        /*
            stack'e 2 değer push edilir bu yüzden ASM'ye bu instrumented metodun
            en az 2 elemanlı bir frame'i olması gerektiğini söylüyoruz

            Not: burada satırları instrument yaptığımızdan frame'ler boş diyebilir
            bu yüzden maxElementsAddedOnStackFrame +  maxStack yerine Math.max(maxElementsAddedOnStackFrame, maxStack)
            kullanıyoruz
         */
        int maxElementsAddedOnStackFrame = 2;
        super.visitMaxs(Math.max(maxElementsAddedOnStackFrame, maxStack), maxLocals);
    }
}
