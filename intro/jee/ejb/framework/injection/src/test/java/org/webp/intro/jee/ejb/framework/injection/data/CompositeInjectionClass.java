package org.webp.intro.jee.ejb.framework.injection.data;

import org.webp.intro.jee.ejb.framework.injection.AnnotatedForInjection;

public class CompositeInjectionClass {

    @AnnotatedForInjection
    private BasicInjectionClass basicInjectionClass;


    public BasicInjectionClass getBasicInjectionClass() {
        return basicInjectionClass;
    }
}
