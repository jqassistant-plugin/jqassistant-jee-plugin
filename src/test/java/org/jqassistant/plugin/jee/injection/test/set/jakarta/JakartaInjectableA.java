package org.jqassistant.plugin.jee.injection.test.set.jakarta;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;

public class JakartaInjectableA {

    private JakartaInjectableB fieldOfInjectable1;

    private final JakartaInjectableD fieldOfInjectable2;

    private static JakartaInjectableC fieldOfInjectable3;

    @Resource
    private JakartaApplicationResource resource;

    @Inject
    public JakartaInjectableA(JakartaInjectableB fieldOfInjectable1, JakartaInjectableD fieldOfInjectable2) {
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
        JakartaInjectableB jakartaInjectableB = new JakartaInjectableB();
    }
}
