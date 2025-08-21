package org.jqassistant.plugin.jee.injection.test.set.jakarta;

public class JakartaInjectableA {

    private JakartaInjectableB fieldOfInjectable1;

    private static JakartaInjectableB fieldOfInjectable2;

    public JakartaInjectableA(){}

    public void manipulateField() {
        // Illegal modification
        fieldOfInjectable1 = null;
    }

    public void accessFieldStatically() {
        // Illegal access
        fieldOfInjectable2 = null;
    }

    public void injectableInstantiation() {
        JakartaInjectableB jakartaInjectableB = new JakartaInjectableB();
    }
}
