package org.jqassistant.plugin.jee.injection.test.set.javax;

public abstract class JavaxAbstractClassHoldingInjectable {
    private final JavaxInjectableA injectableA;

    protected JavaxAbstractClassHoldingInjectable(JavaxInjectableA injectableA) {
        this.injectableA = injectableA;
    }
}
