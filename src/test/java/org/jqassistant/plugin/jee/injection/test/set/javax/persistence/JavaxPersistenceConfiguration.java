package org.jqassistant.plugin.jee.injection.test.set.javax.persistence;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class JavaxPersistenceConfiguration {

    @Produces
    @PersistenceContext
    private JavaxEntityManagerWithMethodProducer entityManagerWithFieldProducer;

    @PersistenceContext
    private JavaxEntityManagerWithMethodProducer entityManagerWithMethodProducer;

    @Produces
    JavaxEntityManagerWithMethodProducer produceEntityManager() {
        return entityManagerWithMethodProducer;
    }
}
