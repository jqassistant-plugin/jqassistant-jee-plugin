package org.jqassistant.plugin.jee.cdi.test;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.jqassistant.plugin.jee.cdi.test.set.beans.decorator.DecoratorBean;
import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.plugin.java.test.matcher.FieldDescriptorMatcher.fieldDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for the decorator concepts.
 */
class DecoratorIT extends AbstractJavaPluginIT {

    /**
     * Verifies the concept "cdi:Decorator".
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void decorator() throws Exception {
        scanClasses(DecoratorBean.class);
        assertThat(applyConcept("decorator:Decorator").getStatus(), equalTo(Result.Status.SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (e:Decorator) RETURN e").getColumn("e"), hasItem(typeDescriptor(DecoratorBean.class)));
        assertThat(query("MATCH (e:Field:Decorator:Delegate) RETURN e").getColumn("e"), hasItem(fieldDescriptor(DecoratorBean.class, "delegate")));
        store.commitTransaction();
    }
}
