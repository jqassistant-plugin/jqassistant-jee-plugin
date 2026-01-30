package org.jqassistant.plugin.jee.injection.test.set.javax.persistence;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class JavaxTypeWithConstructorInjectedPersistenceContexts {

    private final JavaxEntityManagerWithFieldProducer entityManagerWithFieldProducer;
    private final JavaxEntityManagerWithMethodProducer entityManagerWithMethodProducer;

    @Inject
    public JavaxTypeWithConstructorInjectedPersistenceContexts(JavaxEntityManagerWithFieldProducer entityManagerWithFieldProducer, JavaxEntityManagerWithMethodProducer entityManagerWithMethodProducer) {
        this.entityManagerWithFieldProducer = entityManagerWithFieldProducer;
        this.entityManagerWithMethodProducer = entityManagerWithMethodProducer;
    }

}
