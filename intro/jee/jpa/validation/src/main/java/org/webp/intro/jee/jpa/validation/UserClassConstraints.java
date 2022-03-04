package org.webp.intro.jee.jpa.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = UserClassConstraintsValidator.class)
@Target({
        ElementType.TYPE,
        ElementType.ANNOTATION_TYPE}
)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserClassConstraints {

    String message() default "User state'inde geçersiz kısıtlama";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
