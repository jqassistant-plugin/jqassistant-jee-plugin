package org.jqassistant.plugin.jee.injection.test.set.jakarta;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;

public class JakartaInjectableA {

    private JakartaInjectableB fieldOfInjectableB;

    private JakartaNonCdiInjectable nonCdiInjectable;

    private static JakartaInjectableC fieldOfInjectableC;

    private final JakartaInjectableD fieldOfInjectableD;

    @Resource
    private JakartaApplicationResource resource;

    @Inject
    public JakartaInjectableA(JakartaInjectableB injectableB, JakartaInjectableD injectableD) {
        this.fieldOfInjectableB = injectableB;
        this.fieldOfInjectableD = injectableD;
    }

    public JakartaInjectableA(JakartaNonCdiInjectable nonCdiInjectable, JakartaInjectableD fieldOfInjectableD) {
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
        JakartaInjectableB jakartaInjectableB = new JakartaInjectableB();
    }

    public void nonCdiInjectableInstantiation() {
        // Legal instantiation
        JakartaNonCdiInjectable jakartaNonCdiInjectable = new JakartaNonCdiInjectable();
    }
}
