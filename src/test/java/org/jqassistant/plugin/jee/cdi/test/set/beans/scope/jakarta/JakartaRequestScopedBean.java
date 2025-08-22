package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.jakarta;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;

@RequestScoped
public class JakartaRequestScopedBean {

    @Produces
    @RequestScoped
    private String producerField;

    @Produces
    @RequestScoped
    public String producerMethod() {
        return "value";
    }
}
