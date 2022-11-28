package org.jqassistant.plugin.jee.cdi.test.set.beans.qualifier;

import javax.inject.Named;

@Named
public class NamedBean {

    @Named
    public String getValue() {
        return "value";
    }
}
