package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.config_attribute.javax;

import javax.transaction.Transactional;

public class JavaxOverridingSubClassOfTransactionalClassWithTransactionalMethodAndAdditionalConfig extends JavaxGenericClassWithTransactionalMethodAndAdditionalConfig<Long> {

    @Override
    @Transactional
    public void methodWithOverriddenConfigurationAttribute(Long l) {
        super.methodWithOverriddenConfigurationAttribute(l);
    }
}
