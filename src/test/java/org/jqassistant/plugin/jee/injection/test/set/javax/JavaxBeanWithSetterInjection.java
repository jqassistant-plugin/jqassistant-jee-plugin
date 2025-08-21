package org.jqassistant.plugin.jee.injection.test.set.javax;

import javax.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.Bean;

/**
 * Example bean using setter injection.
 *
 * @author Aparna Chaudhary
 */
public class JavaxBeanWithSetterInjection {

    private Bean bean;

    @Inject
    public void setBean(Bean bean) {
        this.bean = bean;
    }

    public void performTask() {
        bean.doSomething();
    }

}
