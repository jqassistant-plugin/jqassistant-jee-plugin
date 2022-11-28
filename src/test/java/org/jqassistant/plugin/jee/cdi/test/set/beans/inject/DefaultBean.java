package org.jqassistant.plugin.jee.cdi.test.set.beans.inject;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.Bean;

public class DefaultBean {

    @Inject
    @Default
    private Bean bean;

}
