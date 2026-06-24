package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.config_attribute.javax;

import javax.transaction.Transactional;

public class JavaxOverridingSubClassOfSimpleTransactionalClassWithConfig extends JavaxSimpleTransactionalClassWithConfig {

    @Override
    @Transactional
    public void methodWithOverriddenConfigurationAttribute() {
        super.methodWithOverriddenConfigurationAttribute();
    }
}
