package org.jqassistant.plugin.jee.injection.test.set.javax;

import javax.annotation.Resource;
import javax.inject.Inject;

public class JavaxInjectableA {

    private JavaxInjectableB fieldOfInjectableB;

    private static JavaxInjectableC fieldOfInjectableC;

    private final JavaxInjectableD fieldOfInjectableD;

    @Resource
    private JavaxApplicationResource resource;

    @Inject
    public JavaxInjectableA(JavaxInjectableB injectableB, JavaxInjectableD injectableD) {
        this.fieldOfInjectableB = injectableB;
        this.fieldOfInjectableD = injectableD;
    }

    public void manipulateField() {
        // Illegal modification
        fieldOfInjectableB = null;
    }

    public void accessFieldStatically() {
        // Illegal access
        fieldOfInjectableC = null;
    }

    public void injectableInstantiation() {
        JavaxInjectableB javaxInjectableB = new JavaxInjectableB();
    }
}
