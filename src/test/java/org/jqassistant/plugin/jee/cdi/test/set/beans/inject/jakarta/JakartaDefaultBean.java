package org.jqassistant.plugin.jee.cdi.test.set.beans.inject.jakarta;

import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;

import org.jqassistant.plugin.jee.cdi.test.set.beans.jakarta.JakartaBean;

public class JakartaDefaultBean {

    @Inject
    @Default
    private JakartaBean bean;

}
