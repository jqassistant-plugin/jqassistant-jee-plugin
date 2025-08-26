package org.jqassistant.plugin.jee.cdi.test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.buschmais.jqassistant.core.report.api.model.Column;
import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Row;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.plugin.java.api.model.FieldDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.MethodDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.jqassistant.plugin.jee.cdi.test.set.beans.alternative.jakarta.JakartaAlternativeBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.alternative.javax.JavaxAlternativeBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.decorator.jakarta.JakartaDecoratorBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.decorator.javax.JavaxDecoratorBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.inject.jakarta.JakartaDefaultBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.inject.javax.JavaxDefaultBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.inject.javax.JavaxNewBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.qualifier.jakarta.JakartaCustomQualifier;
import org.jqassistant.plugin.jee.cdi.test.set.beans.qualifier.jakarta.JakartaNamedBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.qualifier.javax.JavaxCustomQualifier;
import org.jqassistant.plugin.jee.cdi.test.set.beans.qualifier.javax.JavaxNamedBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.ProducedBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.jakarta.*;
import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.javax.*;
import org.jqassistant.plugin.jee.cdi.test.set.beans.specializes.jakarta.JakartaSpecializesJakartaBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.specializes.javax.JavaxSpecializesJavaxBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype.jakarta.JakartaCustomStereotype;
import org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype.jakarta.JakartaStereotypeAnnotatedBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype.jakarta.JakartaTypeWithStereotypeAnnotatedField;
import org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype.jakarta.JakartaTypeWithStereotypeAnnotatedMethod;
import org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype.javax.JavaxCustomStereotype;
import org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype.javax.JavaxStereotypeAnnotatedBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype.javax.JavaxTypeWithStereotypeAnnotatedField;
import org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype.javax.JavaxTypeWithStereotypeAnnotatedMethod;
import org.jqassistant.plugin.jee.ejb.test.set.beans.jakarta.JakartaSingletonBean;
import org.jqassistant.plugin.jee.ejb.test.set.beans.javax.JavaxSingletonBean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.assertj.FieldDescriptorCondition.fieldDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.assertj.MethodDescriptorCondition.methodDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.assertj.TypeDescriptorCondition.typeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

/**
 * Tests for the CDI concepts.
 */
class CdiIT extends AbstractJavaPluginIT {

    /**
     * Verifies the concept "cdi:Dependent".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxDependentBean.class, JakartaDependentBean.class})
    void dependent(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("cdi:Dependent").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        final List<Object> column = query("MATCH (e:CDI:Dependent) RETURN e").getColumn("e");
        assertThat(column).hasSize(3);
        assertThat(column.stream().filter(TypeDescriptor.class::isInstance).map(TypeDescriptor.class::cast))
                .haveExactly(1, typeDescriptor(classToScan));
        assertThat(column.stream().filter(MethodDescriptor.class::isInstance).map(MethodDescriptor.class::cast))
                .haveExactly(1, methodDescriptor(classToScan, "producerMethod"));
        assertThat(column.stream().filter(FieldDescriptor.class::isInstance).map(FieldDescriptor.class::cast))
                .haveExactly(1, fieldDescriptor(classToScan, "producerField"));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:RequestScoped".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxRequestScopedBean.class, JakartaRequestScopedBean.class})
    void requestScoped(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("cdi:RequestScoped").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<Object> column = query("MATCH (e:CDI:RequestScoped) RETURN e").getColumn("e");
        assertThat(column).hasSize(3);
        assertThat(column.stream().filter(TypeDescriptor.class::isInstance).map(TypeDescriptor.class::cast))
                .haveExactly(1, typeDescriptor(classToScan));
        assertThat(column.stream().filter(MethodDescriptor.class::isInstance).map(MethodDescriptor.class::cast))
                .haveExactly(1, methodDescriptor(classToScan, "producerMethod"));
        assertThat(column.stream().filter(FieldDescriptor.class::isInstance).map(FieldDescriptor.class::cast))
                .haveExactly(1, fieldDescriptor(classToScan, "producerField"));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:SessionScoped".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxSessionScopedBean.class, JakartaSessionScopedBean.class})
    void sessionScoped(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("cdi:SessionScoped").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<Object> column = query("MATCH (e:CDI:SessionScoped) RETURN e").getColumn("e");
        assertThat(column).hasSize(3);
        assertThat(column.stream().filter(TypeDescriptor.class::isInstance).map(TypeDescriptor.class::cast))
                .haveExactly(1, typeDescriptor(classToScan));
        assertThat(column.stream().filter(MethodDescriptor.class::isInstance).map(MethodDescriptor.class::cast))
                .haveExactly(1, methodDescriptor(classToScan, "producerMethod"));
        assertThat(column.stream().filter(FieldDescriptor.class::isInstance).map(FieldDescriptor.class::cast))
                .haveExactly(1, fieldDescriptor(classToScan, "producerField"));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:ConversationScoped".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxConversationScopedBean.class, JakartaConversationScopedBean.class})
    void conversationScoped(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("cdi:ConversationScoped").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<Object> column = query("MATCH (e:CDI:ConversationScoped) RETURN e").getColumn("e");
        assertThat(column).hasSize(3);
        assertThat(column.stream().filter(TypeDescriptor.class::isInstance).map(TypeDescriptor.class::cast))
                .haveExactly(1, typeDescriptor(classToScan));
        assertThat(column.stream().filter(MethodDescriptor.class::isInstance).map(MethodDescriptor.class::cast))
                .haveExactly(1, methodDescriptor(classToScan, "producerMethod"));
        assertThat(column.stream().filter(FieldDescriptor.class::isInstance).map(FieldDescriptor.class::cast))
                .haveExactly(1, fieldDescriptor(classToScan, "producerField"));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:ApplicationScoped".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxApplicationScopedBean.class, JakartaApplicationScopedBean.class})
    void applicationScoped(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("cdi:ApplicationScoped").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<Object> column = query("MATCH (e:CDI:ApplicationScoped) RETURN e").getColumn("e");
        assertThat(column).hasSize(3);
        assertThat(column.stream().filter(TypeDescriptor.class::isInstance).map(TypeDescriptor.class::cast))
                .haveExactly(1, typeDescriptor(classToScan));
        assertThat(column.stream().filter(MethodDescriptor.class::isInstance).map(MethodDescriptor.class::cast))
                .haveExactly(1, methodDescriptor(classToScan, "producerMethod"));
        assertThat(column.stream().filter(FieldDescriptor.class::isInstance).map(FieldDescriptor.class::cast))
                .haveExactly(1, fieldDescriptor(classToScan, "producerField"));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:SingletonScoped".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxSingletonScopedBean.class, JakartaSingletonScopedBean.class})
    void singletonScoped(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("cdi:SingletonScoped").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<TypeDescriptor> column = query("MATCH (e:CDI:SingletonScoped:JEE:Injectable) RETURN e").getColumn("e");
        assertThat(column).haveExactly(1, typeDescriptor(classToScan));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:Stereotype".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxCustomStereotype.class, JakartaCustomStereotype.class})
    void stereotype(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("cdi:Stereotype").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<TypeDescriptor> column = query("MATCH (s:CDI:Stereotype) RETURN s").getColumn("s");
        assertThat(column).haveExactly(1, typeDescriptor(classToScan));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:Alternative".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = { JavaxAlternativeBean.class, JakartaAlternativeBean.class})
    void alternative(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("cdi:Alternative").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<TypeDescriptor> column = query("MATCH (a:CDI:Alternative) RETURN a").getColumn("a");
        assertThat(column).haveExactly(1, typeDescriptor(classToScan));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:Specializes".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxSpecializesJavaxBean.class, JakartaSpecializesJakartaBean.class})
    void specializes(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("cdi:Specializes").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<Object> column = query("MATCH (e:CDI:Specializes) RETURN e").getColumn("e");
        assertThat(column).hasSize(2);
        assertThat(column.stream().filter(TypeDescriptor.class::isInstance).map(TypeDescriptor.class::cast))
                .haveExactly(1, typeDescriptor(classToScan));
        assertThat(column.stream().filter(MethodDescriptor.class::isInstance).map(MethodDescriptor.class::cast))
                .haveExactly(1, methodDescriptor(classToScan, "doSomething"));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:Qualifier".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxCustomQualifier.class, JakartaCustomQualifier.class})
    void qualifier(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("cdi:Qualifier").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        final List<TypeDescriptor> types = query("MATCH (e:Type:CDI:Qualifier) RETURN e").getColumn("e");
        assertThat(types).haveExactly(1, typeDescriptor(classToScan));
        final List<MethodDescriptor> methods = query("MATCH (q:Qualifier)-[:DECLARES]->(a:CDI:Method:Nonbinding) RETURN a").getColumn("a");
        assertThat(methods).haveExactly(1, methodDescriptor(classToScan, "nonBindingValue"));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:Produces".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @MethodSource("scopedBeans")
    void produces(Class<?>[] classesToScan) throws Exception {
        scanClasses(classesToScan);
        assertThat(applyConcept("cdi:Produces").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<Object> column = query("MATCH (p)-[:PRODUCES]->({fqn:'java.lang.String'}) RETURN p").getColumn("p");
        assertThat(column).hasSize(10);

        final List<FieldDescriptor> fields = column.stream().filter(FieldDescriptor.class::isInstance).map(FieldDescriptor.class::cast).collect(Collectors.toList());
        assertThat(fields).haveExactly(1, fieldDescriptor(classesToScan[0], "producerField"));
        assertThat(fields).haveExactly(1, fieldDescriptor(classesToScan[1], "producerField"));
        assertThat(fields).haveExactly(1, fieldDescriptor(classesToScan[2], "producerField"));
        assertThat(fields).haveExactly(1, fieldDescriptor(classesToScan[3], "producerField"));
        assertThat(fields).haveExactly(1, fieldDescriptor(classesToScan[4], "producerField"));

        final List<MethodDescriptor> methods = column.stream().filter(MethodDescriptor.class::isInstance).map(MethodDescriptor.class::cast).collect(Collectors.toList());
        assertThat(methods).haveExactly(1, methodDescriptor(classesToScan[0], "producerMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(classesToScan[1], "producerMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(classesToScan[2], "producerMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(classesToScan[3], "producerMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(classesToScan[4], "producerMethod"));
        store.commitTransaction();
    }

    private static Stream<Arguments> scopedBeans() {
        return Stream.of(Arguments.of((Object) new Class[] { JavaxApplicationScopedBean.class, JavaxConversationScopedBean.class, JavaxDependentBean.class,
                JavaxRequestScopedBean.class, JavaxSessionScopedBean.class }), Arguments.of(
                (Object) new Class[] { JakartaApplicationScopedBean.class, JakartaConversationScopedBean.class, JakartaDependentBean.class,
                        JakartaRequestScopedBean.class, JakartaSessionScopedBean.class }));
    }

    /**
     * Verifies the uniqueness of concept "cdi:Produces" with keeping existing
     * properties.
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxApplicationScopedBean.class, JakartaApplicationScopedBean.class})
    void producesUnique(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        store.beginTransaction();
        // create existing relations with and without properties
        assertThat(query("MATCH (m:Method {name: 'producerMethod'}), (t {fqn:'java.lang.String'}) MERGE (m)-[r:PRODUCES {prop: 'value'}]->(t) RETURN r")
                .getColumn("r")).hasSize(1);
        assertThat(query("MATCH (f:Field {name: 'producerField'}), (t {fqn:'java.lang.String'}) MERGE (f)-[r:PRODUCES]->(t) RETURN r")
                .getColumn("r")).hasSize(1);
        verifyUniqueRelation("PRODUCES", 2);
        store.commitTransaction();
        assertThat(applyConcept("cdi:Produces").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        verifyUniqueRelation("PRODUCES", 2);
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:Disposes".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxDisposesBean.class, JakartaDisposesBean.class})
    void disposes(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("cdi:Disposes").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        final List<TypeDescriptor> column = query("MATCH (p:Parameter)-[:DISPOSES]->(disposedType:Type) RETURN disposedType")
                .getColumn("disposedType");
        assertThat(column).haveExactly(1, typeDescriptor(String.class));
        store.commitTransaction();
    }

    /**
     * Verifies the uniqueness of concept "cdi:Disposes" with keeping existing
     * properties.
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxDisposesBean.class, JakartaDisposesBean.class})
    void disposesUnique(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        store.beginTransaction();
        // create existing relation with property
        assertThat(query("MATCH (p:Parameter), (t {fqn:'java.lang.String'}) MERGE (p)-[r:DISPOSES {prop: 'value'}]->(t) RETURN r")
                .getColumn("r")).hasSize(1);
        verifyUniqueRelation("DISPOSES", 1);
        store.commitTransaction();
        assertThat(applyConcept("cdi:Disposes").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        verifyUniqueRelation("DISPOSES", 1);
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:Named".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxNamedBean.class, JakartaNamedBean.class})
    void named(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("cdi:Named").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<Object> column = query("MATCH (e:CDI:Named) RETURN e").getColumn("e");
        assertThat(column).hasSize(2);
        assertThat(column.stream().filter(TypeDescriptor.class::isInstance).map(TypeDescriptor.class::cast))
                .haveExactly(1, typeDescriptor(classToScan));
        assertThat(column.stream().filter(MethodDescriptor.class::isInstance).map(MethodDescriptor.class::cast))
                .haveExactly(1, methodDescriptor(classToScan, "getValue"));
        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = { JavaxDecoratorBean.class, JakartaDecoratorBean.class})
    void decorator(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("cdi:Decorator").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<TypeDescriptor> column = query("MATCH (e:CDI:Decorator:JEE:Injectable) RETURN e").getColumn("e");
        assertThat(column).haveExactly(1, typeDescriptor(classToScan));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:Any".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = { JavaxDecoratorBean.class, JakartaDecoratorBean.class})
    void any(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("cdi:Any").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<FieldDescriptor> column = query("MATCH (e:CDI:Any) RETURN e").getColumn("e");
        assertThat(column).haveExactly(1, fieldDescriptor(classToScan, "delegate"));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:New".
     * Only checks for {@link JavaxNewBean} because "cdi:New" is not supported anymore by jakarta.cdi. v4.0.
     * @throws IOException If the test fails.
     *
     */
    @Test
    void newQualifier() throws Exception {
        scanClasses(JavaxNewBean.class);
        assertThat(applyConcept("cdi:New").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<FieldDescriptor> column = query("MATCH (e:CDI:New) RETURN e").getColumn("e");
        assertThat(column)
                .haveExactly(1, fieldDescriptor(JavaxNewBean.class, "bean"));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:Default".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxDefaultBean.class, JakartaDefaultBean.class})
    void defaultQualifier(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("cdi:Default").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<FieldDescriptor> column = query("MATCH (e:CDI:Default) RETURN e").getColumn("e");
        assertThat(column).haveExactly(1, fieldDescriptor(classToScan, "bean"));
        store.commitTransaction();
    }

    @ParameterizedTest
    @MethodSource("customStereotypeAndBeanForClassTypesClasses")
    void injectableClassType(Class<?> classToScan,  Class<?> clazz) throws RuleException {
        scanClasses(classToScan, clazz);
        final Result<Concept> conceptResult = applyConcept("cdi:InjectableClassType");
        assertThat(conceptResult.getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        assertThat(conceptResult.getRows()).hasSize(1);
        assertThat(conceptResult.getRows().get(0).getColumns().get("Injectable").getValue())
                .asInstanceOf(type(TypeDescriptor.class)).is(typeDescriptor(clazz));
        final List<TypeDescriptor> injectableTypes =
                query("MATCH (injectableType:Java:Type:Injectable) RETURN injectableType").getColumn("injectableType");
        assertThat(injectableTypes).hasSize(1);
        assertThat(injectableTypes.get(0)).is(typeDescriptor(clazz));
        store.commitTransaction();
    }

    private static Stream<Arguments> customStereotypeAndBeanForClassTypesClasses() {
        return Stream.of(Arguments.of(JavaxCustomStereotype.class, JavaxDependentBean.class),
                Arguments.of(JavaxCustomStereotype.class, JavaxRequestScopedBean.class),
                Arguments.of(JavaxCustomStereotype.class, JavaxSessionScopedBean.class),
                Arguments.of(JavaxCustomStereotype.class, JavaxConversationScopedBean.class),
                Arguments.of(JavaxCustomStereotype.class, JavaxApplicationScopedBean.class),
                Arguments.of(JavaxCustomStereotype.class, JavaxStereotypeAnnotatedBean.class),
                Arguments.of(JakartaCustomStereotype.class, JakartaDependentBean.class),
                Arguments.of(JakartaCustomStereotype.class, JakartaRequestScopedBean.class),
                Arguments.of(JakartaCustomStereotype.class, JakartaSessionScopedBean.class),
                Arguments.of(JakartaCustomStereotype.class, JakartaConversationScopedBean.class),
                Arguments.of(JakartaCustomStereotype.class, JakartaApplicationScopedBean.class),
                Arguments.of(JakartaCustomStereotype.class, JakartaStereotypeAnnotatedBean.class));
    }

    @ParameterizedTest
    @MethodSource("customStereotypeAndBeanForFieldTypesClasses")
    void injectableFieldType(Class<?> classToScan,  Class<?> clazz) throws RuleException {
        scanClasses(classToScan, ProducedBean.class, clazz);
        final Result<Concept> conceptResult = applyConcept("cdi:InjectableFieldType");
        assertThat(conceptResult.getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        assertThat(conceptResult.getRows()).hasSize(1);
        assertThat(conceptResult.getRows().get(0).getColumns().get("Injectable").getValue())
                .asInstanceOf(type(TypeDescriptor.class)).is(typeDescriptor(ProducedBean.class));
        final List<TypeDescriptor> injectableTypes =
                query("MATCH (injectableType:Java:Type:Injectable) RETURN injectableType").getColumn("injectableType");
        assertThat(injectableTypes).hasSize(1);
        assertThat(injectableTypes.get(0)).is(typeDescriptor(ProducedBean.class));
        store.commitTransaction();
    }

    private static Stream<Arguments> customStereotypeAndBeanForFieldTypesClasses() {
        return Stream.of(Arguments.of(JavaxCustomStereotype.class, JavaxTypeWithApplicationScopedField.class),
                Arguments.of(JavaxCustomStereotype.class, JavaxTypeWithConversationScopedField.class),
                Arguments.of(JavaxCustomStereotype.class, JavaxTypeWithDependentField.class),
                Arguments.of(JavaxCustomStereotype.class, JavaxTypeWithRequestScopedField.class),
                Arguments.of(JavaxCustomStereotype.class, JavaxTypeWithSessionScopedField.class),
                Arguments.of(JavaxCustomStereotype.class, JavaxTypeWithStereotypeAnnotatedField.class),
                Arguments.of(JakartaCustomStereotype.class, JakartaTypeWithApplicationScopedField.class),
                Arguments.of(JakartaCustomStereotype.class, JakartaTypeWithConversationScopedField.class),
                Arguments.of(JakartaCustomStereotype.class, JakartaTypeWithDependentField.class),
                Arguments.of(JakartaCustomStereotype.class, JakartaTypeWithRequestScopedField.class),
                Arguments.of(JakartaCustomStereotype.class, JakartaTypeWithSessionScopedField.class),
                Arguments.of(JakartaCustomStereotype.class, JakartaTypeWithStereotypeAnnotatedField.class));
    }

    @ParameterizedTest
    @MethodSource("customStereotypeAndBeanForReturnTypesClasses")
    void injectableReturnType(Class<?> classToScan,  Class<?> clazz) throws RuleException {
        scanClasses(classToScan, ProducedBean.class, clazz);
        final Result<Concept> conceptResult = applyConcept("cdi:InjectableReturnType");
        assertThat(conceptResult.getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        assertThat(conceptResult.getRows()).hasSize(1);
        assertThat(conceptResult.getRows().get(0).getColumns().get("Injectable").getValue())
                .asInstanceOf(type(TypeDescriptor.class)).is(typeDescriptor(ProducedBean.class));
        final List<TypeDescriptor> injectableTypes =
                query("MATCH (injectableType:Java:Type:Injectable) RETURN injectableType").getColumn("injectableType");
        assertThat(injectableTypes).hasSize(1);
        assertThat(injectableTypes.get(0)).is(typeDescriptor(ProducedBean.class));
        store.commitTransaction();
    }

    private static Stream<Arguments> customStereotypeAndBeanForReturnTypesClasses() {
        return Stream.of(Arguments.of(JavaxCustomStereotype.class, JavaxTypeWithApplicationScopedMethod.class),
                Arguments.of(JavaxCustomStereotype.class, JavaxTypeWithConversationScopedMethod.class),
                Arguments.of(JavaxCustomStereotype.class, JavaxTypeWithDependentMethod.class),
                Arguments.of(JavaxCustomStereotype.class, JavaxTypeWithRequestScopedMethod.class),
                Arguments.of(JavaxCustomStereotype.class, JavaxTypeWithSessionScopedMethod.class),
                Arguments.of(JavaxCustomStereotype.class, JavaxTypeWithStereotypeAnnotatedMethod.class),
                Arguments.of(JakartaCustomStereotype.class, JakartaTypeWithApplicationScopedMethod.class),
                Arguments.of(JakartaCustomStereotype.class, JakartaTypeWithConversationScopedMethod.class),
                Arguments.of(JakartaCustomStereotype.class, JakartaTypeWithDependentMethod.class),
                Arguments.of(JakartaCustomStereotype.class, JakartaTypeWithRequestScopedMethod.class),
                Arguments.of(JakartaCustomStereotype.class, JakartaTypeWithSessionScopedMethod.class),
                Arguments.of(JakartaCustomStereotype.class, JakartaTypeWithStereotypeAnnotatedMethod.class));
    }

    @ParameterizedTest
    @MethodSource("classesToScanForprovidedConceptJeeInjectable")
    void providedConceptJeeInjectable(Class<?>[] classesToScan, String jCase) throws RuleException {
        scanClasses(classesToScan);
        final Result<Concept> conceptResult = applyConcept("jee-injection:Injectable");
        assertThat(conceptResult.getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        final List<TypeDescriptor> conceptResultTypes = conceptResult.getRows().stream()
                .map(Row::getColumns)
                .map(columns -> columns.get("Injectable"))
                .map(Column::getValue)
                .map(TypeDescriptor.class::cast)
                .collect(Collectors.toList());
        assertCdiInjectables(conceptResultTypes, jCase);

        final List<TypeDescriptor> injectableTypes =
                query("MATCH (injectableType:Java:Type:Injectable) RETURN injectableType").getColumn("injectableType");
        assertCdiInjectables(injectableTypes, jCase);
        store.commitTransaction();
    }

    private static Stream<Arguments> classesToScanForprovidedConceptJeeInjectable() {
        return Stream.of(Arguments.of(
                new Class[] { JavaxDependentBean.class, JavaxRequestScopedBean.class, JavaxSessionScopedBean.class, JavaxConversationScopedBean.class,
                        JavaxApplicationScopedBean.class, JavaxSingletonBean.class, JavaxDecoratorBean.class, ProducedBean.class,
                        JavaxTypeWithApplicationScopedField.class, JavaxTypeWithConversationScopedField.class, JavaxTypeWithDependentField.class,
                        JavaxTypeWithRequestScopedField.class, JavaxTypeWithSessionScopedField.class, JavaxTypeWithApplicationScopedMethod.class,
                        JavaxTypeWithConversationScopedMethod.class, JavaxTypeWithDependentMethod.class, JavaxTypeWithRequestScopedMethod.class,
                        JavaxTypeWithSessionScopedMethod.class, JavaxStereotypeAnnotatedBean.class, JavaxCustomStereotype.class }, "javax"), Arguments.of(
                new Class[] { JakartaDependentBean.class, JakartaRequestScopedBean.class, JakartaSessionScopedBean.class,
                        JakartaConversationScopedBean.class, JakartaApplicationScopedBean.class, JakartaSingletonBean.class, JakartaDecoratorBean.class,
                        ProducedBean.class, JakartaTypeWithApplicationScopedField.class, JakartaTypeWithConversationScopedField.class,
                        JakartaTypeWithDependentField.class, JakartaTypeWithRequestScopedField.class, JakartaTypeWithSessionScopedField.class,
                        JakartaTypeWithApplicationScopedMethod.class, JakartaTypeWithConversationScopedMethod.class, JakartaTypeWithDependentMethod.class,
                        JakartaTypeWithRequestScopedMethod.class, JakartaTypeWithSessionScopedMethod.class, JakartaStereotypeAnnotatedBean.class,
                        JakartaCustomStereotype.class }, "jakarta"));
    }

    private static void assertCdiInjectables(List<TypeDescriptor> actualTypes, String jCase) {
        assertThat(actualTypes).hasSize(10);
        assertThat(actualTypes).haveExactly(1, typeDescriptor(String.class)); // Caused by @Produces
        assertThat(actualTypes).haveExactly(1, typeDescriptor(ProducedBean.class));

        switch (jCase) {
        case "javax":
            assertThat(actualTypes).haveExactly(1, typeDescriptor(JavaxDependentBean.class));
            assertThat(actualTypes).haveExactly(1, typeDescriptor(JavaxRequestScopedBean.class));
            assertThat(actualTypes).haveExactly(1, typeDescriptor(JavaxSessionScopedBean.class));
            assertThat(actualTypes).haveExactly(1, typeDescriptor(JavaxConversationScopedBean.class));
            assertThat(actualTypes).haveExactly(1, typeDescriptor(JavaxApplicationScopedBean.class));
            assertThat(actualTypes).haveExactly(1, typeDescriptor(JavaxSingletonBean.class));
            assertThat(actualTypes).haveExactly(1, typeDescriptor(JavaxDecoratorBean.class));
            assertThat(actualTypes).haveExactly(1, typeDescriptor(JavaxStereotypeAnnotatedBean.class));
            break;

        case "jakarta":
            assertThat(actualTypes).haveExactly(1, typeDescriptor(JakartaDependentBean.class));
            assertThat(actualTypes).haveExactly(1, typeDescriptor(JakartaRequestScopedBean.class));
            assertThat(actualTypes).haveExactly(1, typeDescriptor(JakartaSessionScopedBean.class));
            assertThat(actualTypes).haveExactly(1, typeDescriptor(JakartaConversationScopedBean.class));
            assertThat(actualTypes).haveExactly(1, typeDescriptor(JakartaApplicationScopedBean.class));
            assertThat(actualTypes).haveExactly(1, typeDescriptor(JakartaSingletonBean.class));
            assertThat(actualTypes).haveExactly(1, typeDescriptor(JakartaDecoratorBean.class));
            assertThat(actualTypes).haveExactly(1, typeDescriptor(JakartaStereotypeAnnotatedBean.class));
            break;
        }
    }

    /**
     * Verifies a unique relation with property. An existing transaction is
     * assumed.
     *
     * @param relationName The name of the relation.
     * @param total        The total of relations with the given name.
     */
    private void verifyUniqueRelation(String relationName, int total) {
        assertThat(query("MATCH ()-[r:" + relationName + " {prop: 'value'}]->() RETURN r").getColumn("r")).hasSize(1);
        assertThat(query("MATCH ()-[r:" + relationName + "]->() RETURN r").getColumn("r")).hasSize(total);
    }
}
