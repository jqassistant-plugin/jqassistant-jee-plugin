package org.jqassistant.plugin.jee.jpa.test.set.entity.javax;

import javax.persistence.Embeddable;

@Embeddable
public class JavaxJpaEmbeddable {

    private int intValue;

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }
}
