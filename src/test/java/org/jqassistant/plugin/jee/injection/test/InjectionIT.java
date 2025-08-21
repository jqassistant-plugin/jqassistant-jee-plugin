package org.jqassistant.plugin.jee.injection.test;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Row;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.plugin.java.api.model.FieldDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.jqassistant.plugin.jee.ejb.test.set.beans.jakarta.JakartaStatelessLocalBean;
import org.jqassistant.plugin.jee.ejb.test.set.beans.javax.JavaxStatelessLocalBean;
import org.jqassistant.plugin.jee.injection.test.set.jakarta.JakartaBeanProducerWithConstraintViolations;
import org.jqassistant.plugin.jee.injection.test.set.jakarta.JakartaInjectableA;
import org.jqassistant.plugin.jee.injection.test.set.jakarta.JakartaInjectableB;
import org.jqassistant.plugin.jee.injection.test.set.javax.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.assertj.FieldDescriptorCondition.fieldDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.assertj.TypeDescriptorCondition.typeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;


public class InjectionIT extends AbstractJavaPluginIT {

    /**
     * Verifies the concept "jee-injection:InjectionPoint".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = { JavaxBeanProducerWithConstraintViolations.class, JakartaBeanProducerWithConstraintViolations.class})
    void injectionPointIdentification(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        Result<Concept> conceptResult = applyConcept("jee-injection:InjectionPoint");
        store.beginTransaction();
        assertThat(conceptResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        assertThat(conceptResult.getRows()).hasSize(3);
        assertThat(conceptResult.getRows().get(0).getColumns().get("InjectionPoint").getLabel()).endsWith("LocalEjb ejb");
        assertThat(conceptResult.getRows().get(1).getColumns().get("InjectionPoint").getLabel()).endsWith("java.lang.Object injectionPointField");
        assertThat(conceptResult.getRows().get(2).getColumns().get("InjectionPoint").getLabel()).endsWith("void test()");
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:BeanProducerMustNotBeInvokedDirectly".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @MethodSource("beanProducerClasses")
    void beanProducerAccess(Class<?> classToScan, String expectedTypeEnding) throws Exception {
        scanClasses(classToScan);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:BeanProducerMustNotBeInvokedDirectly");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(1);
        assertThat(constraintResult.getRows().get(0).getColumns().get("Type").getLabel()).endsWith(expectedTypeEnding);
        assertThat(constraintResult.getRows().get(0).getColumns().get("Invocation").getLabel()).endsWith("void beanProducerAccessor()");
        store.commitTransaction();
    }

    private static Stream<Arguments> beanProducerClasses() {
        return Stream.of(
                Arguments.of(JavaxBeanProducerWithConstraintViolations.class, "test.set.javax.JavaxBeanProducerWithConstraintViolations"),
                Arguments.of(JakartaBeanProducerWithConstraintViolations.class,  "test.set.jakarta.JakartaBeanProducerWithConstraintViolations")
        );
    }

    /**
     * Verifies the constraint "jee-injection:FieldsOfInjectablesMustNotBeManipulated".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @MethodSource("finalFieldsForInjectablesClasses")
    void injectableFieldManipulation(Class<?> classToScan, String expectedTypeEndingInjectableA) throws Exception {
        scanClasses(classToScan);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:FieldsOfInjectablesMustNotBeManipulated");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(2);
        assertThat(constraintResult.getRows().get(0).getColumns().get("Injectable").getLabel()).endsWith(expectedTypeEndingInjectableA);
        assertThat(constraintResult.getRows().get(0).getColumns().get("WriteToInjectableField").getLabel()).endsWith("void manipulateField()");
        assertThat(constraintResult.getRows().get(0).getColumns().get("Field").getLabel()).endsWith("InjectableB fieldOfInjectable1");
        assertThat(constraintResult.getRows().get(1).getColumns().get("Injectable").getLabel()).endsWith(expectedTypeEndingInjectableA);
        assertThat(constraintResult.getRows().get(1).getColumns().get("WriteToInjectableField").getLabel()).endsWith("void accessFieldStatically()");
        assertThat(constraintResult.getRows().get(1).getColumns().get("Field").getLabel()).endsWith("InjectableB fieldOfInjectable2");
        store.commitTransaction();
    }

    private static Stream<Arguments>  finalFieldsForInjectablesClasses() {
        return Stream.of(
                Arguments.of(JavaxBeanProducerWithConstraintViolations.class, "test.set.javax.JavaxInjectableA"),
                Arguments.of(JakartaBeanProducerWithConstraintViolations.class,  "test.set.jakarta.JakartaInjectableA"),
                Arguments.of(JavaxInjectableA.class,  "test.set.javax.JavaxInjectableA"),
                Arguments.of(JakartaInjectableA.class,  "test.set.jakarta.JakartaInjectableA"),
                Arguments.of(JavaxInjectableB.class,  "test.set.javax.JavaxInjectableA"),
                Arguments.of(JakartaInjectableB.class,  "test.set.jakarta.JakartaInjectableA")
        );
    }

    /**
     * Verifies the constraint "jee-injection:InjectablesShouldBeHeldInFinalFields".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = { JavaxBeanProducerWithConstraintViolations.class, JakartaBeanProducerWithConstraintViolations.class, JavaxInjectableA.class, JakartaInjectableA.class, JavaxInjectableB.class, JakartaInjectableB.class})
    void finalFieldsForInjectables(Class<?> classToScan) throws Exception {
        scanClasses(JavaxBeanProducerWithConstraintViolations.class, JavaxInjectableA.class, JavaxInjectableB.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:InjectablesShouldBeHeldInFinalFields");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows().size()).isEqualTo(2);
        assertThat(constraintResult.getRows().get(0).getColumns().get("Type").getLabel()).endsWith("test.set.InjectableA");
        assertThat(constraintResult.getRows().get(0).getColumns().get("Field").getLabel()).endsWith("InjectableB fieldOfInjectable1");
        assertThat(constraintResult.getRows().get(1).getColumns().get("Type").getLabel()).endsWith("test.set.InjectableA");
        assertThat(constraintResult.getRows().get(1).getColumns().get("Field").getLabel()).endsWith("InjectableB fieldOfInjectable2");
        store.commitTransaction();
    }



    /**
     * Verifies the constraint "jee-injection:InjectablesMustNotBeAccessedStatically".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = { JavaxBeanProducerWithConstraintViolations.class, JakartaBeanProducerWithConstraintViolations.class})
    void staticAccessOfInjectables(Class<?> classToScan) throws Exception {
        scanClasses(JavaxBeanProducerWithConstraintViolations.class, JavaxInjectableA.class, JavaxInjectableB.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:InjectablesMustNotBeAccessedStatically");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(1);
        assertThat(constraintResult.getRows().get(0).getColumns().get("Type").getLabel()).endsWith("test.set.InjectableA");
        assertThat(constraintResult.getRows().get(0).getColumns().get("Method").getLabel()).endsWith("void accessFieldStatically()");
        assertThat(constraintResult.getRows().get(0).getColumns().get("Field").getLabel()).endsWith("InjectableB fieldOfInjectable2");
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:InjectablesMustNotBeHeldInStaticVariables".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = { JavaxBeanProducerWithConstraintViolations.class, JakartaBeanProducerWithConstraintViolations.class})
    void staticVariablesForInjectables(Class<?> classToScan) throws Exception {
        scanClasses(JavaxBeanProducerWithConstraintViolations.class, JavaxInjectableA.class, JavaxInjectableB.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:InjectablesMustNotBeHeldInStaticVariables");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(1);
        assertThat(constraintResult.getRows().get(0).getColumns().get("Type").getLabel()).endsWith("test.set.InjectableA");
        assertThat(constraintResult.getRows().get(0).getColumns().get("Field").getLabel()).endsWith("InjectableB fieldOfInjectable2");
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:InjectablesMustNotBeInstantiated".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = { JavaxBeanProducerWithConstraintViolations.class, JakartaBeanProducerWithConstraintViolations.class})
    void injectableInstantiation(Class<?> classToScan) throws Exception {
        scanClasses(JavaxBeanProducerWithConstraintViolations.class, JavaxInjectableA.class, JavaxInjectableB.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:InjectablesMustNotBeInstantiated");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(1);
        assertThat(constraintResult.getRows().get(0).getColumns().get("Type").getLabel()).endsWith("test.set.InjectableA");
        assertThat(constraintResult.getRows().get(0).getColumns().get("Method").getLabel()).endsWith("void injectableInstantiation()");
        assertThat(constraintResult.getRows().get(0).getColumns().get("Injectable").getLabel()).endsWith("test.set.InjectableB");
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:InjectablesMustOnlyBeHeldInInjectables".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = { JavaxBeanProducerWithConstraintViolations.class, JakartaBeanProducerWithConstraintViolations.class})
    void injectableOfNonInjectable(Class<?> classToScan) throws Exception {
        scanClasses(JavaxBeanProducerWithConstraintViolations.class, JavaxInjectableA.class, JavaxNonInjectableType.class, JavaxLocalEjb.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:InjectablesMustOnlyBeHeldInInjectables");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows().size()).isEqualTo(2);

        final Map<String, List<?>> violations = constraintResult.getRows().stream()
                .map(Row::getColumns)
                .collect(Collectors.toMap(
                        map -> map.get("NonInjectableHavingInjectablesAsField").getLabel(),
                        map -> (List<?>)  map.get("Fields").getValue()
                ));

        Assertions.assertThat(violations.keySet()).containsExactlyInAnyOrder(JavaxNonInjectableType.class.getName(), JavaxBeanProducerWithConstraintViolations.class.getName());
        Assertions.assertThat(violations.get(JavaxNonInjectableType.class.getName()))
                .asInstanceOf(InstanceOfAssertFactories.LIST).containsExactly(JavaxInjectableA.class.getName());
        Assertions.assertThat(violations.get(JavaxBeanProducerWithConstraintViolations.class.getName()))
                .asInstanceOf(InstanceOfAssertFactories.LIST).containsExactly(JavaxLocalEjb.class.getName());

        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:JdkClassesMustNotBeInjectables".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = { JavaxBeanProducerWithConstraintViolations.class, JakartaBeanProducerWithConstraintViolations.class})
    void jdkClassesAsInjectables(Class<?> classToScan) throws Exception {
        scanClasses(JavaxBeanProducerWithConstraintViolations.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:JdkClassesMustNotBeInjectables");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(1);
        assertThat(constraintResult.getRows().get(0).getColumns().get("Injectable").getLabel()).endsWith("java.lang.String");
        store.commitTransaction();
    }

    /**
     * Verifies that the constraint "jee-injection:JdkClassesMustNotBeInjectables" whitelists {@link java.time.Clock}.
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = { JavaxBeanProducerWithConstraintViolations.class, JakartaBeanProducerWithConstraintViolations.class})
    void clockAsInjectable(Class<?> classToScan) throws Exception {
        scanClasses(JavaxBeanWithInjectedClock.class, JavaxBeanWithInjectedClock.ClockProducer.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:JdkClassesMustNotBeInjectables");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(SUCCESS);
        assertThat(constraintResult.getRows().size()).isEqualTo(0);
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:NoCombinationOfBeanProducersAndApplicationCode" with a test class with violations.
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = { JavaxBeanProducerWithConstraintViolations.class, JakartaBeanProducerWithConstraintViolations.class})
    void combinationOfBeanProducersAndApplicationCodeWithViolation(Class<?> classToScan) throws Exception {
        scanClasses(JavaxBeanProducerWithConstraintViolations.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:NoCombinationOfBeanProducersAndApplicationCode");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(1);
        assertThat(constraintResult.getRows().get(0).getColumns().get("Java-Klasse").getLabel()).endsWith("test.set.BeanProducerWithConstraintViolations");
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:NoCombinationOfBeanProducersAndApplicationCode" with a test class without violations.
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = { JavaxBeanProducerWithConstraintViolations.class, JakartaBeanProducerWithConstraintViolations.class})
    void combinationOfBeanProducersAndApplicationCodeWithoutViolation(Class<?> classToScan) throws Exception {
        scanClasses(JavaxBeanProducerWithoutConstraintViolations.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:NoCombinationOfBeanProducersAndApplicationCode");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(SUCCESS);
        assertThat(constraintResult.getRows().size()).isEqualTo(0);
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:BeansMustNotUseFieldInjection.
     */
    @ParameterizedTest
    @ValueSource(classes = { JavaxBeanProducerWithConstraintViolations.class, JakartaBeanProducerWithConstraintViolations.class})
    void fieldInjectionWithEjb_Violation(Class<?> classToScan) throws Exception {
        scanClasses(JavaxBeanProducerWithConstraintViolations.class, JavaxLocalEjb.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:BeansMustNotUseFieldInjection");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(2);

        final List<TypeDescriptor> types = constraintResult.getRows().stream()
                .map(Row::getColumns)
                .map(row -> row.get("Type").getValue())
                .map(TypeDescriptor.class::cast)
                .collect(Collectors.toList());
        assertThat(types).haveExactly(2, typeDescriptor(JavaxBeanProducerWithConstraintViolations.class));

        final List<FieldDescriptor> fields = constraintResult.getRows().stream()
                .map(Row::getColumns)
                .map(row -> row.get("Field").getValue())
                .map(FieldDescriptor.class::cast)
                .collect(Collectors.toList());
        assertThat(fields).haveExactly(1, fieldDescriptor(JavaxBeanProducerWithConstraintViolations.class, "injectionPointField"));
        assertThat(fields).haveExactly(1, fieldDescriptor(JavaxBeanProducerWithConstraintViolations.class, "ejb"));

        assertThat(constraintResult.getRows().get(0).getColumns().get("Field").getLabel()).endsWith("Object injectionPointField");
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:BeansMustNotUseFieldInjection results in no
     * violations when applied to beans with setter or constructor injection.
     */
    @ParameterizedTest
    @ValueSource(classes = { JavaxBeanProducerWithConstraintViolations.class, JakartaBeanProducerWithConstraintViolations.class})
    void fieldInjectionWithEjb_No_Violation(Class<?> classToScan) throws Exception {
        scanClasses(JavaxBeanWithConstructorInjection.class, JavaxBeanWithSetterInjection.class);
        String ruleName = "jee-injection:BeansMustNotUseFieldInjection";
        assertThat(validateConstraint(ruleName).getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();

        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations).hasSize(1);
        Result<Constraint> result = constraintViolations.get(0);
        assertThat(result.getRule().getId()).isEqualTo(ruleName);
        List<Row> violations = result.getRows();
        assertThat(violations).hasSize(0);

        store.commitTransaction();
    }
}
