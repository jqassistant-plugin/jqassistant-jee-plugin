package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.javax;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;

@ConversationScoped
public class JavaxConversationScopedBean implements Serializable {

    @Produces
    @ConversationScoped
    private String producerField;

    @Produces
    @ConversationScoped
    public String producerMethod() {
        return "value";
    }
}
