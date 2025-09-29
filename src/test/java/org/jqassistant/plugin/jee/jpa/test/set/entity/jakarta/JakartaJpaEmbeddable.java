package org.jqassistant.plugin.jee.jpa.test.set.entity.jakarta;

import jakarta.persistence.Embeddable;

@Embeddable
public class JakartaJpaEmbeddable {

    private int intValue;

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }
}
