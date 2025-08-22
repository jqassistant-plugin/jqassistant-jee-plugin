package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.jakarta;

import java.io.Serializable;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class JakartaApplicationScopedBean implements Serializable {

    @Produces
    @ApplicationScoped
    private String producerField;

    @Produces
    @ApplicationScoped
    public String producerMethod() {
        return "value";
    }

}
