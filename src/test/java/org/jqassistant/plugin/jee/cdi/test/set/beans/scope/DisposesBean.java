package org.jqassistant.plugin.jee.cdi.test.set.beans.scope;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

public class DisposesBean implements Serializable {

    @Produces
    @ApplicationScoped
    public String string() {
        return "value";
    }

    public void dispose(@Disposes String value) {
    }
}
