package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaOverridingSubClassOfGenericClassWithTransactionalMethod extends JakartaGenericClassWithTransactionalMethod<Long> {

    @Override
    public void methodWithRequiredSemantics(Long l) {
        super.methodWithRequiredSemantics(l);
    }

    // This method always runs without a transaction. The REQUIRED semantics of methodWithOverriddenSemantics() would have no effect if called.
    // Transaction semantics is overridden.
    @Override
    @Transactional(Transactional.TxType.NEVER)
    public void methodWithOverriddenSemantics(Long l) {
        super.methodWithOverriddenSemantics(l);
    }

}
