package org.jqassistant.plugin.jee.cdi.test.set.beans.inject.javax;

import javax.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.javax.JavaxBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.qualifier.javax.JavaxCustomQualifier;

public class JavaxInjectionPointBean {

    @Inject
    private JavaxBean bean;

    @JavaxCustomQualifier(bindingValue = "1", nonBindingValue = "2")
    @Inject
    JavaxBean qualifiedJavaxBean;

    @Inject
    JavaxInjectionPointBean(JavaxBean bean, @JavaxCustomQualifier(bindingValue = "1", nonBindingValue = "2") JavaxBean qualifiedJavaxBean) {
    }
}
