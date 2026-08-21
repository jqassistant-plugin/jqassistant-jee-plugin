package org.jqassistant.plugin.jee.injection.test.set.jakarta;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JakartaConcreteInjectableSubclass extends JakartaAbstractClassHoldingInjectable {

    public JakartaConcreteInjectableSubclass(JakartaInjectableA jakartaInjectableA) {
        super(jakartaInjectableA);
    }
}
