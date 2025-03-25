package org.jqassistant.plugin.jee.cdi.test.set.beans.scope;

import javax.enterprise.context.ConversationScoped;

public class TypeWithConversationScopedMethod {

    @ConversationScoped
    ProducedBean produceBean() {
        return new ProducedBean();
    }

}
