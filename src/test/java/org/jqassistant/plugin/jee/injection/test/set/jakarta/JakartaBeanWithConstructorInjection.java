package org.jqassistant.plugin.jee.injection.test.set.jakarta;

import jakarta.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.Bean;

/**
 * Example bean using constructor injection.
 *
 * @author Aparna Chaudhary
 */
public class JakartaBeanWithConstructorInjection {

    private Bean bean;

    @Inject
    public JakartaBeanWithConstructorInjection(Bean bean) {
        this.bean = bean;
    }

    public void performTask() {
        bean.doSomething();
    }


}
