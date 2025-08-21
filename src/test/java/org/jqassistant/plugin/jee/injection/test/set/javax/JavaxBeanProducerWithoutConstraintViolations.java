package org.jqassistant.plugin.jee.injection.test.set.javax;

import javax.enterprise.inject.Produces;

public class JavaxBeanProducerWithoutConstraintViolations {

    @Produces
    public JavaxInjectableA beanProducerA(){
        return new JavaxInjectableA();
    }

}
