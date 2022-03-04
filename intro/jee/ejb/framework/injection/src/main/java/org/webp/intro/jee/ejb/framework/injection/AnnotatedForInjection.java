package org.webp.intro.jee.ejb.framework.injection;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Bir alanın inject edilip edilmemesi gerektiğini belirten Annotation
 */
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface AnnotatedForInjection {
}
