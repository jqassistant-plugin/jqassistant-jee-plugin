package org.jqassistant.plugin.jee;

import com.buschmais.jqassistant.plugin.java.api.model.MethodDescriptor;
import org.assertj.core.api.Condition;

/**
 * A {@link Condition} of type {@link MethodDescriptor} that can be created using the method's signature without the
 * method being checked for existence. This can be useful to check nodes of inherited methods. This class should be part
 * of the jqassistant testing api.
 */
public class SimpleMethodDescriptorCondition extends Condition<MethodDescriptor> {
    private final String expectedTypeName;
    private final String expectedSignature;

    private SimpleMethodDescriptorCondition(String expectedTypeName, String expectedSignature) {
        super("inherited method '" + expectedTypeName + "#" + expectedSignature + "'");
        this.expectedTypeName = expectedTypeName;
        this.expectedSignature = expectedSignature;
    }

    public boolean matches(MethodDescriptor value) {
        return value.getSignature().equals(this.expectedSignature) && value.getDeclaringType().getFullQualifiedName().equals(this.expectedTypeName);
    }

    public static SimpleMethodDescriptorCondition simpleMethodDescriptor(Class<?> type, String signature) {
        return new SimpleMethodDescriptorCondition(type.getName(), signature);
    }

}
