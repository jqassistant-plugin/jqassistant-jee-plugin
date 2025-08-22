package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.jakarta;

import jakarta.enterprise.context.RequestScoped;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

public class JakartaTypeWithRequestScopedMethod {

    @RequestScoped
    ProducedBean produceBean() {
        return new ProducedBean();
    }

}
