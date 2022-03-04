package org.webp.intro.jee.jpa.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Past;
import java.lang.annotation.*;

@Past //Daha fazla kısıtlama, bugün veya daha önceki bir tarih kısıtlaması
@Constraint(validatedBy = AgeValidator.class)
@Target({  //Sınıfın hangi elemanlarına @ annotation'ları kullanılabilir
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.ANNOTATION_TYPE}
)
@Retention(RetentionPolicy.RUNTIME) // SOURCE, CLASS ve RUNTIME olarak üç farklı policy bulunur.
// SOURCE: Derleme sırasında annotation atılır ve byte kod'a yazılmaz
// CLASS: Sınıf yüklenirken atılır Byte-kod seviyesinde işlem yapılırken kullanışlıdır ve varsayılandır.
// RUNTIME: Atılmaz. Çalışma sırasında Reflection kullanımı için gereklidir ve okunabilir olurlar.
@Documented //JavaDoc ile dokümantasyon için kullanılır
public @interface Age {

    //Bu üçü varsayılan ve gerekli olan metotlardır
    String message() default "Çok genç";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    //min değerini parametre olarak kullanarak genel bir age kısıtı yapabiliriz
    int min() default 0;
}
