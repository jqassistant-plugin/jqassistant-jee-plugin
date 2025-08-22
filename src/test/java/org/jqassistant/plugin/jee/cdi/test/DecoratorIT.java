package org.jqassistant.plugin.jee.cdi.test;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.jqassistant.plugin.jee.cdi.test.set.beans.decorator.jakarta.JakartaDecoratorBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.decorator.javax.JavaxDecoratorBean;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
    @ParameterizedTest
    @ValueSource(classes = { JavaxDecoratorBean.class, JakartaDecoratorBean.class})
    void decorator(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("decorator:Decorator").getStatus(), equalTo(Result.Status.SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (e:Decorator) RETURN e").getColumn("e"), hasItem(typeDescriptor(classToScan)));
        assertThat(query("MATCH (e:Field:Decorator:Delegate) RETURN e").getColumn("e"), hasItem(fieldDescriptor(classToScan, "delegate")));
        store.commitTransaction();
    }
}
