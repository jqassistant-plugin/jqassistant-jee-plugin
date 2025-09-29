package org.jqassistant.plugin.jee.cdi.test.set.beans.scope.javax;

import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;

@ConversationScoped
public class JavaxConversationScopedBean implements Serializable {

    @Produces
    @ConversationScoped
    private ProducedBean producerField;

    @Produces
    @ConversationScoped
    public ProducedBean producerMethod() {
        return new ProducedBean();
    }
}
