package org.jqassistant.plugin.jee.cdi.test.set.beans.jakarta;

import jakarta.enterprise.inject.Produces;

public class JakartaBean {

    @Produces
    public String doSomething() {
        return "value";
    }
}
