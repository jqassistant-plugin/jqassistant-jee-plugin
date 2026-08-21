package org.jqassistant.plugin.jee.transaction.set.inheritance.simple.config_attribute.jakarta;

import jakarta.transaction.Transactional;

public class JakartaOverridingSubClassOfSimpleTransactionalClassWithConfig extends JakartaSimpleTransactionalClassWithConfig {

    @Override
    @Transactional
    public void methodWithOverriddenConfigurationAttribute() {
        super.methodWithOverriddenConfigurationAttribute();
    }
}
