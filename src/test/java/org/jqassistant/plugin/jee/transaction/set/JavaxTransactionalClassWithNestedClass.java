package org.jqassistant.plugin.jee.transaction.set;

import javax.transaction.Transactional;

@Transactional
public class JavaxTransactionalClassWithNestedClass {

    class InnerClass {
        void nonTransactionalMethod() {
            JavaxTransactionalClassWithNestedClass.this.transactionalMethodWithRequiredSemantics();
        }

        @Transactional
        void transactionalMethodWithRequiredSemantics() {
            JavaxTransactionalClassWithNestedClass.this.transactionalMethodWithRequiredSemantics();
        }

        void callingPrivateMethod() {
            JavaxTransactionalClassWithNestedClass.this.privateMethod();
        }

        // This method always runs without a transaction. The REQUIRED semantics of transactionalMethodWithRequiredSemantics() would have no effect if called.
        @Transactional(Transactional.TxType.NEVER)
        void transactionalMethodWithNeverSemantics() {
            JavaxTransactionalClassWithNestedClass.this.transactionalMethodWithRequiredSemantics();
        }
    }

    void transactionalMethodWithRequiredSemantics(){
    }

    private void privateMethod() {
    }

}
