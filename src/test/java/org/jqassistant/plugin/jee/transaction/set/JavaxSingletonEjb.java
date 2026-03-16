package org.jqassistant.plugin.jee.transaction.set;

import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * A singleton EJB.
 */
@Singleton
public class JavaxSingletonEjb {

    public void transactionalMethodWithRequiredSemantics(){
    }

    private void privateCallingTransactional() {
        transactionalMethodWithRequiredSemantics();
    }

    private void privateMethod() {
    }

    private void callingPrivateMethod() {
        privateMethod(); // Private methods are not transactional and may be called.
    }

    // This method always runs without a transaction. The REQUIRED semantics of transactionalMethodWithRequiredSemantics() would have no effect if called.
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void transactionalMethodWithNeverSemantics(){
        transactionalMethodWithRequiredSemantics();
    }

    public void anotherTransactionalMethodWithRequiredSemantics(){
        transactionalMethodWithRequiredSemantics();
    }

    public void requiredTransactionalCallingRequiredTransactionalTransitively() {
        privateCallingTransactional();
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void neverTransactionalCallingRequiredTransactionalTransitively() {
        privateCallingTransactional();
    }

}
