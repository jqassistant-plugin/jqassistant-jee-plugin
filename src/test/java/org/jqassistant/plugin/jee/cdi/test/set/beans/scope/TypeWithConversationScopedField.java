package org.jqassistant.plugin.jee.cdi.test.set.beans.scope;

import javax.enterprise.context.ConversationScoped;

public class TypeWithConversationScopedField {

    @ConversationScoped
    ProducedBean producedBean;

}
