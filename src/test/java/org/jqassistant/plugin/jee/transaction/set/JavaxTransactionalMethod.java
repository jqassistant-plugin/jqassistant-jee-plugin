package org.jqassistant.plugin.jee.transaction.set;

import javax.transaction.Transactional;

public class JavaxTransactionalMethod {

    @Transactional
    public void transactionalMethodWithRequiredSemantics() {

    }

    public void nonTransactionalMethod() {
    }

    private void privateCallingTransactional() {
        transactionalMethodWithRequiredSemantics();
    }

    @Transactional
    private void privateMethod() {
    }

    private void callingPrivateMethod() {
        privateMethod(); // Private methods are not transactional and may be called.
    }

    // This method always runs without a transaction. The REQUIRED semantics of transactionalMethodWithRequiredSemantics() would have no effect if called.
    @Transactional(value = Transactional.TxType.NEVER)
    public void transactionalMethodWithNeverSemantics(){
        transactionalMethodWithRequiredSemantics();
    }

    @Transactional
    public void anotherTransactionalMethodWithRequiredSemantics(){
        transactionalMethodWithRequiredSemantics();
    }

    @Transactional
    public void requiredTransactionalCallingRequiredTransactionalTransitively() {
        privateCallingTransactional();
    }

    @Transactional(value = Transactional.TxType.NEVER)
    public void neverTransactionalCallingRequiredTransactionalTransitively() {
        privateCallingTransactional();
    }

}
