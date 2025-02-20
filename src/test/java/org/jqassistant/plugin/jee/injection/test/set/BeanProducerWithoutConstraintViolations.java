package org.jqassistant.plugin.jee.injection.test.set;

import javax.enterprise.inject.Produces;

public class BeanProducerWithoutConstraintViolations {

    @Produces
    public InjectableA beanProducerA(){
        return new InjectableA();
    }

}
