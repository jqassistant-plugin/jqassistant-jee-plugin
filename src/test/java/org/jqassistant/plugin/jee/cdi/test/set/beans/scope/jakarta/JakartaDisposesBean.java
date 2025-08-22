package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.jakarta;

import java.io.Serializable;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;

public class JakartaDisposesBean implements Serializable {

    @Produces
    @ApplicationScoped
    public String string() {
        return "value";
    }

    public void dispose(@Disposes String value) {
    }
}
