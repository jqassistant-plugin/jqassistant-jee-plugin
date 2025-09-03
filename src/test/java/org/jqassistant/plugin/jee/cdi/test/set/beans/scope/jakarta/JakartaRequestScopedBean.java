package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.jakarta;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

@RequestScoped
public class JakartaRequestScopedBean {

    @Produces
    @RequestScoped
    private ProducedBean producerField;

    @Produces
    @RequestScoped
    public ProducedBean producerMethod() {
        return new ProducedBean();
    }
}
