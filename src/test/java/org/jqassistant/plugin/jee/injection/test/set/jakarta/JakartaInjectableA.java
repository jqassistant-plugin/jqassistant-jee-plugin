package org.jqassistant.plugin.jee.injection.test.set.jakarta;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;

public class JakartaInjectableA {

    private JakartaInjectableB fieldOfInjectableB;

    private JakartaNonCdiOrEjbInjectable nonCdiOrEjbInjectable;

    private static JakartaInjectableC fieldOfInjectableC;

    private final JakartaInjectableD fieldOfInjectableD;

    @Resource
    private JakartaApplicationResource resource;

    @Inject
    public JakartaInjectableA(JakartaInjectableB injectableB, JakartaInjectableD injectableD) {
        this.fieldOfInjectableB = injectableB;
        this.fieldOfInjectableD = injectableD;
    }

    public JakartaInjectableA(JakartaNonCdiOrEjbInjectable nonCdiOrEjbInjectable, JakartaInjectableD fieldOfInjectableD) {
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
        JakartaInjectableB jakartaInjectableB = new JakartaInjectableB();
    }

    public void nonCdiOrEjbInjectableInstantiation() {
        // Legal instantiation
        JakartaNonCdiOrEjbInjectable jakartaNonCdiOrEjbInjectable = new JakartaNonCdiOrEjbInjectable();
    }
}
