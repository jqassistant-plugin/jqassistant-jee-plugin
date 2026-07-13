package org.jqassistant.plugin.jee.injection.test.set.javax;

public class JavaxConcreteSubclassWithInheritedInjectable extends JavaxAbstractClassHoldingInjectable {

    protected JavaxConcreteSubclassWithInheritedInjectable(JavaxInjectableA injectableA) {
        super(injectableA);
    }
}
