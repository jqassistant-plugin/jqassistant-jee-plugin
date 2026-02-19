package org.jqassistant.plugin.jee.injection.test.set.javax;

public class JavaxInjectableB {

    private static JavaxNonCdiInjectable nonCdiInjectable;

    public JavaxInjectableB(){}

    public void accessFieldStatically() {
        nonCdiInjectable = null;
    }
}
