package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.jakarta;

import jakarta.enterprise.context.Dependent;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

public class JakartaTypeWithDependentMethod {

    @Dependent
    ProducedBean produceBean() {
        return new ProducedBean();
    }

}
