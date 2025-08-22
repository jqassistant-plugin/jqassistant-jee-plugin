package org.jqassistant.plugin.jee.cdi.test.set.beans.inject.javax;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.javax.JavaxBean;

public class JavaxDefaultBean {

    @Inject
    @Default
    private JavaxBean bean;

}
