package org.jqassistant.plugin.jee.transaction.set;

import javax.transaction.Transactional;

@Transactional
public class JavaxTransactionalClassWithNestedClass {

    class InnerClass {
        void callingTransactional() {
            JavaxTransactionalClassWithNestedClass.this.transactionalMethod();
        }

        void callingPrivateMethod() {
            JavaxTransactionalClassWithNestedClass.this.privateMethod();
        }
    }

    void transactionalMethod(){
    }

    private void privateMethod() {
    }

}
