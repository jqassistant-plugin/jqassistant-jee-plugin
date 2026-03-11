package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta;

import jakarta.transaction.Transactional;

public class JakartaSubClassWithCallingNestedClass extends JakartaSimpleTransactionalClass {
    class InnerClass {
        void callingTransactional() {
            JakartaSubClassWithCallingNestedClass.super.methodWithRequiredSemantics();
        }

        // This method always runs without a transaction. The REQUIRED semantics of methodWithOverriddenSemantics() would have no effect if called.
        @Transactional(Transactional.TxType.NEVER)
        void transactionalMethodWithNeverSemantics() {
            JakartaSubClassWithCallingNestedClass.super.methodWithRequiredSemantics();
        }
    }
}
