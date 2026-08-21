package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.config_attribute.jakarta;

import jakarta.transaction.Transactional;

public class JakartaCallingSubClassOfSimpleTransactionClassWithConfig extends JakartaSimpleTransactionalClassWithConfig {

    @Transactional
    public void transactionalMethodWithAdditionalConfiguration() {
        methodWithAdditionalConfigurationAttribute();
    }
}
