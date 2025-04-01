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
import org.jqassistant.plugin.jee.injection.test.set.BeanWithConstructorInjection;
import org.jqassistant.plugin.jee.injection.test.set.BeanWithSetterInjection;
import org.jqassistant.plugin.jee.injection.test.set.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Test
    void injectionPointIdentification() throws Exception {
        scanClasses(BeanProducerWithConstraintViolations.class);
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
    @Test
    void beanProducerAccess() throws Exception {
        scanClasses(BeanProducerWithConstraintViolations.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:BeanProducerMustNotBeInvokedDirectly");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(1);
        assertThat(constraintResult.getRows().get(0).getColumns().get("Type").getLabel()).endsWith("test.set.BeanProducerWithConstraintViolations");
        assertThat(constraintResult.getRows().get(0).getColumns().get("Invocation").getLabel()).endsWith("void beanProducerAccessor()");
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:FieldsOfInjectablesMustNotBeManipulated".
     *
     * @throws IOException If the test fails.
     */
    @Test
    void injectableFieldManipulation() throws Exception {
        scanClasses(BeanProducerWithConstraintViolations.class, InjectableA.class, InjectableB.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:FieldsOfInjectablesMustNotBeManipulated");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(2);
        assertThat(constraintResult.getRows().get(0).getColumns().get("Injectable").getLabel()).endsWith("test.set.InjectableA");
        assertThat(constraintResult.getRows().get(0).getColumns().get("WriteToInjectableField").getLabel()).endsWith("void manipulateField()");
        assertThat(constraintResult.getRows().get(0).getColumns().get("Field").getLabel()).endsWith("InjectableB fieldOfInjectable1");
        assertThat(constraintResult.getRows().get(1).getColumns().get("Injectable").getLabel()).endsWith("test.set.InjectableA");
        assertThat(constraintResult.getRows().get(1).getColumns().get("WriteToInjectableField").getLabel()).endsWith("void accessFieldStatically()");
        assertThat(constraintResult.getRows().get(1).getColumns().get("Field").getLabel()).endsWith("InjectableB fieldOfInjectable2");
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:InjectablesShouldBeHeldInFinalFields".
     *
     * @throws IOException If the test fails.
     */
    @Test
    void finalFieldsForInjectables() throws Exception {
        scanClasses(BeanProducerWithConstraintViolations.class, InjectableA.class, InjectableB.class);
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
    @Test
    void staticAccessOfInjectables() throws Exception {
        scanClasses(BeanProducerWithConstraintViolations.class, InjectableA.class, InjectableB.class);
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
    @Test
    void staticVariablesForInjectables() throws Exception {
        scanClasses(BeanProducerWithConstraintViolations.class, InjectableA.class, InjectableB.class);
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
    @Test
    void injectableInstantiation() throws Exception {
        scanClasses(BeanProducerWithConstraintViolations.class, InjectableA.class, InjectableB.class);
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
    @Test
    void injectableOfNonInjectable() throws Exception {
        scanClasses(BeanProducerWithConstraintViolations.class, InjectableA.class, NonInjectableType.class, LocalEjb.class);
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

        Assertions.assertThat(violations.keySet()).containsExactlyInAnyOrder(NonInjectableType.class.getName(), BeanProducerWithConstraintViolations.class.getName());
        Assertions.assertThat(violations.get(NonInjectableType.class.getName()))
                .asInstanceOf(InstanceOfAssertFactories.LIST).containsExactly(InjectableA.class.getName());
        Assertions.assertThat(violations.get(BeanProducerWithConstraintViolations.class.getName()))
                .asInstanceOf(InstanceOfAssertFactories.LIST).containsExactly(LocalEjb.class.getName());

        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:JdkClassesMustNotBeInjectables".
     *
     * @throws IOException If the test fails.
     */
    @Test
    void jdkClassesAsInjectables() throws Exception {
        scanClasses(BeanProducerWithConstraintViolations.class);
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
    @Test
    void clockAsInjectable() throws Exception {
        scanClasses(BeanWithInjectedClock.class, BeanWithInjectedClock.ClockProducer.class);
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
    @Test
    void combinationOfBeanProducersAndApplicationCodeWithViolation() throws Exception {
        scanClasses(BeanProducerWithConstraintViolations.class);
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
    @Test
    void combinationOfBeanProducersAndApplicationCodeWithoutViolation() throws Exception {
        scanClasses(BeanProducerWithoutConstraintViolations.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:NoCombinationOfBeanProducersAndApplicationCode");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(SUCCESS);
        assertThat(constraintResult.getRows().size()).isEqualTo(0);
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:BeansMustNotUseFieldInjection.
     */
    @Test
    void fieldInjectionWithEjb_Violation() throws Exception {
        scanClasses(BeanProducerWithConstraintViolations.class, LocalEjb.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:BeansMustNotUseFieldInjection");
        store.beginTransaction();
        assertThat(constraintResult.getStatus()).isEqualTo(Result.Status.FAILURE);
        assertThat(constraintResult.getRows()).hasSize(2);

        final List<TypeDescriptor> types = constraintResult.getRows().stream()
                .map(Row::getColumns)
                .map(row -> row.get("Type").getValue())
                .map(TypeDescriptor.class::cast)
                .collect(Collectors.toList());
        assertThat(types).haveExactly(2, typeDescriptor(BeanProducerWithConstraintViolations.class));

        final List<FieldDescriptor> fields = constraintResult.getRows().stream()
                .map(Row::getColumns)
                .map(row -> row.get("Field").getValue())
                .map(FieldDescriptor.class::cast)
                .collect(Collectors.toList());
        assertThat(fields).haveExactly(1, fieldDescriptor(BeanProducerWithConstraintViolations.class, "injectionPointField"));
        assertThat(fields).haveExactly(1, fieldDescriptor(BeanProducerWithConstraintViolations.class, "ejb"));

        assertThat(constraintResult.getRows().get(0).getColumns().get("Field").getLabel()).endsWith("Object injectionPointField");
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:BeansMustNotUseFieldInjection results in no
     * violations when applied to beans with setter or constructor injection.
     */
    @Test
    void fieldInjectionWithEjb_No_Violation() throws Exception {
        scanClasses(BeanWithConstructorInjection.class, BeanWithSetterInjection.class);
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
