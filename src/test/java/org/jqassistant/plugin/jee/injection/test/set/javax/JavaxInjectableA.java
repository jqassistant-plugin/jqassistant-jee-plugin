package org.jqassistant.plugin.jee.injection.test.set.javax;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

public class JavaxInjectableA {

    private final JavaxInjectableBFactory factory = new JavaxInjectableBFactory();

    private JavaxInjectableB fieldOfInjectableB;

    private JavaxNonCdiInjectable nonCdiInjectable;

    private static JavaxInjectableC fieldOfInjectableC;

    private final JavaxInjectableD fieldOfInjectableD;

    @Resource
    private JavaxApplicationResource resource;

    @Inject
    public JavaxInjectableA(JavaxInjectableB injectableB, JavaxInjectableD injectableD) {
        this.fieldOfInjectableB = injectableB;
        this.fieldOfInjectableD = injectableD;
    }

    public JavaxInjectableA(JavaxNonCdiInjectable nonCdiInjectable, JavaxInjectableD fieldOfInjectableD) {
        this.nonCdiInjectable = nonCdiInjectable;
        this.fieldOfInjectableD = fieldOfInjectableD;
    }

    public void manipulateField() {
        // Illegal modification
        fieldOfInjectableB = null;
    }

    @PostConstruct
    public void manipulateFieldViaPostConstruct() {
        // Illegal modification
        fieldOfInjectableB = null;
    }

    @PreDestroy
    public void manipulateFieldViaPreDestroy() {
        // Illegal modification
        fieldOfInjectableB = null;
    }

    public void accessFieldStatically() {
        // Illegal access
        fieldOfInjectableC = null;
    }

    public void manipulateNonCdi() {
        // Legal modification
        nonCdiInjectable = null;
    }

    public void injectableInstantiation() {
        // Illegal instantiation
        JavaxInjectableB javaxInjectableB = new JavaxInjectableB();
    }

    public void nonCdiInjectableInstantiation() {
        // Legal instantiation
        JavaxNonCdiInjectable javaxNonCdiInjectable = new JavaxNonCdiInjectable();
    }

    @Produces
    public JavaxInjectableB beanProducerUsingFactoryMethod() {
        return factory.createInjectableB();
    }

    public JavaxInjectableB someOtherMethodUsingFactoryMethod() {
        // Illegal factory call
        return factory.createInjectableB();
    }

}
