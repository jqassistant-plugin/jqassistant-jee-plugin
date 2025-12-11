package org.jqassistant.plugin.jee.injection.test.set.javax;

import javax.annotation.Resource;
import javax.inject.Inject;

public class JavaxInjectableA {

    private JavaxInjectableB fieldOfInjectable1;

    private final JavaxInjectableD fieldOfInjectable2;

    private static JavaxInjectableC fieldOfInjectable3;

    @Resource
    private JavaxApplicationResource resource;

    @Inject
    public JavaxInjectableA(JavaxInjectableB fieldOfInjectable1, JavaxInjectableD fieldOfInjectable2) {
        this.fieldOfInjectable1 = fieldOfInjectable1;
        this.fieldOfInjectable2 = fieldOfInjectable2;
    }

    public void manipulateField() {
        // Illegal modification
        fieldOfInjectable1 = null;
    }

    public void accessFieldStatically() {
        // Illegal access
        fieldOfInjectable3 = null;
    }

    public void injectableInstantiation() {
        JavaxInjectableB javaxInjectableB = new JavaxInjectableB();
    }
}
