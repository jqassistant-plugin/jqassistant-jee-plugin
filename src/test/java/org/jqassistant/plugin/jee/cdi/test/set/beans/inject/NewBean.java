package org.jqassistant.plugin.jee.cdi.test.set.beans.inject;

import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.Bean;

public class NewBean {

    @Inject
    @New
    private Bean bean;

}
