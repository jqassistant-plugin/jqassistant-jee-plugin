package org.jqassistant.plugin.jee.cdi.test.set.beans.inject;

import javax.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.Bean;

/**
 * Example bean using constructor injection.
 *
 * @author Aparna Chaudhary
 */
public class BeanWithConstructorInjection {

    private Bean bean;

    @Inject
    public BeanWithConstructorInjection(Bean bean) {
        this.bean = bean;
    }

    public void performTask() {
        bean.doSomething();
    }


}
