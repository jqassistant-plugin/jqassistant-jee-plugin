package org.jqassistant.plugin.jee.cdi.test.set.beans.inject.javax;

import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.javax.JavaxBean;

public class JavaxNewBean {

    @Inject
    @New
    private JavaxBean bean;

}
