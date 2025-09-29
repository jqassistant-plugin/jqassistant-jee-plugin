package org.jqassistant.plugin.jee.cdi.test.set.beans.qualifier.jakarta;

import jakarta.inject.Named;

@Named
public class JakartaNamedBean {

    @Named
    public String getValue() {
        return "value";
    }
}
