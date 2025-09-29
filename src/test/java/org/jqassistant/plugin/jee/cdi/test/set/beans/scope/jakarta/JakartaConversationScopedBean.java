package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.jakarta;

import java.io.Serializable;

import jakarta.enterprise.context.ConversationScoped;
import jakarta.enterprise.inject.Produces;
import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

@ConversationScoped
public class JakartaConversationScopedBean implements Serializable {

    @Produces
    @ConversationScoped
    private ProducedBean producerField;

    @Produces
    @ConversationScoped
    public ProducedBean producerMethod() {
        return new ProducedBean();
    }
}
