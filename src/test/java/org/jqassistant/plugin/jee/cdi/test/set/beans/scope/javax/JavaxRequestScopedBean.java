package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.javax;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

@RequestScoped
public class JavaxRequestScopedBean {

    @Produces
    @RequestScoped
    private String producerField;

    @Produces
    @RequestScoped
    public String producerMethod() {
        return "value";
    }
}
