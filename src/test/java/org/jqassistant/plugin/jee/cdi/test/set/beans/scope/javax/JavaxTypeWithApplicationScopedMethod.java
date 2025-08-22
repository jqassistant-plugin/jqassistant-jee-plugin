package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.javax;

import javax.enterprise.context.ApplicationScoped;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

public class JavaxTypeWithApplicationScopedMethod {

    @ApplicationScoped
    ProducedBean produceBean() {
        return new ProducedBean();
    }

}
