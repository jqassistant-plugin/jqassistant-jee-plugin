package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.jakarta;

import jakarta.enterprise.context.ConversationScoped;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

public class JakartaTypeWithConversationScopedMethod {

    @ConversationScoped
    ProducedBean produceBean() {
        return new ProducedBean();
    }

}
