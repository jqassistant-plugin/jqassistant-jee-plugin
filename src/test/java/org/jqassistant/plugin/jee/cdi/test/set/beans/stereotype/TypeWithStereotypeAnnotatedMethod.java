package org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

public class TypeWithStereotypeAnnotatedMethod {

    @CustomStereotype
    ProducedBean produceBean() {
        return new ProducedBean();
    }

}
