package org.jqassistant.plugin.jee.injection.test.set.javax;

import javax.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.javax.JavaxBean;

/**
 * Example bean using setter injection.
 *
 * @author Aparna Chaudhary
 */
public class JavaxBeanWithSetterInjection {

    private JavaxBean javaxBean;

    @Inject
    public void setBean(JavaxBean javaxBean) {
        this.javaxBean = javaxBean;
    }

    public void performTask() {
        javaxBean.doSomething();
    }

}
