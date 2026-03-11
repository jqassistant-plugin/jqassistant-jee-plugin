package org.jqassistant.plugin.jee.transaction.set;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaTransactionalClassWithNestedClass {

    class InnerClass {
        void callingTransactional() {
            JakartaTransactionalClassWithNestedClass.this.transactionalMethodWithRequiredSemantics();
        }

        void callingPrivateMethod() {
            JakartaTransactionalClassWithNestedClass.this.privateMethod();
        }

        // This method always runs without a transaction. The REQUIRED semantics of transactionalMethodWithRequiredSemantics() would have no effect if called.
        @Transactional(Transactional.TxType.NEVER)
        void transactionalMethodWithNeverSemantics() {
            JakartaTransactionalClassWithNestedClass.this.transactionalMethodWithRequiredSemantics();
        }
    }

    void transactionalMethodWithRequiredSemantics(){
    }

    private void privateMethod() {
    }

}
