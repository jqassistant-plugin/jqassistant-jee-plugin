package org.jqassistant.plugin.jee.injection.test.set.javax;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

public class JavaxBeanProducerWithConstraintViolations {

    @Inject
    private Object injectionPointField;

    @Inject
    private JavaxLocalEjb ejb;

    @Inject
    public void test() {}

    @Produces
    public JavaxInjectableA beanProducerA(){
        return new JavaxInjectableA();
    }

    @Produces
    public JavaxInjectableB beanProducerB(){
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
