package org.jqassistant.plugin.jee.injection.test.set;

public class InjectableA {

    private InjectableB fieldOfInjectable1;

    private static InjectableB fieldOfInjectable2;

    public InjectableA(){}

    public void manipulateField() {
        // Illegal modification
        fieldOfInjectable1 = null;
    }

    public void accessFieldStatically() {
        // Illegal access
        fieldOfInjectable2 = null;
    }

    public void injectableInstantiation() {
        InjectableB injectableB = new InjectableB();
    }
}
