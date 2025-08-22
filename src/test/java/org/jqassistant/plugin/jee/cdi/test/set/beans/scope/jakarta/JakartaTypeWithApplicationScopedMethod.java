package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.jakarta;

import jakarta.enterprise.context.ApplicationScoped;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

public class JakartaTypeWithApplicationScopedMethod {

    @ApplicationScoped
    ProducedBean produceBean() {
        return new ProducedBean();
    }

}
