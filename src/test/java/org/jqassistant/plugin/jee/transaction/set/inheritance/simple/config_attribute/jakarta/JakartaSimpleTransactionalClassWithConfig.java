package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.config_attribute.jakarta;

import jakarta.transaction.Transactional;

public class JakartaSimpleTransactionalClassWithConfig {


    @Transactional(rollbackOn =  Exception.class)
    public void methodWithAdditionalConfigurationAttribute() {}

    @Transactional(rollbackOn = Exception.class)
    public void methodWithOverriddenConfigurationAttribute() {}
}
