package org.jqassistant.plugin.jee.injection.test.set.jakarta;

public class JakartaInjectableB {

    private static JakartaNonCdiOrEjbInjectable nonCdiOrEjbInjectable;

    public JakartaInjectableB(){}

    public void accessFieldStatically() {
        // Legal access
        nonCdiOrEjbInjectable = null;
    }
}
