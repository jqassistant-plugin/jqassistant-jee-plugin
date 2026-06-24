package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.config_attribute.javax;

import javax.transaction.Transactional;

public class JavaxGenericClassWithTransactionalMethodAndAdditionalConfig<T> {

    @Transactional(rollbackOn = Exception.class)
    public void methodWithAdditionalConfigurationAttribute(T parameter) {
    }

    @Transactional(rollbackOn = Exception.class)
    public void methodWithOverriddenConfigurationAttribute(T parameter) {
    }
}
