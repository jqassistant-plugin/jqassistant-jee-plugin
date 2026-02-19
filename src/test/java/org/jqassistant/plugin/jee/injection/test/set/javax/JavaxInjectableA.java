package org.jqassistant.plugin.jee.injection.test.set.javax;

import javax.annotation.Resource;
import javax.inject.Inject;

public class JavaxInjectableA {

    private JavaxInjectableB fieldOfInjectableB;

    private JavaxNonCdiInjectable nonCdiInjectable;

    private static JavaxInjectableC fieldOfInjectableC;

    private final JavaxInjectableD fieldOfInjectableD;

    @Resource
    private JavaxApplicationResource resource;

    @Inject
    public JavaxInjectableA(JavaxInjectableB injectableB, JavaxInjectableD injectableD) {
        this.fieldOfInjectableB = injectableB;
        this.fieldOfInjectableD = injectableD;
    }

    public JavaxInjectableA(JavaxNonCdiInjectable nonCdiInjectable, JavaxInjectableD fieldOfInjectableD) {
        this.nonCdiInjectable = nonCdiInjectable;
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

    public void manipulateNonCdi() {
        // Legal modification
        nonCdiInjectable = null;
    }

    public void injectableInstantiation() {
        // Illegal instantiation
        JavaxInjectableB javaxInjectableB = new JavaxInjectableB();
    }

    public void nonCdiInjectableInstantiation() {
        // Legal instantiation
        JavaxNonCdiInjectable javaxNonCdiInjectable = new JavaxNonCdiInjectable();
    }
}
