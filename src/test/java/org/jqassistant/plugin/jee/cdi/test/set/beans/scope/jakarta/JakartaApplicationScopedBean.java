package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.jakarta;

import java.io.Serializable;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

@ApplicationScoped
public class JakartaApplicationScopedBean implements Serializable {

    @Produces
    @ApplicationScoped
    private ProducedBean producerField;

    @Produces
    @ApplicationScoped
    public ProducedBean producerMethod() {
        return new ProducedBean();
    }

}
