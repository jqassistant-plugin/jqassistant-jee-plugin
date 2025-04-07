package org.jqassistant.plugin.jee.cdi.test;

import java.util.List;
import java.util.stream.Collectors;

import com.buschmais.jqassistant.core.report.api.model.Column;
import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Row;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.jqassistant.plugin.jee.cdi.test.set.beans.interceptor.CustomBinding;
import org.jqassistant.plugin.jee.cdi.test.set.beans.interceptor.CustomInterceptor;
import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.assertj.TypeDescriptorCondition.typeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the interceptor concepts.
 */
class InterceptorIT extends AbstractJavaPluginIT {

    /**
     * Verifies the concept "interceptor:Interceptor".
     */
    @Test
    void interceptor() throws Exception {
        scanClasses(CustomInterceptor.class);
        final Result<Concept> conceptResult = applyConcept("interceptor:Interceptor");
        assertThat(conceptResult.getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        final List<TypeDescriptor> conceptResultTypes = conceptResult.getRows().stream()
                .map(Row::getColumns)
                .map(columns -> columns.get("Interceptor"))
                .map(Column::getValue)
                .map(TypeDescriptor.class::cast)
                .collect(Collectors.toList());
        assertThat(conceptResultTypes).hasSize(1);
        assertThat(conceptResultTypes).haveExactly(1, typeDescriptor(CustomInterceptor.class));

        final List<TypeDescriptor> interceptorTypes =
                query("MATCH (interceptorType:Java:Type:Interceptor) RETURN interceptorType").getColumn("interceptorType");
        assertThat(interceptorTypes).hasSize(1);
        assertThat(interceptorTypes).haveExactly(1, typeDescriptor(CustomInterceptor.class));

        final List<TypeDescriptor> injectableTypes =
                query("MATCH (injectableType:Java:Type:Injectable) RETURN injectableType").getColumn("injectableType");
        assertThat(injectableTypes).hasSize(1);
        assertThat(injectableTypes).haveExactly(1, typeDescriptor(CustomInterceptor.class));

        store.commitTransaction();
    }

    @Test
    void providedConceptInjectable() throws Exception {
        scanClasses(CustomInterceptor.class);
        final Result<Concept> conceptResult = applyConcept("jee-injection:Injectable");
        assertThat(conceptResult.getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        final List<TypeDescriptor> conceptResultTypes = conceptResult.getRows().stream()
                .map(Row::getColumns)
                .map(columns -> columns.get("Injectable"))
                .map(Column::getValue)
                .map(TypeDescriptor.class::cast)
                .collect(Collectors.toList());
        assertThat(conceptResultTypes).hasSize(1);
        assertThat(conceptResultTypes).haveExactly(1, typeDescriptor(CustomInterceptor.class));

        final List<TypeDescriptor> injectableTypes =
                query("MATCH (injectableType:Java:Type:Injectable) RETURN injectableType").getColumn("injectableType");
        assertThat(injectableTypes).hasSize(1);
        assertThat(injectableTypes).haveExactly(1, typeDescriptor(CustomInterceptor.class));

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
        assertThat(applyConcept("interceptor:InterceptorBinding").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        final List<TypeDescriptor> column = query("MATCH (b:InterceptorBinding) RETURN b").getColumn("b");
        assertThat(column).haveExactly(1, typeDescriptor(CustomBinding.class));
        store.commitTransaction();
    }

}
