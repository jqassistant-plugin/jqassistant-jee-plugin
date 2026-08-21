package org.jqassistant.plugin.jee.injection.test.set.jakarta;

public class JakartaConcreteSubclassWithInheritedInjectable extends JakartaAbstractClassHoldingInjectable {

    public JakartaConcreteSubclassWithInheritedInjectable(JakartaInjectableA jakartaInjectableA) {
        super(jakartaInjectableA);
    }
}
