package org.jqassistant.plugin.jee.injection.test.set.javax;

import javax.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.Bean;

/**
 * Example bean using constructor injection.
 *
 * @author Aparna Chaudhary
 */
public class JavaxBeanWithConstructorInjection {

    private Bean bean;

    @Inject
    public JavaxBeanWithConstructorInjection(Bean bean) {
        this.bean = bean;
    }

    public void performTask() {
        bean.doSomething();
    }


}
