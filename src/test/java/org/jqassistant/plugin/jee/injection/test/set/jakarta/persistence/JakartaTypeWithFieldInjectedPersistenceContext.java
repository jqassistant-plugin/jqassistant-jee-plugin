package org.jqassistant.plugin.jee.injection.test.set.jakarta.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class JakartaTypeWithFieldInjectedPersistenceContext {

    @PersistenceContext
    private JakartaEntityManagerWithoutProducer entityManagerWithoutProducer;

}
