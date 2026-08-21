package org.jqassistant.plugin.jee.injection.test.set.jakarta;

public class JakartaConcreteSubclassWithOwnAndInheritedInjectable extends JakartaAbstractClassHoldingInjectable {
    private final JakartaInjectableA jakartaInjectableA;

    public JakartaConcreteSubclassWithOwnAndInheritedInjectable(JakartaInjectableA jakartaInjectableA) {
        super(jakartaInjectableA);
        this.jakartaInjectableA = jakartaInjectableA;
    }
}
