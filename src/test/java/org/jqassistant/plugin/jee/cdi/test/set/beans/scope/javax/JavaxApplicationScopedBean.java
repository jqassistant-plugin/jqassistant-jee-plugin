package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.javax;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class JavaxApplicationScopedBean implements Serializable {

    @Produces
    @ApplicationScoped
    private ProducedBean producerField;

    @Produces
    @ApplicationScoped
    public ProducedBean producerMethod() {
        return new ProducedBean();
    }

}
