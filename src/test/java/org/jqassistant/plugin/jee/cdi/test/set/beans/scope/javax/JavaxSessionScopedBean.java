package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.javax;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

@SessionScoped
public class JavaxSessionScopedBean implements Serializable {

    @Produces
    @SessionScoped
    private ProducedBean producerField;

    @Produces
    @SessionScoped
    public ProducedBean producerMethod() {
        return new ProducedBean();
    }
}
