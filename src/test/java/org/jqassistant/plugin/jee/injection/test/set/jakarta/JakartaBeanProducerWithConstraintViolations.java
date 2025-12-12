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
    public JakartaInjectableA beanProducerA(JakartaInjectableB injectableB, JakartaInjectableD injectableD){
        return new JakartaInjectableA(injectableB, injectableD);
    }

    @Produces
    public JakartaInjectableB beanProducerB(){
        return new JakartaInjectableB();
    }

    @Produces
    public JakartaInjectableC beanProducerC(){
        return new JakartaInjectableC();
    }

    @Produces
    public JakartaInjectableD beanProducerD(){
        return new JakartaInjectableD();
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
