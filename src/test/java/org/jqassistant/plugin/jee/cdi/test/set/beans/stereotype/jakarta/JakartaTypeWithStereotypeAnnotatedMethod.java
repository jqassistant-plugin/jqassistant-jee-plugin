package org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype.jakarta;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

public class JakartaTypeWithStereotypeAnnotatedMethod {

    @JakartaCustomStereotype
    ProducedBean produceBean() {
        return new ProducedBean();
    }

}
