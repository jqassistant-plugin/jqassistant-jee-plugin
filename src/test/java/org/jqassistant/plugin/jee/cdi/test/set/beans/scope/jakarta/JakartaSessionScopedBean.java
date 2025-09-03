package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.jakarta;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Produces;
import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

@SessionScoped
public class JakartaSessionScopedBean implements Serializable {

    @Produces
    @SessionScoped
    private ProducedBean producerField;

    @Produces
    @SessionScoped
    public ProducedBean producerMethod() {
        return new ProducedBean();
    }
}
