package org.jqassistant.plugin.jee.injection.test.set.javax;

public class JavaxInjectableB {

    private static JavaxNonCdiOrEjbInjectable nonCdiOrEjbInjectable;

    public JavaxInjectableB(){}

    public void accessFieldStatically() {
        nonCdiOrEjbInjectable = null;
    }
}
