package org.jqassistant.plugin.jee.transaction.set;

import jakarta.transaction.Transactional;

@Transactional
public class JakartaTransactionalClassWithNestedClass {

    class InnerClass {
        void callingTransactional() {
            JakartaTransactionalClassWithNestedClass.this.transactionalMethod();
        }

        void callingPrivateMethod() {
            JakartaTransactionalClassWithNestedClass.this.privateMethod();
        }
    }

    void transactionalMethod(){
    }

    private void privateMethod() {
    }

}
