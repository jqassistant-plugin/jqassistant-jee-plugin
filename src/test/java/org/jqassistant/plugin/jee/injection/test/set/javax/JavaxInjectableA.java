package org.jqassistant.plugin.jee.injection.test.set.javax;

public class JavaxInjectableA {

    private JavaxInjectableB fieldOfInjectable1;

    private static JavaxInjectableB fieldOfInjectable2;

    public JavaxInjectableA(){}

    public void manipulateField() {
        // Illegal modification
        fieldOfInjectable1 = null;
    }

    public void accessFieldStatically() {
        // Illegal access
        fieldOfInjectable2 = null;
    }

    public void injectableInstantiation() {
        JavaxInjectableB javaxInjectableB = new JavaxInjectableB();
    }
}
