package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax;

import javax.transaction.Transactional;

public class JavaxSubClassWithCallingNestedClass extends JavaxSimpleTransactionalClass {

    class InnerClass {
        void callingTransactional() {
            JavaxSubClassWithCallingNestedClass.super.methodWithRequiredSemantics();
        }

        // This method always runs without a transaction. The REQUIRED semantics of methodWithOverriddenSemantics() would have no effect if called.
        @Transactional(Transactional.TxType.NEVER)
        void transactionalMethodWithNeverSemantics() {
            JavaxSubClassWithCallingNestedClass.super.methodWithRequiredSemantics();
        }
    }

}
