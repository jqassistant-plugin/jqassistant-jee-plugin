package org.jqassistant.plugin.jee.jpa.test.set.persistencecontext.javax;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class JavaxTypeWithPersistenceContext {
    @PersistenceContext
    private EntityManager entityManager;
}
