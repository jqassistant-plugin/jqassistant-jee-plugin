package org.jqassistant.plugin.jee.injection.test.set.jakarta;

import jakarta.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.jakarta.JakartaBean;

/**
 * Example bean using constructor injection.
 *
 * @author Aparna Chaudhary
 */
public class JakartaBeanWithConstructorInjection {

    private JakartaBean jakartaBean;

    @Inject
    public JakartaBeanWithConstructorInjection(JakartaBean jakartaBean) {
        this.jakartaBean = jakartaBean;
    }

    public void performTask() {
        jakartaBean.doSomething();
    }


}
