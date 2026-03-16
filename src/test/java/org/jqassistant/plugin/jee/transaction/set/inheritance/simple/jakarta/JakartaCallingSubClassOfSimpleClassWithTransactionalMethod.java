package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaCallingSubClassOfSimpleClassWithTransactionalMethod extends JakartaSimpleClassWithTransactionalMethod {

    public void anotherMethodWithRequiredSemantics() {
        methodWithRequiredSemantics();
    }

    // This method always runs without a transaction. The REQUIRED semantic of methodWithRequiredSemantics() would have no effect if called.
    @Transactional(Transactional.TxType.NEVER)
    public void transactionalMethodWithNeverSemantics() {
        methodWithRequiredSemantics();
    }
}
