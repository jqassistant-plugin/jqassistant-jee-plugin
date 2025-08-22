package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.javax;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class JavaxApplicationScopedBean implements Serializable {

    @Produces
    @ApplicationScoped
    private String producerField;

    @Produces
    @ApplicationScoped
    public String producerMethod() {
        return "value";
    }

}
