package org.jqassistant.plugin.jee.injection.test.set.jakarta.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class JakartaPersistenceConfiguration {

    @Produces
    @PersistenceContext
    private JakartaEntityManagerWithMethodProducer entityManagerWithFieldProducer;

    @PersistenceContext
    private JakartaEntityManagerWithMethodProducer entityManagerWithMethodProducer;

    @Produces
    JakartaEntityManagerWithMethodProducer produceEntityManager() {
        return entityManagerWithMethodProducer;
    }
}
