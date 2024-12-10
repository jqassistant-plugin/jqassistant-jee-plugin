package org.jqassistant.plugin.jee.injection.test.set;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

public class BeanProducer {

    @Inject
    private Object injectionPointField;

    @Inject
    private LocalEjb ejb;

    @Inject
    public void test() {}

    @Produces
    public InjectableA beanProducerA(){
        return new InjectableA();
    }

    @Produces
    public InjectableB beanProducerB(){
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
