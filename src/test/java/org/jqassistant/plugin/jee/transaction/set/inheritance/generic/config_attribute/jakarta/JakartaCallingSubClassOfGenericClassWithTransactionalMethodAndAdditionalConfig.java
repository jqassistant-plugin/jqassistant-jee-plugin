package org.jqassistant.plugin.jee.transaction.set.inheritance.generic.config_attribute.jakarta;

import jakarta.transaction.Transactional;


public class JakartaCallingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig extends JakartaGenericClassWithTransactionalMethodAndAdditionalConfig<Long> {

    @Transactional
    public void anotherMethodWithRequiredSemantics() {
        methodWithAdditionalConfigurationAttribute(1L);
    }
}
