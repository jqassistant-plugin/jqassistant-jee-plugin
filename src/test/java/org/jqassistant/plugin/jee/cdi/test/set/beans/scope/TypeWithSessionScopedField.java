package org.jqassistant.plugin.jee.cdi.test.set.beans.scope;

import javax.enterprise.context.SessionScoped;

public class TypeWithSessionScopedField {

    @SessionScoped
    ProducedBean producedBean;

}
