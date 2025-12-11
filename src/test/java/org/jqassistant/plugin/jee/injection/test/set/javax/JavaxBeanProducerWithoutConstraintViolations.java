package org.jqassistant.plugin.jee.injection.test.set.javax;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class JavaxBeanProducerWithoutConstraintViolations {

    @Produces
    public JavaxInjectableA beanProducerA(JavaxInjectableB injectableB, JavaxInjectableD injectableD){
        return new JavaxInjectableA(injectableB, injectableD);
    }

    @Produces
    public JavaxInjectableB beanProducerB(){
        return instantiate();
    }

    private JavaxInjectableB instantiate(){
        return new JavaxInjectableB();
    }

}
