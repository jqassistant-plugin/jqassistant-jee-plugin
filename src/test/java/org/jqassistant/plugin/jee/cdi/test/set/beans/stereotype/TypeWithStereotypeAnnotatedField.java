package org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

public class TypeWithStereotypeAnnotatedField {

    @CustomStereotype
    ProducedBean producedBean;

}
