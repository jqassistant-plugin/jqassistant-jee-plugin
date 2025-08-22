package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.jakarta;

import java.io.Serializable;

import jakarta.enterprise.context.ConversationScoped;
import jakarta.enterprise.inject.Produces;

@ConversationScoped
public class JakartaConversationScopedBean implements Serializable {

    @Produces
    @ConversationScoped
    private String producerField;

    @Produces
    @ConversationScoped
    public String producerMethod() {
        return "value";
    }
}
