package org.jqassistant.plugin.jee.cdi.test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.buschmais.jqassistant.core.report.api.model.Column;
import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Row;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.plugin.java.api.model.FieldDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.MethodDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.jqassistant.plugin.jee.cdi.test.set.beans.alternative.AlternativeBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.decorator.DecoratorBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.inject.DefaultBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.inject.NewBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.qualifier.CustomQualifier;
import org.jqassistant.plugin.jee.cdi.test.set.beans.qualifier.NamedBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.scope.*;
import org.jqassistant.plugin.jee.cdi.test.set.beans.specializes.SpecializesBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype.CustomStereotype;
import org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype.StereotypeAnnotatedBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype.TypeWithStereotypeAnnotatedField;
import org.jqassistant.plugin.jee.cdi.test.set.beans.stereotype.TypeWithStereotypeAnnotatedMethod;
import org.jqassistant.plugin.jee.ejb.test.set.beans.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
    @Test
    void dependent() throws Exception {
        scanClasses(DependentBean.class);
        assertThat(applyConcept("cdi:Dependent").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        final List<Object> column = query("MATCH (e:CDI:Dependent) RETURN e").getColumn("e");
        assertThat(column).hasSize(3);
        assertThat(column.stream().filter(TypeDescriptor.class::isInstance).map(TypeDescriptor.class::cast))
                .haveExactly(1, typeDescriptor(DependentBean.class));
        assertThat(column.stream().filter(MethodDescriptor.class::isInstance).map(MethodDescriptor.class::cast))
                .haveExactly(1, methodDescriptor(DependentBean.class, "producerMethod"));
        assertThat(column.stream().filter(FieldDescriptor.class::isInstance).map(FieldDescriptor.class::cast))
                .haveExactly(1, fieldDescriptor(DependentBean.class, "producerField"));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:RequestScoped".
     *
     * @throws IOException If the test fails.
     */
    @Test
    void requestScoped() throws Exception {
        scanClasses(RequestScopedBean.class);
        assertThat(applyConcept("cdi:RequestScoped").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<Object> column = query("MATCH (e:CDI:RequestScoped) RETURN e").getColumn("e");
        assertThat(column).hasSize(3);
        assertThat(column.stream().filter(TypeDescriptor.class::isInstance).map(TypeDescriptor.class::cast))
                .haveExactly(1, typeDescriptor(RequestScopedBean.class));
        assertThat(column.stream().filter(MethodDescriptor.class::isInstance).map(MethodDescriptor.class::cast))
                .haveExactly(1, methodDescriptor(RequestScopedBean.class, "producerMethod"));
        assertThat(column.stream().filter(FieldDescriptor.class::isInstance).map(FieldDescriptor.class::cast))
                .haveExactly(1, fieldDescriptor(RequestScopedBean.class, "producerField"));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:SessionScoped".
     *
     * @throws IOException If the test fails.
     */
    @Test
    void sessionScoped() throws Exception {
        scanClasses(SessionScopedBean.class);
        assertThat(applyConcept("cdi:SessionScoped").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<Object> column = query("MATCH (e:CDI:SessionScoped) RETURN e").getColumn("e");
        assertThat(column).hasSize(3);
        assertThat(column.stream().filter(TypeDescriptor.class::isInstance).map(TypeDescriptor.class::cast))
                .haveExactly(1, typeDescriptor(SessionScopedBean.class));
        assertThat(column.stream().filter(MethodDescriptor.class::isInstance).map(MethodDescriptor.class::cast))
                .haveExactly(1, methodDescriptor(SessionScopedBean.class, "producerMethod"));
        assertThat(column.stream().filter(FieldDescriptor.class::isInstance).map(FieldDescriptor.class::cast))
                .haveExactly(1, fieldDescriptor(SessionScopedBean.class, "producerField"));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:ConversationScoped".
     *
     * @throws IOException If the test fails.
     */
    @Test
    void conversationScoped() throws Exception {
        scanClasses(ConversationScopedBean.class);
        assertThat(applyConcept("cdi:ConversationScoped").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<Object> column = query("MATCH (e:CDI:ConversationScoped) RETURN e").getColumn("e");
        assertThat(column).hasSize(3);
        assertThat(column.stream().filter(TypeDescriptor.class::isInstance).map(TypeDescriptor.class::cast))
                .haveExactly(1, typeDescriptor(ConversationScopedBean.class));
        assertThat(column.stream().filter(MethodDescriptor.class::isInstance).map(MethodDescriptor.class::cast))
                .haveExactly(1, methodDescriptor(ConversationScopedBean.class, "producerMethod"));
        assertThat(column.stream().filter(FieldDescriptor.class::isInstance).map(FieldDescriptor.class::cast))
                .haveExactly(1, fieldDescriptor(ConversationScopedBean.class, "producerField"));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:ApplicationScoped".
     *
     * @throws IOException If the test fails.
     */
    @Test
    void applicationScoped() throws Exception {
        scanClasses(ApplicationScopedBean.class);
        assertThat(applyConcept("cdi:ApplicationScoped").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<Object> column = query("MATCH (e:CDI:ApplicationScoped) RETURN e").getColumn("e");
        assertThat(column).hasSize(3);
        assertThat(column.stream().filter(TypeDescriptor.class::isInstance).map(TypeDescriptor.class::cast))
                .haveExactly(1, typeDescriptor(ApplicationScopedBean.class));
        assertThat(column.stream().filter(MethodDescriptor.class::isInstance).map(MethodDescriptor.class::cast))
                .haveExactly(1, methodDescriptor(ApplicationScopedBean.class, "producerMethod"));
        assertThat(column.stream().filter(FieldDescriptor.class::isInstance).map(FieldDescriptor.class::cast))
                .haveExactly(1, fieldDescriptor(ApplicationScopedBean.class, "producerField"));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:SingletonScoped".
     *
     * @throws IOException If the test fails.
     */
    @Test
    void singletonScoped() throws Exception {
        scanClasses(SingletonScopedBean.class);
        assertThat(applyConcept("cdi:SingletonScoped").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<TypeDescriptor> column = query("MATCH (e:CDI:SingletonScoped:JEE:Injectable) RETURN e").getColumn("e");
        assertThat(column).haveExactly(1, typeDescriptor(SingletonScopedBean.class));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:Stereotype".
     *
     * @throws IOException If the test fails.
     */
    @Test
    void stereotype() throws Exception {
        scanClasses(CustomStereotype.class);
        assertThat(applyConcept("cdi:Stereotype").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<TypeDescriptor> column = query("MATCH (s:CDI:Stereotype) RETURN s").getColumn("s");
        assertThat(column).haveExactly(1, typeDescriptor(CustomStereotype.class));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:Alternative".
     *
     * @throws IOException If the test fails.
     */
    @Test
    void alternative() throws Exception {
        scanClasses(AlternativeBean.class);
        assertThat(applyConcept("cdi:Alternative").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<TypeDescriptor> column = query("MATCH (a:CDI:Alternative) RETURN a").getColumn("a");
        assertThat(column).haveExactly(1, typeDescriptor(AlternativeBean.class));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:Specializes".
     *
     * @throws IOException If the test fails.
     */
    @Test
    void specializes() throws Exception {
        scanClasses(SpecializesBean.class);
        assertThat(applyConcept("cdi:Specializes").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<Object> column = query("MATCH (e:CDI:Specializes) RETURN e").getColumn("e");
        assertThat(column).hasSize(2);
        assertThat(column.stream().filter(TypeDescriptor.class::isInstance).map(TypeDescriptor.class::cast))
                .haveExactly(1, typeDescriptor(SpecializesBean.class));
        assertThat(column.stream().filter(MethodDescriptor.class::isInstance).map(MethodDescriptor.class::cast))
                .haveExactly(1, methodDescriptor(SpecializesBean.class, "doSomething"));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:Qualifier".
     *
     * @throws IOException If the test fails.
     */
    @Test
    void qualifier() throws Exception {
        scanClasses(CustomQualifier.class);
        assertThat(applyConcept("cdi:Qualifier").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        final List<TypeDescriptor> types = query("MATCH (e:Type:CDI:Qualifier) RETURN e").getColumn("e");
        assertThat(types).haveExactly(1, typeDescriptor(CustomQualifier.class));
        final List<MethodDescriptor> methods = query("MATCH (q:Qualifier)-[:DECLARES]->(a:CDI:Method:Nonbinding) RETURN a").getColumn("a");
        assertThat(methods).haveExactly(1, methodDescriptor(CustomQualifier.class, "nonBindingValue"));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:Produces".
     *
     * @throws IOException If the test fails.
     */
    @Test
    void produces() throws Exception {
        scanClasses(ApplicationScopedBean.class, ConversationScopedBean.class, DependentBean.class, RequestScopedBean.class, SessionScopedBean.class);
        assertThat(applyConcept("cdi:Produces").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<Object> column = query("MATCH (p)-[:PRODUCES]->({fqn:'java.lang.String'}) RETURN p").getColumn("p");
        assertThat(column).hasSize(10);

        final List<FieldDescriptor> fields = column.stream().filter(FieldDescriptor.class::isInstance).map(FieldDescriptor.class::cast).collect(Collectors.toList());
        assertThat(fields).haveExactly(1, fieldDescriptor(ApplicationScopedBean.class, "producerField"));
        assertThat(fields).haveExactly(1, fieldDescriptor(ConversationScopedBean.class, "producerField"));
        assertThat(fields).haveExactly(1, fieldDescriptor(DependentBean.class, "producerField"));
        assertThat(fields).haveExactly(1, fieldDescriptor(RequestScopedBean.class, "producerField"));
        assertThat(fields).haveExactly(1, fieldDescriptor(SessionScopedBean.class, "producerField"));

        final List<MethodDescriptor> methods = column.stream().filter(MethodDescriptor.class::isInstance).map(MethodDescriptor.class::cast).collect(Collectors.toList());
        assertThat(methods).haveExactly(1, methodDescriptor(ApplicationScopedBean.class, "producerMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(ConversationScopedBean.class, "producerMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(DependentBean.class, "producerMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(RequestScopedBean.class, "producerMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(SessionScopedBean.class, "producerMethod"));
        store.commitTransaction();
    }

    /**
     * Verifies the uniqueness of concept "cdi:Produces" with keeping existing
     * properties.
     *
     * @throws IOException If the test fails.
     */
    @Test
    void producesUnique() throws Exception {
        scanClasses(ApplicationScopedBean.class);
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
    @Test
    void disposes() throws Exception {
        scanClasses(DisposesBean.class);
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
    @Test
    void disposesUnique() throws Exception {
        scanClasses(DisposesBean.class);
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
    @Test
    void named() throws Exception {
        scanClasses(NamedBean.class);
        assertThat(applyConcept("cdi:Named").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<Object> column = query("MATCH (e:CDI:Named) RETURN e").getColumn("e");
        assertThat(column).hasSize(2);
        assertThat(column.stream().filter(TypeDescriptor.class::isInstance).map(TypeDescriptor.class::cast))
                .haveExactly(1, typeDescriptor(NamedBean.class));
        assertThat(column.stream().filter(MethodDescriptor.class::isInstance).map(MethodDescriptor.class::cast))
                .haveExactly(1, methodDescriptor(NamedBean.class, "getValue"));
        store.commitTransaction();
    }

    @Test
    void decorator() throws Exception {
        scanClasses(DecoratorBean.class);
        assertThat(applyConcept("cdi:Decorator").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<TypeDescriptor> column = query("MATCH (e:CDI:Decorator:JEE:Injectable) RETURN e").getColumn("e");
        assertThat(column).haveExactly(1, typeDescriptor(DecoratorBean.class));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:Any".
     *
     * @throws IOException If the test fails.
     */
    @Test
    void any() throws Exception {
        scanClasses(DecoratorBean.class);
        assertThat(applyConcept("cdi:Any").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<FieldDescriptor> column = query("MATCH (e:CDI:Any) RETURN e").getColumn("e");
        assertThat(column).haveExactly(1, fieldDescriptor(DecoratorBean.class, "delegate"));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:New".
     *
     * @throws IOException If the test fails.
     */
    @Test
    void newQualifier() throws Exception {
        scanClasses(NewBean.class);
        assertThat(applyConcept("cdi:New").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<FieldDescriptor> column = query("MATCH (e:CDI:New) RETURN e").getColumn("e");
        assertThat(column).haveExactly(1, fieldDescriptor(NewBean.class, "bean"));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:Default".
     *
     * @throws IOException If the test fails.
     */
    @Test
    void defaultQualifier() throws Exception {
        scanClasses(DefaultBean.class);
        assertThat(applyConcept("cdi:Default").getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.beginTransaction();
        List<FieldDescriptor> column = query("MATCH (e:CDI:Default) RETURN e").getColumn("e");
        assertThat(column).haveExactly(1, fieldDescriptor(DefaultBean.class, "bean"));
        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = {
            DependentBean.class, RequestScopedBean.class, SessionScopedBean.class, ConversationScopedBean.class,
            ApplicationScopedBean.class, StereotypeAnnotatedBean.class
    })
    void injectableClassType(Class<?> clazz) throws RuleException {
        scanClasses(CustomStereotype.class, clazz);
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

    @ParameterizedTest
    @ValueSource(classes = {
            TypeWithApplicationScopedField.class, TypeWithConversationScopedField.class, TypeWithDependentField.class,
            TypeWithRequestScopedField.class, TypeWithSessionScopedField.class, TypeWithStereotypeAnnotatedField.class
    })
    void injectableFieldType(Class<?> clazz) throws RuleException {
        scanClasses(CustomStereotype.class,  ProducedBean.class, clazz);
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

    @ParameterizedTest
    @ValueSource(classes = {
            TypeWithApplicationScopedMethod.class, TypeWithConversationScopedMethod.class,
            TypeWithDependentMethod.class, TypeWithRequestScopedMethod.class, TypeWithSessionScopedMethod.class,
            TypeWithStereotypeAnnotatedMethod.class
    })
    void injectableReturnType(Class<?> clazz) throws RuleException {
        scanClasses(CustomStereotype.class,  ProducedBean.class, clazz);
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

    @Test
    void providedConceptJeeInjectable() throws RuleException {
        scanClasses(DependentBean.class, RequestScopedBean.class, SessionScopedBean.class, ConversationScopedBean.class,
                ApplicationScopedBean.class, SingletonBean.class, DecoratorBean.class,
                ProducedBean.class, TypeWithApplicationScopedField.class, TypeWithConversationScopedField.class,
                TypeWithDependentField.class, TypeWithRequestScopedField.class, TypeWithSessionScopedField.class,
                TypeWithApplicationScopedMethod.class, TypeWithConversationScopedMethod.class,
                TypeWithDependentMethod.class, TypeWithRequestScopedMethod.class, TypeWithSessionScopedMethod.class,
                StereotypeAnnotatedBean.class, CustomStereotype.class);
        final Result<Concept> conceptResult = applyConcept("jee-injection:Injectable");
        assertThat(conceptResult.getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        final List<TypeDescriptor> conceptResultTypes = conceptResult.getRows().stream()
                .map(Row::getColumns)
                .map(columns -> columns.get("Injectable"))
                .map(Column::getValue)
                .map(TypeDescriptor.class::cast)
                .collect(Collectors.toList());
        assertCdiInjectables(conceptResultTypes);

        final List<TypeDescriptor> injectableTypes =
                query("MATCH (injectableType:Java:Type:Injectable) RETURN injectableType").getColumn("injectableType");
        assertCdiInjectables(injectableTypes);
        store.commitTransaction();
    }

    private static void assertCdiInjectables(List<TypeDescriptor> actualTypes) {
        assertThat(actualTypes).hasSize(10);
        assertThat(actualTypes).haveExactly(1, typeDescriptor(DependentBean.class));
        assertThat(actualTypes).haveExactly(1, typeDescriptor(RequestScopedBean.class));
        assertThat(actualTypes).haveExactly(1, typeDescriptor(SessionScopedBean.class));
        assertThat(actualTypes).haveExactly(1, typeDescriptor(ConversationScopedBean.class));
        assertThat(actualTypes).haveExactly(1, typeDescriptor(ApplicationScopedBean.class));
        assertThat(actualTypes).haveExactly(1, typeDescriptor(SingletonBean.class));
        assertThat(actualTypes).haveExactly(1, typeDescriptor(DecoratorBean.class));
        assertThat(actualTypes).haveExactly(1, typeDescriptor(ProducedBean.class));
        assertThat(actualTypes).haveExactly(1, typeDescriptor(StereotypeAnnotatedBean.class));
        assertThat(actualTypes).haveExactly(1, typeDescriptor(String.class)); // Caused by @Produces
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
