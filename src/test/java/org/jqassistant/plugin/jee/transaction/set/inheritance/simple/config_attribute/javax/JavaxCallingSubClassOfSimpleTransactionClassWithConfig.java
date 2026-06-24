package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.config_attribute.javax;

import javax.transaction.Transactional;

public class JavaxCallingSubClassOfSimpleTransactionClassWithConfig extends JavaxSimpleTransactionalClassWithConfig {

    @Transactional
    public void transactionalMethodWithAdditionalConfiguration() {
        methodWithAdditionalConfigurationAttribute();
    }
}
