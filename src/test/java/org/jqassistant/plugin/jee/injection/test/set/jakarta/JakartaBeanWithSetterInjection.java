package org.jqassistant.plugin.jee.injection.test.set.jakarta;

import jakarta.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.jakarta.JakartaBean;

/**
 * Example bean using setter injection.
 *
 * @author Aparna Chaudhary
 */
public class JakartaBeanWithSetterInjection {

    private JakartaBean jakartaBean;

    @Inject
    public void setBean(JakartaBean jakartaBean) {
        this.jakartaBean = jakartaBean;
    }

    public void performTask() {
        jakartaBean.doSomething();
    }

}
