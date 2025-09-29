package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.javax;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

@RequestScoped
public class JavaxRequestScopedBean {

    @Produces
    @RequestScoped
    private ProducedBean producerField;

    @Produces
    @RequestScoped
    public ProducedBean producerMethod() {
        return new ProducedBean();
    }
}
