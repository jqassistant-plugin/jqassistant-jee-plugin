package org.jqassistant.plugin.jee.cdi.test.set.beans.scope;

import javax.enterprise.context.ApplicationScoped;

public class TypeWithApplicationScopedField {

    @ApplicationScoped
    ProducedBean producedBean;

}
