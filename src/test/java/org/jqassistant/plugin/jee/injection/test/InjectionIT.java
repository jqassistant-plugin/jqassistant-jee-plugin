package org.jqassistant.plugin.jee.injection.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Row;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.plugin.java.api.model.FieldDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.jqassistant.plugin.jee.injection.test.set.jakarta.*;
import org.jqassistant.plugin.jee.injection.test.set.javax.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

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
    @ValueSource(classes = { JavaxBeanProducerWithConstraintViolations.class, JakartaBeanProducerWithConstraintViolations.class})
    void beanProducerAccess(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:BeanProducerMustNotBeInvokedDirectly");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(1);
        final TypeDescriptor actualType = (TypeDescriptor) constraintResult.getRows().get(0).getColumns().get("Type").getValue();
        assertThat(actualType).is(typeDescriptor(classToScan));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Invocation").getLabel()).endsWith("void beanProducerAccessor()");
        store.commitTransaction();
    }

    private static Stream<Arguments>  beanProducerWithConstraintViolationsAndInjectableClasses() {
        return Stream.of(
                Arguments.of(JavaxBeanProducerWithConstraintViolations.class, JavaxInjectableA.class, JavaxInjectableB.class),
                Arguments.of(JakartaBeanProducerWithConstraintViolations.class, JakartaInjectableA.class, JakartaInjectableB.class)
        );
    }

    /**
     * Verifies the constraint "jee-injection:FieldsOfInjectablesMustNotBeManipulated".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @MethodSource("beanProducerWithConstraintViolationsAndInjectableClasses")
    void injectableFieldManipulation(Class<?> producerWithViolations, Class<?> injectableA, Class<?> injectableB) throws Exception {
        scanClasses(producerWithViolations, injectableA, injectableB);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:FieldsOfInjectablesMustNotBeManipulated");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(2);
        assertThat((TypeDescriptor) constraintResult.getRows().get(0).getColumns().get("Injectable").getValue())
                .is(typeDescriptor(injectableA));
        assertThat(constraintResult.getRows().get(0).getColumns().get("WriteToInjectableField").getLabel()).endsWith("void manipulateField()");
        assertThat(constraintResult.getRows().get(0).getColumns().get("Field").getLabel()).endsWith("InjectableB fieldOfInjectable1");
        assertThat((TypeDescriptor) constraintResult.getRows().get(1).getColumns().get("Injectable").getValue())
                .is(typeDescriptor(injectableA));
        assertThat(constraintResult.getRows().get(1).getColumns().get("WriteToInjectableField").getLabel()).endsWith("void accessFieldStatically()");
        assertThat(constraintResult.getRows().get(1).getColumns().get("Field").getLabel()).endsWith("InjectableB fieldOfInjectable2");
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:InjectablesShouldBeHeldInFinalFields".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @MethodSource("beanProducerWithConstraintViolationsAndInjectableClasses")
    void finalFieldsForInjectables(Class<?> producerWithViolations, Class<?> injectableA, Class<?> injectableB) throws Exception {
        scanClasses(producerWithViolations, injectableA, injectableB);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:InjectablesShouldBeHeldInFinalFields");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows().size()).isEqualTo(2);
        assertThat((TypeDescriptor) constraintResult.getRows().get(0).getColumns().get("Type").getValue())
                .is(typeDescriptor(injectableA));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Field").getLabel()).endsWith("InjectableB fieldOfInjectable1");
        assertThat((TypeDescriptor) constraintResult.getRows().get(1).getColumns().get("Type").getValue())
                .is(typeDescriptor(injectableA));
        assertThat(constraintResult.getRows().get(1).getColumns().get("Field").getLabel()).endsWith("InjectableB fieldOfInjectable2");
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:InjectablesMustNotBeAccessedStatically".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @MethodSource("beanProducerWithConstraintViolationsAndInjectableClasses")
    void staticAccessOfInjectables(Class<?> producerWithViolations, Class<?> injectableA, Class<?> injectableB) throws Exception {
        scanClasses(producerWithViolations, injectableA, injectableB);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:InjectablesMustNotBeAccessedStatically");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(1);
        assertThat((TypeDescriptor) constraintResult.getRows().get(0).getColumns().get("Type").getValue())
                .is(typeDescriptor(injectableA));
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
    @MethodSource("beanProducerWithConstraintViolationsAndInjectableClasses")
    void staticVariablesForInjectables(Class<?> producerWithViolations, Class<?> injectableA, Class<?> injectableB) throws Exception {
        scanClasses(producerWithViolations, injectableA, injectableB);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:InjectablesMustNotBeHeldInStaticVariables");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(1);
        assertThat((TypeDescriptor) constraintResult.getRows().get(0).getColumns().get("Type").getValue())
                .is(typeDescriptor(injectableA));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Field").getLabel()).endsWith("InjectableB fieldOfInjectable2");
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:InjectablesMustNotBeInstantiated".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @MethodSource("beanProducerWithConstraintViolationsAndInjectableClasses")
    void injectableInstantiation(Class<?> producerWithViolations, Class<?> injectableA, Class<?> injectableB) throws Exception {
        scanClasses(producerWithViolations, injectableA, injectableB);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:InjectablesMustNotBeInstantiated");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(1);
        assertThat((TypeDescriptor) constraintResult.getRows().get(0).getColumns().get("Type").getValue())
                .is(typeDescriptor(injectableA));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Method").getLabel()).endsWith("void injectableInstantiation()");
        assertThat((TypeDescriptor) constraintResult.getRows().get(0).getColumns().get("Injectable").getValue())
                .is(typeDescriptor(injectableB));
        store.commitTransaction();
    }

    private static Stream<Arguments> injectableOfNonInjectableClasses() {
        return Stream.of(Arguments.of(JavaxBeanProducerWithConstraintViolations.class, JavaxInjectableA.class, JavaxNonInjectableType.class,
                        JavaxLocalEjb.class),
                Arguments.of(JakartaBeanProducerWithConstraintViolations.class, JakartaInjectableA.class, JakartaNonInjectableType.class,
                        JakartaLocalEjb.class));
    }

    /**
     * Verifies the constraint "jee-injection:InjectablesMustNotBeInstantiated" with a test class without violations.
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxBeanProducerWithoutConstraintViolations.class, JakartaBeanProducerWithoutConstraintViolations.class})
    void injectableInstantiationWithoutViolations(Class<?> producerWithoutViolation) throws Exception {
        scanClasses(producerWithoutViolation);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:InjectablesMustNotBeInstantiated");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:InjectablesMustOnlyBeHeldInInjectables".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @MethodSource("injectableOfNonInjectableClasses")
    void injectableOfNonInjectable(Class<?> producerWithViolations, Class<?> injectableA, Class<?> nonInjectable, Class<?> localEjb) throws Exception {
        scanClasses(producerWithViolations, injectableA, nonInjectable, localEjb);
        final Result<Constraint> constraintResult = validateConstraint("jee-injection:InjectablesMustOnlyBeHeldInInjectables");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows().size()).isEqualTo(2);

        final Map<String, List<?>> violations = constraintResult.getRows().stream()
                .map(Row::getColumns)
                .collect(Collectors.toMap(
                        map -> map.get("NonInjectableHavingInjectablesAsField").getLabel(),
                        map -> (List<?>)  map.get("Fields").getValue()
                ));

        Assertions.assertThat(violations.keySet()).containsExactlyInAnyOrder(producerWithViolations.getName(), nonInjectable.getName());
        Assertions.assertThat(violations.get(nonInjectable.getName()))
                .asInstanceOf(InstanceOfAssertFactories.LIST).containsExactly(injectableA.getName());
        Assertions.assertThat(violations.get(producerWithViolations.getName()))
                .asInstanceOf(InstanceOfAssertFactories.LIST).containsExactly(localEjb.getName());

        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:JdkClassesMustNotBeInjectables".
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxBeanProducerWithConstraintViolations.class, JakartaBeanProducerWithConstraintViolations.class})
    void jdkClassesAsInjectables(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:JdkClassesMustNotBeInjectables");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(1);
        assertThat(constraintResult.getRows().get(0).getColumns().get("Injectable").getLabel()).endsWith("java.lang.String");
        store.commitTransaction();
    }

    private static Stream<Arguments> clockAsInjectableClasses() {
        return Stream.of(Arguments.of((Object) new Class[] { JavaxBeanWithInjectedClock.class, JavaxBeanWithInjectedClock.ClockProducer.class }),
                Arguments.of((Object) new Class[] { JakartaBeanWithInjectedClock.class, JakartaBeanWithInjectedClock.ClockProducer.class }));
    }

    /**
     * Verifies that the constraint "jee-injection:JdkClassesMustNotBeInjectables" whitelists {@link java.time.Clock}.
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @MethodSource("clockAsInjectableClasses")
    void clockAsInjectable(Class<?>[] classesToScan) throws Exception {
        scanClasses(classesToScan);
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
        scanClasses(classToScan);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:NoCombinationOfBeanProducersAndApplicationCode");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(1);
        final TypeDescriptor actualType = (TypeDescriptor) constraintResult.getRows().get(0).getColumns().get("Java-Klasse").getValue();
        assertThat(actualType).is(typeDescriptor(classToScan));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:NoCombinationOfBeanProducersAndApplicationCode" with a test class without violations.
     *
     * @throws IOException If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = { JavaxBeanProducerWithoutConstraintViolations.class, JakartaBeanProducerWithoutConstraintViolations.class})
    void combinationOfBeanProducersAndApplicationCodeWithoutViolation(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:NoCombinationOfBeanProducersAndApplicationCode");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(SUCCESS);
        assertThat(constraintResult.getRows().size()).isEqualTo(0);
        store.commitTransaction();
    }

    private static Stream<Arguments> beanProducerAndLocalEjbClasses() {
        return Stream.of(Arguments.of(JavaxBeanProducerWithConstraintViolations.class, JavaxLocalEjb.class),
                Arguments.of(JakartaBeanProducerWithConstraintViolations.class, JakartaLocalEjb.class));
    }

    /**
     * Verifies the constraint "jee-injection:BeansMustNotUseFieldInjection.
     */
    @ParameterizedTest
    @MethodSource("beanProducerAndLocalEjbClasses")
    void fieldInjectionWithEjb_Violation(Class<?> beanProducerWithViolations, Class<?> localEjb) throws Exception {
        scanClasses(beanProducerWithViolations, localEjb);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:BeansMustNotUseFieldInjection");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(2);

        final List<TypeDescriptor> types = constraintResult.getRows().stream()
                .map(Row::getColumns)
                .map(row -> row.get("Type").getValue())
                .map(TypeDescriptor.class::cast)
                .collect(Collectors.toList());
        assertThat(types).haveExactly(2, typeDescriptor(beanProducerWithViolations));

        final List<FieldDescriptor> fields = constraintResult.getRows().stream()
                .map(Row::getColumns)
                .map(row -> row.get("Field").getValue())
                .map(FieldDescriptor.class::cast)
                .collect(Collectors.toList());
        assertThat(fields).haveExactly(1, fieldDescriptor(beanProducerWithViolations, "injectionPointField"));
        assertThat(fields).haveExactly(1, fieldDescriptor(beanProducerWithViolations, "ejb"));

        assertThat(constraintResult.getRows().get(0).getColumns().get("Field").getLabel()).endsWith("Object injectionPointField");
        store.commitTransaction();
    }

    private static Stream<Arguments> beansWithConstructorAndSetterInjectionClasses() {
        return Stream.of(Arguments.of(JavaxBeanWithConstructorInjection.class, JavaxBeanWithSetterInjection.class),
                Arguments.of(JakartaBeanWithConstructorInjection.class, JakartaBeanWithSetterInjection.class));
    }

    /**
     * Verifies the constraint "jee-injection:BeansMustNotUseFieldInjection results in no
     * violations when applied to beans with setter or constructor injection.
     */
    @ParameterizedTest
    @MethodSource("beansWithConstructorAndSetterInjectionClasses")
    void fieldInjectionWithEjb_No_Violation(Class<?> beanWithConstructorInjection, Class<?> beanWithSetterInjection) throws Exception {
        scanClasses(beanWithConstructorInjection, beanWithSetterInjection);
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
