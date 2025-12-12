package org.jqassistant.plugin.jee.injection.test.set.jakarta;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;

public class JakartaInjectableA {

    private JakartaInjectableB fieldOfInjectableB;

    private static JakartaInjectableC fieldOfInjectableC;

    private final JakartaInjectableD fieldOfInjectableD;

    @Resource
    private JakartaApplicationResource resource;

    @Inject
    public JakartaInjectableA(JakartaInjectableB injectableB, JakartaInjectableD injectableD) {
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
        JakartaInjectableB jakartaInjectableB = new JakartaInjectableB();
    }
}
