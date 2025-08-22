package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.jakarta;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Produces;

@SessionScoped
public class JakartaSessionScopedBean implements Serializable {

    @Produces
    @SessionScoped
    private String producerField;

    @Produces
    @SessionScoped
    public String producerMethod() {
        return "value";
    }
}
