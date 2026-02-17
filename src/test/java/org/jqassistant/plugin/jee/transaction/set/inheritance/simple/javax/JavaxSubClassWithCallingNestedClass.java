package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax;

public class JavaxSubClassWithCallingNestedClass extends JavaxSimpleTransactionalClass {

    class InnerClass {
        void callingTransactional() {
            JavaxSubClassWithCallingNestedClass.super.method();
        }
    }

}
