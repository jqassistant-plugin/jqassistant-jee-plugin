package org.jqassistant.plugin.jee.transaction.set;


import jakarta.transaction.Transactional;

@Transactional
public class JakartaTransactionalClassWithAdditionalConfigurationAndNestedClass {

    class InnerClass {
        void methodCallingOuterMethodWithAdditionalConfiguration() {
            JakartaTransactionalClassWithAdditionalConfigurationAndNestedClass.this.methodWithAdditionalConfigurationAttribute();
        }

        void methodCallingOuterMethodWithoutAdditionalConfiguration() {
            JakartaTransactionalClassWithAdditionalConfigurationAndNestedClass.this.methodWithoutAdditionalConfigurationAttribute();
        }
    }

    @Transactional(rollbackOn = Exception.class)
    void methodWithAdditionalConfigurationAttribute (){
    }

    private void methodWithoutAdditionalConfigurationAttribute() {
    }
}
