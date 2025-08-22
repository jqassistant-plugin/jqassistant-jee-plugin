package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.javax;

import javax.enterprise.context.SessionScoped;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

public class JavaxTypeWithSessionScopedField {

    @SessionScoped
    ProducedBean producedBean;

}
