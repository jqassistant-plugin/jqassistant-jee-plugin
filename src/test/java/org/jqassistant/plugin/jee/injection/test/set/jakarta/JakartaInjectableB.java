package org.jqassistant.plugin.jee.injection.test.set.jakarta;

public class JakartaInjectableB {

    private static JakartaNonCdiInjectable nonCdiInjectable;

    public JakartaInjectableB(){}

    public void accessFieldStatically() {
        // Legal access
        nonCdiInjectable = null;
    }
}
