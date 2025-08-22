package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.jakarta;

import jakarta.enterprise.context.SessionScoped;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

public class JakartaTypeWithSessionScopedField {

    @SessionScoped
    ProducedBean producedBean;

}
