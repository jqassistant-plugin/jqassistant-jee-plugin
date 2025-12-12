package org.jqassistant.plugin.jee.injection.test.set.jakarta;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class JakartaBeanProducerWithoutConstraintViolations {

    @Produces
    public JakartaInjectableA beanProducerA(JakartaInjectableB injectableB, JakartaInjectableD injectableD){
        return new JakartaInjectableA(injectableB, injectableD);
    }

    @Produces
    public JakartaInjectableB beanProducerB(){
        return instantiate();
    }

    private JakartaInjectableB instantiate() {
        return new JakartaInjectableB();
    }


}
