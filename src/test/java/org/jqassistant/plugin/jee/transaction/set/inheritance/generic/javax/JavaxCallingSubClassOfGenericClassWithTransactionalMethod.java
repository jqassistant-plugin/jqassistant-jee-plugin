package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax;

import javax.transaction.Transactional;

@Transactional
public class JavaxCallingSubClassOfGenericClassWithTransactionalMethod extends JavaxGenericClassWithTransactionalMethod<Long> {

    public void anotherMethodWithRequiredSemantics() {
        methodWithRequiredSemantics(1L);
    }

    // This method always runs without a transaction. The REQUIRED semantic of methodWithOverriddenSemantics() would have no effect if called.
    @Transactional(Transactional.TxType.NEVER)
    public void callingMethodWithNeverSemantics() {
        methodWithOverriddenSemantics(1L);
    }

}
