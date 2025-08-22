package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.javax;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

@SessionScoped
public class JavaxSessionScopedBean implements Serializable {

    @Produces
    @SessionScoped
    private String producerField;

    @Produces
    @SessionScoped
    public String producerMethod() {
        return "value";
    }
}
