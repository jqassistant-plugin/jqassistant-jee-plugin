package org.jqassistant.plugin.jee.cdi.test.set.beans.scope;

import javax.enterprise.context.Dependent;

public class TypeWithDependentField {

    @Dependent
    ProducedBean producedBean;

}
