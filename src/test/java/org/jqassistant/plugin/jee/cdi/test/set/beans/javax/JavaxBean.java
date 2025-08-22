package org.jqassistant.plugin.jee.cdi.test.set.beans.javax;

import javax.enterprise.inject.Produces;

public class JavaxBean {

    @Produces
    public String doSomething() {
        return "value";
    }
}
