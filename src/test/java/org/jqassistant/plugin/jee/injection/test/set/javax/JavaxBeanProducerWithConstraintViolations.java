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
    public JavaxInjectableA beanProducerA(JavaxInjectableB injectableB, JavaxInjectableD injectableD){
        return new JavaxInjectableA(injectableB, injectableD);
    }

    @Produces
    public JavaxInjectableB beanProducerB(){
        return new JavaxInjectableB();
    }

    @Produces
    public JavaxInjectableC beanProducerC(){
        return new JavaxInjectableC();
    }

    // illegal type
    @Produces
    public String beanProducerWithIllegalType(){
        return null;
    }

    public void beanProducerAccessor(){
        // Illegal direct access
        beanProducerB();
    }
}
