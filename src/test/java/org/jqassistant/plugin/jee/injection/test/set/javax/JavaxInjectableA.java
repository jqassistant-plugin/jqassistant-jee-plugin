package org.jqassistant.plugin.jee.injection.test.set.javax;

import javax.annotation.Resource;
import javax.inject.Inject;

public class JavaxInjectableA {

    private JavaxInjectableB fieldOfInjectableB;

    private JavaxNonCdiOrEjbInjectable nonCdiOrEjbInjectable;

    private static JavaxInjectableC fieldOfInjectableC;

    private final JavaxInjectableD fieldOfInjectableD;

    @Resource
    private JavaxApplicationResource resource;

    @Inject
    public JavaxInjectableA(JavaxInjectableB injectableB, JavaxInjectableD injectableD) {
        this.fieldOfInjectableB = injectableB;
        this.fieldOfInjectableD = injectableD;
    }

    public JavaxInjectableA(JavaxNonCdiOrEjbInjectable nonCdiOrEjbInjectable, JavaxInjectableD fieldOfInjectableD) {
        this.nonCdiOrEjbInjectable = nonCdiOrEjbInjectable;
        this.fieldOfInjectableD = fieldOfInjectableD;
    }

    public void manipulateField() {
        // Illegal modification
        fieldOfInjectableB = null;
    }

    public void accessFieldStatically() {
        // Illegal access
        fieldOfInjectableC = null;
    }

    public void manipulateNonCdiOrEjb() {
        // Legal modification
        nonCdiOrEjbInjectable = null;
    }

    public void injectableInstantiation() {
        // Illegal instantiation
        JavaxInjectableB javaxInjectableB = new JavaxInjectableB();
    }

    public void nonCdiOrEjbInjectableInstantiation() {
        // Legal instantiation
        JavaxNonCdiOrEjbInjectable javaxNonCdiOrEjbInjectable = new JavaxNonCdiOrEjbInjectable();
    }
}
