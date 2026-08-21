package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.config_attribute.javax;

import javax.transaction.Transactional;

public class JavaxSimpleTransactionalClassWithConfig {

    @Transactional(rollbackOn =  Exception.class)
    public void methodWithAdditionalConfigurationAttribute() {}

    @Transactional(rollbackOn = Exception.class)
    public void methodWithOverriddenConfigurationAttribute() {}
}
