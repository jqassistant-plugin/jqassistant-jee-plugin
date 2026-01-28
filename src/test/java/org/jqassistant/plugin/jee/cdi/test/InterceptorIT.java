package org.jqassistant.plugin.jee.cdi.test;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.buschmais.jqassistant.core.report.api.model.Column;
import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Row;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.jqassistant.plugin.jee.cdi.test.set.beans.interceptor.jakarta.JakartaCustomBinding;
import org.jqassistant.plugin.jee.cdi.test.set.beans.interceptor.jakarta.JakartaCustomInterceptor;
import org.jqassistant.plugin.jee.cdi.test.set.beans.interceptor.jakarta.JakartaInactiveCustomInterceptor;
import org.jqassistant.plugin.jee.cdi.test.set.beans.interceptor.javax.JavaxCustomBinding;
import org.jqassistant.plugin.jee.cdi.test.set.beans.interceptor.javax.JavaxCustomInterceptor;
import org.jqassistant.plugin.jee.cdi.test.set.beans.interceptor.javax.JavaxInactiveCustomInterceptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.WARNING;
import static com.buschmais.jqassistant.plugin.java.test.assertj.TypeDescriptorCondition.typeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

/**
 * Tests for the interceptor concepts.
 */
class InterceptorIT extends AbstractJavaPluginIT {

    /**
     * Verifies the concept "interceptor:Interceptor".
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxCustomInterceptor.class, JavaxInactiveCustomInterceptor.class, JakartaCustomInterceptor.class, JakartaCustomInterceptor.class})
    void interceptor(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        final Result<Concept> conceptResult = applyConcept("interceptor:Interceptor");
        assertThat(conceptResult.getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        final List<TypeDescriptor> conceptResultTypes = conceptResult.getRows()
                .stream()
                .map(Row::getColumns)
                .map(columns -> columns.get("Interceptor"))
                .map(Column::getValue)
                .map(TypeDescriptor.class::cast)
                .collect(Collectors.toList());
        assertThat(conceptResultTypes).hasSize(1);
        assertThat(conceptResultTypes).haveExactly(1, typeDescriptor(classToScan));

        final List<TypeDescriptor> interceptorTypes =
                query("MATCH (type:Java:Type:Interceptor:Injectable) RETURN type").getColumn("type");
        assertThat(interceptorTypes).hasSize(1);
        assertThat(interceptorTypes).haveExactly(1, typeDescriptor(classToScan));

        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = {JavaxCustomInterceptor.class, JavaxInactiveCustomInterceptor.class, JakartaCustomInterceptor.class, JakartaInactiveCustomInterceptor.class})
    void providedConceptInjectable(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        final Result<Concept> conceptResult = applyConcept("jee-injection:Injectable");
        assertThat(conceptResult.getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        final List<TypeDescriptor> conceptResultTypes = conceptResult.getRows()
                .stream()
                .map(Row::getColumns)
                .map(columns -> columns.get("Injectable"))
                .map(Column::getValue)
                .map(TypeDescriptor.class::cast)
                .collect(Collectors.toList());
        assertThat(conceptResultTypes).hasSize(1);
        assertThat(conceptResultTypes).haveExactly(1, typeDescriptor(classToScan));

        final List<TypeDescriptor> injectableTypes = query("MATCH (injectableType:Java:Type:Injectable) RETURN injectableType").getColumn("injectableType");
        assertThat(injectableTypes).hasSize(1);
        assertThat(injectableTypes).haveExactly(1, typeDescriptor(classToScan));

        store.commitTransaction();
    }

    /**
     * Verifies the concept "interceptor:Binding".
     *
     * @throws java.io.IOException
     *         If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxCustomBinding.class, JakartaCustomBinding.class})
    void interceptorBinding(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("interceptor:InterceptorBinding").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        final List<TypeDescriptor> column = query("MATCH (b:JEE:InterceptorBinding) RETURN b").getColumn("b");
        assertThat(column).haveExactly(1, typeDescriptor(classToScan));
        store.commitTransaction();
    }

    static Stream<Arguments> activatedInterceptorParameters() {
        return Stream.of( //
                of("cdi/1_1", JavaxCustomInterceptor.class),
                of("cdi/5_0", JakartaCustomInterceptor.class)
        );
    }

    @ParameterizedTest
    @MethodSource("activatedInterceptorParameters")
    void activatedInterceptor(String beansXmlFilePath, Class<?> classToScan) throws Exception {
        scanClassPathDirectory(new File(getClassesDirectory(InterceptorIT.class), beansXmlFilePath));
        scanClasses(classToScan);
        final Result<Concept> conceptResult = applyConcept("interceptor:ActivatedInterceptor");
        assertThat(conceptResult.getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        final List<TypeDescriptor> conceptResultTypes = conceptResult.getRows()
                .stream()
                .map(Row::getColumns)
                .map(columns -> columns.get("Interceptor"))
                .map(Column::getValue)
                .map(TypeDescriptor.class::cast)
                .collect(Collectors.toList());
        assertThat(conceptResultTypes).hasSize(1);
        assertThat(conceptResultTypes).haveExactly(1, typeDescriptor(classToScan));

        final List<TypeDescriptor> activatedInterceptorTypes =
                query("MATCH (type:Java:Type:Interceptor:CDI:Injectable) RETURN type").getColumn("type");
        assertThat(activatedInterceptorTypes).hasSize(1);
        assertThat(activatedInterceptorTypes).haveExactly(1, typeDescriptor(classToScan));

        store.commitTransaction();
    }

    @Test
    void inactiveInterceptor() throws Exception {
        scanClassPathDirectory(new File(getClassesDirectory(InterceptorIT.class), "cdi/1_1"));
        scanClassPathDirectory(new File(getClassesDirectory(InterceptorIT.class), "cdi/5_0"));
        scanClasses(JavaxInactiveCustomInterceptor.class, JakartaInactiveCustomInterceptor.class);
        final Result<Concept> conceptResult = applyConcept("interceptor:ActivatedInterceptor");
        assertThat(conceptResult.getStatus()).isEqualTo(WARNING);
        store.beginTransaction();
        final List<TypeDescriptor> conceptResultTypes = conceptResult.getRows()
                .stream()
                .map(Row::getColumns)
                .map(columns -> columns.get("Interceptor"))
                .map(Column::getValue)
                .map(TypeDescriptor.class::cast)
                .collect(Collectors.toList());
        assertThat(conceptResultTypes).isEmpty();

        assertThat(query("MATCH (type:Java:Type:Interceptor:CDI:Injectable) RETURN type").getRows()).isEmpty();

        store.commitTransaction();
    }


}
