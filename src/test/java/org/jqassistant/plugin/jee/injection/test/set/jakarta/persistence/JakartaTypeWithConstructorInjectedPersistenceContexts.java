package org.jqassistant.plugin.jee.injection.test.set.jakarta.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JakartaTypeWithConstructorInjectedPersistenceContexts {

    private final JakartaEntityManagerWithFieldProducer entityManagerWithFieldProducer;
    private final JakartaEntityManagerWithMethodProducer entityManagerWithMethodProducer;

    @Inject
    public JakartaTypeWithConstructorInjectedPersistenceContexts(JakartaEntityManagerWithFieldProducer entityManagerWithFieldProducer, JakartaEntityManagerWithMethodProducer entityManagerWithMethodProducer) {
        this.entityManagerWithFieldProducer = entityManagerWithFieldProducer;
        this.entityManagerWithMethodProducer = entityManagerWithMethodProducer;
    }

}
