package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.jakarta;

import java.io.Serializable;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

@Dependent
public class JakartaDependentBean implements Serializable {

    @Produces
    @Dependent
    private ProducedBean producerField;

    @Produces
    @Dependent
    public ProducedBean producerMethod() {
        return new ProducedBean();
    }
}
