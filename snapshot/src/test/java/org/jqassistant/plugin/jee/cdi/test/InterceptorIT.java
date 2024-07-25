package org.jqassistant.plugin.jee.cdi.test;

import java.util.List;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.jqassistant.plugin.jee.cdi.test.set.beans.interceptor.CustomBinding;
import org.jqassistant.plugin.jee.cdi.test.set.beans.interceptor.CustomInterceptor;
import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for the interceptor concepts.
 */
class InterceptorIT extends AbstractJavaPluginIT {

    /**
     * Verifies the concept "cdi:Interceptor".
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void interceptor() throws Exception {
        scanClasses(CustomInterceptor.class);
        assertThat(applyConcept("interceptor:Interceptor").getStatus(), equalTo(Result.Status.SUCCESS));
        store.beginTransaction();
        List<Object> column = query("MATCH (i:Interceptor) RETURN i").getColumn("i");
        assertThat(column, hasItem(typeDescriptor(CustomInterceptor.class)));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "interceptor:Binding".
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void interceptorBinding() throws Exception {
        scanClasses(CustomBinding.class);
        assertThat(applyConcept("interceptor:Binding").getStatus(), equalTo(Result.Status.SUCCESS));
        store.beginTransaction();
        List<Object> column = query("MATCH (b:Interceptor:Binding) RETURN b").getColumn("b");
        assertThat(column, hasItem(typeDescriptor(CustomBinding.class)));
        store.commitTransaction();
    }

}
