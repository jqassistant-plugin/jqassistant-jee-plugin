package org.jqassistant.plugin.jee.cdi.test.set.beans.specializes.jakarta;

import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.Specializes;

import org.jqassistant.plugin.jee.cdi.test.set.beans.javax.JavaxBean;

@Specializes
public class JakartaSpecializesJakartaBean extends JavaxBean {

    @Produces
    @Specializes
    @Override
    public String doSomething() {
        return super.doSomething();
    }
}
