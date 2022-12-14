package org.jqassistant.plugin.jee.cdi.test.set.beans.inject;

import javax.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.Bean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.qualifier.CustomQualifier;

public class InjectionPointBean {

    @Inject
    private Bean bean;

    @CustomQualifier(bindingValue = "1", nonBindingValue = "2")
    @Inject
    Bean qualifiedBean;

    @Inject
    InjectionPointBean(Bean bean, @CustomQualifier(bindingValue = "1", nonBindingValue = "2") Bean qualifiedBean) {
    }
}
