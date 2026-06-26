package org.jqassistant.plugin.jee.transaction.set;

import javax.transaction.Transactional;

@Transactional
public class JavaxTransactionalClassWithAdditionalConfigurationAndNestedClass {

    class InnerClass {
        void methodCallingOuterMethodWithAdditionalConfiguration() {
            JavaxTransactionalClassWithAdditionalConfigurationAndNestedClass.this.methodWithAdditionalConfigurationAttribute();
        }

        void methodCallingOuterMethodWithoutAdditionalConfiguration() {
            JavaxTransactionalClassWithAdditionalConfigurationAndNestedClass.this.methodWithoutAdditionalConfigurationAttribute();
        }
    }

    @Transactional(rollbackOn = Exception.class)
    void methodWithAdditionalConfigurationAttribute (){
    }

    private void methodWithoutAdditionalConfigurationAttribute() {
    }
}
