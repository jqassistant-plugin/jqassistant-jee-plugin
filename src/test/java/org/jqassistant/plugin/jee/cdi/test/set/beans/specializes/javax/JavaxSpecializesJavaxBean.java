package org.jqassistant.plugin.jee.cdi.test.set.beans.specializes.javax;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;

import org.jqassistant.plugin.jee.cdi.test.set.beans.javax.JavaxBean;

@Specializes
public class JavaxSpecializesJavaxBean extends JavaxBean {

    @Produces
    @Specializes
    @Override
    public String doSomething() {
        return super.doSomething();
    }
}
