package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta;

public class JakartaSubClassWithCallingNestedClass extends JakartaSimpleTransactionalClass {
    class InnerClass {
        void callingTransactional() {
            JakartaSubClassWithCallingNestedClass.super.method();
        }
    }
}
