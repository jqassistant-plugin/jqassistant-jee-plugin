package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.config_attribute.jakarta;

import jakarta.transaction.Transactional;

public class JakartaOverridingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig extends JakartaGenericClassWithTransactionalMethodAndAdditionalConfig<Long> {

    @Override
    @Transactional
    public void methodWithOverriddenConfigurationAttribute(Long l) {
        super.methodWithOverriddenConfigurationAttribute(l);
    }
}
