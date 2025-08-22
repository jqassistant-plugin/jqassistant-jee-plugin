package org.jqassistant.plugin.jee.cdi.test.set.beans.qualifier.javax;

import javax.inject.Named;

@Named
public class JavaxNamedBean {

    @Named
    public String getValue() {
        return "value";
    }
}
