package org.jqassistant.plugin.jee.cdi.test.set.beans.scope;

import javax.enterprise.context.ApplicationScoped;

public class TypeWithApplicationScopedMethod {

    @ApplicationScoped
    ProducedBean produceBean() {
        return new ProducedBean();
    }

}
