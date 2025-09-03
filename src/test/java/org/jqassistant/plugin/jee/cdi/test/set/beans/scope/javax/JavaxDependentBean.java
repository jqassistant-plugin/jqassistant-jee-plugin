package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.javax;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@Dependent
public class JavaxDependentBean implements Serializable {

    @Produces
    @Dependent
    private ProducedBean producerField;

    @Produces
    @Dependent
    public ProducedBean producerMethod() {
        return new ProducedBean();
    }
}
