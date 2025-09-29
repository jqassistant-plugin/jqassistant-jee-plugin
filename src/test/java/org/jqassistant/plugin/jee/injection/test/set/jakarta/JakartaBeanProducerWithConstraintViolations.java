package org.jqassistant.plugin.jee.injection.test.set.jakarta;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

public class JakartaBeanProducerWithConstraintViolations {

    @Inject
    private Object injectionPointField;

    @Inject
    private JakartaLocalEjb ejb;

    @Inject
    public void test() {}

    @Produces
    public JakartaInjectableA beanProducerA(){
        return new JakartaInjectableA();
    }

    @Produces
    public JakartaInjectableB beanProducerB(){
        return null;
    }

    // illegal type
    @Produces
    public String beanProducerC(){
        return null;
    }

    public void beanProducerAccessor(){
        // Illegal direct access
        beanProducerA();
    }
}
