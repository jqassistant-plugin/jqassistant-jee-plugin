package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta;


import jakarta.transaction.Transactional;

@Transactional
public class JakartaOverridingSubClassOfSimpleClassWithTransactionalMethod extends JakartaSimpleClassWithTransactionalMethod{

    @Override
    public void methodWithRequiredSemantics() {
        super.methodWithRequiredSemantics();
    }

    // This method always runs without a transaction. The REQUIRED semantics of methodWithOverriddenSemantics() would have no effect if called.
    // Transaction semantics is overridden.
    @Override
    @Transactional(Transactional.TxType.NEVER)
    public void methodWithOverriddenSemantics() {
        super.methodWithOverriddenSemantics();
    }

}
