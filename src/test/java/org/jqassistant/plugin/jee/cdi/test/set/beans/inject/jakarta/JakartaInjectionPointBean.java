package org.jqassistant.plugin.jee.cdi.test.set.beans.inject.jakarta;

import jakarta.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.jakarta.JakartaBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.qualifier.jakarta.JakartaCustomQualifier;

public class JakartaInjectionPointBean {

    @Inject
    private JakartaBean bean;

    @JakartaCustomQualifier(bindingValue = "1", nonBindingValue = "2")
    @Inject
    JakartaBean qualifiedJavaxBean;

    @Inject
    JakartaInjectionPointBean(JakartaBean bean, @JakartaCustomQualifier(bindingValue = "1", nonBindingValue = "2") JakartaBean qualifiedJakartaBean) {
    }
}
