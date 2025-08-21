package org.jqassistant.plugin.jee.injection.test.set.jakarta;

import jakarta.enterprise.inject.Produces;

public class JakartaBeanProducerWithoutConstraintViolations {

    @Produces
    public JakartaInjectableA beanProducerA(){
        return new JakartaInjectableA();
    }

}
