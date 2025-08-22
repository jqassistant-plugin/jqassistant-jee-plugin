package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.javax;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@Dependent
public class JavaxDependentBean implements Serializable {

    @Produces
    @Dependent
    private String producerField;

    @Produces
    @Dependent
    public String producerMethod() {
        return "value";
    }
}
