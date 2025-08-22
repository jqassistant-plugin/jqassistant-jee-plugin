package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.javax;

import javax.enterprise.context.RequestScoped;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

public class JavaxTypeWithRequestScopedField {

    @RequestScoped
    ProducedBean producedBean;

}
