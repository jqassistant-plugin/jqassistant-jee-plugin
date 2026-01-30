package org.jqassistant.plugin.jee.injection.test.set.javax.persistence;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class JavaxTypeWithFieldInjectedPersistenceContext {

    @PersistenceContext
    private JavaxEntityManagerWithoutProducer entityManagerWithoutProducer;

}
