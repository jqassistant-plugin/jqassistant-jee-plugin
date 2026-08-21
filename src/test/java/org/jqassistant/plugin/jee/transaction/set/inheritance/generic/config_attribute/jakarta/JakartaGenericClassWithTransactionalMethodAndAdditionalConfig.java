package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.config_attribute.jakarta;

import jakarta.transaction.Transactional;

public class JakartaGenericClassWithTransactionalMethodAndAdditionalConfig<T> {

    @Transactional(rollbackOn =  Exception.class)
    public void methodWithAdditionalConfigurationAttribute(T parameter) {}

    @Transactional(rollbackOn = Exception.class)
    public void methodWithOverriddenConfigurationAttribute(T parameter) {}
}
