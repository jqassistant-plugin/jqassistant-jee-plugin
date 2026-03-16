package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax;

import javax.transaction.Transactional;

@Transactional
public class JavaxCallingSubClassOfSimpleClassWithTransactionalMethod extends JavaxSimpleClassWithTransactionalMethod {

    public void anotherMethodWithRequiredSemantics() {
        methodWithRequiredSemantics();
    }

    // This method always runs without a transaction. The REQUIRED semantic of methodWithRequiredSemantics() would have no effect if called.
    @Transactional(Transactional.TxType.NEVER)
    public void transactionalMethodWithNeverSemantics() {
        methodWithRequiredSemantics();
    }
}
