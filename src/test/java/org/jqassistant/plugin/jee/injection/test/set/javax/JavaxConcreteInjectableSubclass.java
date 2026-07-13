package org.jqassistant.plugin.jee.injection.test.set.javax;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JavaxConcreteInjectableSubclass extends JavaxAbstractClassHoldingInjectable {

    public JavaxConcreteInjectableSubclass(JavaxInjectableA injectableA) {
        super(injectableA);
    }
}
