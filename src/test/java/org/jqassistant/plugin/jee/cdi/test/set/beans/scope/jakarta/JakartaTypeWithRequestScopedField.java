package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.jakarta;

import jakarta.enterprise.context.RequestScoped;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

public class JakartaTypeWithRequestScopedField {

    @RequestScoped
    ProducedBean producedBean;

}
