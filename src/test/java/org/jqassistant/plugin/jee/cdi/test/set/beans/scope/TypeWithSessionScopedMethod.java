package org.jqassistant.plugin.jee.cdi.test.set.beans.scope;

import javax.enterprise.context.SessionScoped;

public class TypeWithSessionScopedMethod {

    @SessionScoped
    ProducedBean produceBean() {
        return new ProducedBean();
    }

}
