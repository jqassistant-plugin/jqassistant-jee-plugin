package org.jqassistant.plugin.jee.cdi.test.set.beans.scope;

import javax.enterprise.context.RequestScoped;

public class TypeWithRequestScopedMethod {

    @RequestScoped
    ProducedBean produceBean() {
        return new ProducedBean();
    }

}
