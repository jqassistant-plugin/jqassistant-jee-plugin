package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.config_attribute.javax;

import javax.transaction.Transactional;

public class JavaxCallingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig extends JavaxGenericClassWithTransactionalMethodAndAdditionalConfig<Long> {

    @Transactional
    public void anotherMethodWithRequiredSemantics() {
        methodWithAdditionalConfigurationAttribute(1L);
    }
}
