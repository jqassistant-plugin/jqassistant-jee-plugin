package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.javax;

import javax.enterprise.context.Dependent;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

public class JavaxTypeWithDependentMethod {

    @Dependent
    ProducedBean produceBean() {
        return new ProducedBean();
    }

}
