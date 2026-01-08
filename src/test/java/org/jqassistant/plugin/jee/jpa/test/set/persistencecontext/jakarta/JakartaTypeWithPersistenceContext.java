package org.jqassistant.plugin.jee.jpa.test.set.persistencecontext.jakarta;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class JakartaTypeWithPersistenceContext {
    @PersistenceContext
    private EntityManager entityManager;
}
