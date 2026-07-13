package org.jqassistant.plugin.jee.injection.test.set.javax;

public class JavaxConcreteSubclassWithOwnAndInheritedInjectable extends JavaxAbstractClassHoldingInjectable {
    private final JavaxInjectableA injectableA;

    public JavaxConcreteSubclassWithOwnAndInheritedInjectable(JavaxInjectableA injectableA) {
        super(injectableA);
        this.injectableA = injectableA;
    }
}
