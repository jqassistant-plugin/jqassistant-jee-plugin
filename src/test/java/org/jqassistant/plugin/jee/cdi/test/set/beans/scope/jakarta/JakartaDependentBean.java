package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.jakarta;

import java.io.Serializable;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;

@Dependent
public class JakartaDependentBean implements Serializable {

    @Produces
    @Dependent
    private String producerField;

    @Produces
    @Dependent
    public String producerMethod() {
        return "value";
    }
}
