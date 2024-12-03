package org.jqassistant.plugin.jee.injection.test.set;

import javax.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.Bean;

/**
 * Example bean using field injection.
 *
 * @author Aparna Chaudhary
 */
public class BeanWithFieldInjection {

    @Inject
    private Bean bean;

    public void performTask() {
        bean.doSomething();
    }

}
