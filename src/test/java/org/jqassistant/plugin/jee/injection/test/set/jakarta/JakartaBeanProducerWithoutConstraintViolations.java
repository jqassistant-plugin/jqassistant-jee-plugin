package org.jqassistant.plugin.jee.injection.test.set.jakarta;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class JakartaBeanProducerWithoutConstraintViolations {

    @Produces
    public JakartaInjectableA beanProducerA(){
        return new JakartaInjectableA();
    }

    @Produces
    public JakartaInjectableB beanProducerB(){
        return instantiate();
    }

    private JakartaInjectableB instantiate() {
        return new JakartaInjectableB();
    }


}
