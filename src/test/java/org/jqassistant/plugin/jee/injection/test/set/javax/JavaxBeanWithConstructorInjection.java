package org.jqassistant.plugin.jee.injection.test.set.javax;

import javax.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.javax.JavaxBean;

/**
 * Example bean using constructor injection.
 *
 * @author Aparna Chaudhary
 */
public class JavaxBeanWithConstructorInjection {

    private JavaxBean javaxBean;

    @Inject
    public JavaxBeanWithConstructorInjection(JavaxBean javaxBean) {
        this.javaxBean = javaxBean;
    }

    public void performTask() {
        javaxBean.doSomething();
    }


}
