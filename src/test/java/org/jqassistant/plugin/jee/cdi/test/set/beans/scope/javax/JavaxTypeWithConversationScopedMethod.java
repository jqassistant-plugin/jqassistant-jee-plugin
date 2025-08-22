package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.javax;

import javax.enterprise.context.ConversationScoped;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

public class JavaxTypeWithConversationScopedMethod {

    @ConversationScoped
    ProducedBean produceBean() {
        return new ProducedBean();
    }

}
