package org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype.javax;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

public class JavaxTypeWithStereotypeAnnotatedMethod {

    @JavaxCustomStereotype
    ProducedBean produceBean() {
        return new ProducedBean();
    }

}
