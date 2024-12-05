package org.jqassistant.plugin.jee.injection.test;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Row;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import org.jqassistant.plugin.jee.injection.test.set.BeanWithConstructorInjection;
import org.jqassistant.plugin.jee.injection.test.set.BeanWithFieldInjection;
import org.jqassistant.plugin.jee.injection.test.set.BeanWithSetterInjection;
import org.jqassistant.plugin.jee.injection.test.set.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.core.test.matcher.ConstraintMatcher.constraint;
import static com.buschmais.jqassistant.core.test.matcher.ResultMatcher.result;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class InjectionIT extends AbstractJavaPluginIT {

    /**
     * Verifies the concept "jee-injection:InjectionPoint".
     *
     * @throws IOException
     *             If the test fails.
     */
    @Test
    void injectionPointIdentification() throws Exception {
        scanClasses(BeanProducer.class);
        Result<Concept> conceptResult = applyConcept("jee-injection:InjectionPoint");
        store.beginTransaction();
        assertThat(conceptResult.getStatus(), equalTo(Result.Status.SUCCESS));
        assertThat(conceptResult.getRows().size(), equalTo(3));
        assertThat(conceptResult.getRows().get(0).getColumns().get("InjectionPoint").getLabel(), endsWith("LocalEjb ejb"));
        assertThat(conceptResult.getRows().get(1).getColumns().get("InjectionPoint").getLabel(), endsWith("java.lang.Object injectionPointField"));
        assertThat(conceptResult.getRows().get(2).getColumns().get("InjectionPoint").getLabel(), endsWith("void test()"));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:BeanProducerMustNotBeInvokedDirectly".
     *
     * @throws IOException
     *             If the test fails.
     */
    @Test
    void beanProducerAccess() throws Exception {
        scanClasses(BeanProducer.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:BeanProducerMustNotBeInvokedDirectly");
        store.beginTransaction();
        assertThat(constraintResult.getStatus(), equalTo(Result.Status.FAILURE));
        assertThat(constraintResult.getRows().size(), equalTo(1));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Type").getLabel(), endsWith("test.set.BeanProducer"));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Invocation").getLabel(), endsWith("void beanProducerAccessor()"));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:FieldsOfInjectablesMustNotBeManipulated".
     *
     * @throws IOException
     *             If the test fails.
     */
    @Test
    void injectableFieldManipulation() throws Exception {
        scanClasses(BeanProducer.class, InjectableA.class, InjectableB.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:FieldsOfInjectablesMustNotBeManipulated");
        store.beginTransaction();
        assertThat(constraintResult.getStatus(), equalTo(Result.Status.FAILURE));
        assertThat(constraintResult.getRows().size(), equalTo(2));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Injectable").getLabel(), endsWith("test.set.InjectableA"));
        assertThat(constraintResult.getRows().get(0).getColumns().get("WriteToInjectableField").getLabel(), endsWith("void manipulateField()"));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Field").getLabel(), endsWith("InjectableB fieldOfInjectable1"));
        assertThat(constraintResult.getRows().get(1).getColumns().get("Injectable").getLabel(), endsWith("test.set.InjectableA"));
        assertThat(constraintResult.getRows().get(1).getColumns().get("WriteToInjectableField").getLabel(), endsWith("void accessFieldStatically()"));
        assertThat(constraintResult.getRows().get(1).getColumns().get("Field").getLabel(), endsWith("InjectableB fieldOfInjectable2"));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:InjectablesShouldBeHeldInFinalFields".
     *
     * @throws IOException
     *             If the test fails.
     */
    @Test
    void finalFieldsForInjectables() throws Exception {
        scanClasses(BeanProducer.class, InjectableA.class, InjectableB.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:InjectablesShouldBeHeldInFinalFields");
        store.beginTransaction();
        assertThat(constraintResult.getStatus(), equalTo(Result.Status.FAILURE));
        assertThat(constraintResult.getRows().size(), equalTo(2));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Type").getLabel(), endsWith("test.set.InjectableA"));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Field").getLabel(), endsWith("InjectableB fieldOfInjectable1"));
        assertThat(constraintResult.getRows().get(1).getColumns().get("Type").getLabel(), endsWith("test.set.InjectableA"));
        assertThat(constraintResult.getRows().get(1).getColumns().get("Field").getLabel(), endsWith("InjectableB fieldOfInjectable2"));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:InjectablesMustNotBeAccessedStatically".
     *
     * @throws IOException
     *             If the test fails.
     */
    @Test
    void staticAccessOfInjectables() throws Exception {
        scanClasses(BeanProducer.class, InjectableA.class, InjectableB.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:InjectablesMustNotBeAccessedStatically");
        store.beginTransaction();
        assertThat(constraintResult.getStatus(), equalTo(Result.Status.FAILURE));
        assertThat(constraintResult.getRows().size(), equalTo(1));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Type").getLabel(), endsWith("test.set.InjectableA"));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Method").getLabel(), endsWith("void accessFieldStatically()"));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Field").getLabel(), endsWith("InjectableB fieldOfInjectable2"));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:InjectablesMustNotBeHeldInStaticVariables".
     *
     * @throws IOException
     *             If the test fails.
     */
    @Test
    void staticVariablesForInjectables() throws Exception {
        scanClasses(BeanProducer.class, InjectableA.class, InjectableB.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:InjectablesMustNotBeHeldInStaticVariables");
        store.beginTransaction();
        assertThat(constraintResult.getStatus(), equalTo(Result.Status.FAILURE));
        assertThat(constraintResult.getRows().size(), equalTo(1));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Type").getLabel(), endsWith("test.set.InjectableA"));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Field").getLabel(), endsWith("InjectableB fieldOfInjectable2"));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:InjectablesMustNotBeInstantiated".
     *
     * @throws IOException
     *             If the test fails.
     */
    @Test
    void injectableInstantiation() throws Exception {
        scanClasses(BeanProducer.class, InjectableA.class, InjectableB.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:InjectablesMustNotBeInstantiated");
        store.beginTransaction();
        assertThat(constraintResult.getStatus(), equalTo(Result.Status.FAILURE));
        assertThat(constraintResult.getRows().size(), equalTo(1));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Type").getLabel(), endsWith("test.set.InjectableA"));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Method").getLabel(), endsWith("void injectableInstantiation()"));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Injectable").getLabel(), endsWith("test.set.InjectableB"));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:InjectablesMustOnlyBeHeldInInjectables".
     *
     * @throws IOException
     *             If the test fails.
     */
    @Test
    void injectableOfNonInjectable() throws Exception {
        scanClasses(BeanProducer.class, InjectableA.class, NonInjectableType.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:InjectablesMustOnlyBeHeldInInjectables");
        store.beginTransaction();
        assertThat(constraintResult.getStatus(), equalTo(Result.Status.FAILURE));
        assertThat(constraintResult.getRows().size(), equalTo(1));
        assertThat(constraintResult.getRows().get(0).getColumns().get("NonInjectableHavingInjectablesAsField").getLabel(), endsWith("test.set.NonInjectableType"));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Fields").getLabel(), endsWith("test.set.InjectableA"));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:JdkClassesMustNotBeInjectables".
     *
     * @throws IOException
     *             If the test fails.
     */
    @Test
    void jdkClassesAsInjectables() throws Exception {
        scanClasses(BeanProducer.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:JdkClassesMustNotBeInjectables");
        store.beginTransaction();
        assertThat(constraintResult.getStatus(), equalTo(Result.Status.FAILURE));
        assertThat(constraintResult.getRows().size(), equalTo(1));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Injectable").getLabel(), endsWith("java.lang.String"));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:NoCombinationOfBeanProducersAndApplicationCode".
     *
     * @throws IOException
     *             If the test fails.
     */
    @Test
    void combinationOfBeanProducersAndApplicationCode() throws Exception {
        scanClasses(BeanProducer.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:NoCombinationOfBeanProducersAndApplicationCode");
        store.beginTransaction();
        assertThat(constraintResult.getStatus(), equalTo(Result.Status.FAILURE));
        assertThat(constraintResult.getRows().size(), equalTo(1));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Java-Klasse").getLabel(), endsWith("test.set.BeanProducer"));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:BeansMustNotUseFieldInjectionExceptEJBs".
     *
     * @throws IOException
     *             If the test fails.
     */
    @Test
    void fieldInjectionWithEjb() throws Exception {
        scanClasses(BeanProducer.class, LocalEjb.class);
        Result<Constraint> constraintResult = validateConstraint("jee-injection:BeansMustNotUseFieldInjectionExceptEJBs");
        store.beginTransaction();
        assertThat(constraintResult.getStatus(), equalTo(Result.Status.FAILURE));
        assertThat(constraintResult.getRows().size(), equalTo(1));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Type").getLabel(), endsWith("test.set.BeanProducer"));
        assertThat(constraintResult.getRows().get(0).getColumns().get("Field").getLabel(), endsWith("Object injectionPointField"));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:BeansMustNotUseFieldInjection" results in no
     * violations when applied to beans with setter or constructor injection.
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void fieldInjection_No_Violation() throws Exception {
        scanClasses(BeanWithConstructorInjection.class);
        scanClasses(BeanWithSetterInjection.class);
        String ruleName = "jee-injection:BeansMustNotUseFieldInjection";
        assertThat(validateConstraint(ruleName).getStatus(), equalTo(SUCCESS));
        store.beginTransaction();

        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat("Unexpected number of violated constraints", constraintViolations.size(), equalTo(1));
        Result<Constraint> result = constraintViolations.get(0);
        assertThat("Expected constraint " + ruleName, result, result(constraint(ruleName)));
        List<Row> violations = result.getRows();
        assertThat("Unexpected number of violations", violations.size(), equalTo(0));

        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:BeansMustNotUseFieldInjection".
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void beanInjection() throws Exception {
        scanClasses(BeanWithFieldInjection.class);
        String ruleName = "jee-injection:BeansMustNotUseFieldInjection";
        assertThat(validateConstraint(ruleName).getStatus(), equalTo(FAILURE));
        store.beginTransaction();

        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat("Unexpected number of violated constraints", constraintViolations.size(), equalTo(1));
        Result<Constraint> result = constraintViolations.get(0);
        assertThat("Expected constraint " + ruleName, result, result(constraint(ruleName)));

        List<Row> violations = result.getRows();
        assertThat("Unexpected number of violations", violations, hasSize(1));
        assertThat("Unexpected bean name", BeanWithFieldInjection.class.getName(), equalTo(violations.get(0).getColumns().get("invalidBean").getValue()));

        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:BeansMustUseConstructorInjection".
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void constructorInjection() throws Exception {
        scanClasses(BeanWithFieldInjection.class);
        String ruleName = "jee-injection:BeansMustUseConstructorInjection";
        assertThat(validateConstraint(ruleName).getStatus(), equalTo(FAILURE));

        store.beginTransaction();

        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat("Unexpected number of violated constraints", constraintViolations.size(), equalTo(1));

        Result<Constraint> result = constraintViolations.get(0);
        assertThat("Expected constraint " + ruleName, result, result(constraint(ruleName)));

        List<Row> violatedBeans = result.getRows();
        assertThat("Unexpected number of violations", violatedBeans.size(), equalTo(1));
        assertThat("Unexpected bean name", BeanWithFieldInjection.class.getName(), equalTo(violatedBeans.get(0).getColumns().get("invalidBean").getValue()));

        store.commitTransaction();
    }

    /**
     * Verifies the constraint "jee-injection:BeansMustUseConstructorInjection" results in
     * no violations when applied to valid beans.
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void constructorInjection_No_Violation() throws Exception {
        scanClasses(BeanWithConstructorInjection.class);
        String ruleName = "jee-injection:BeansMustUseConstructorInjection";
        assertThat(validateConstraint(ruleName).getStatus(), equalTo(SUCCESS));
        store.beginTransaction();

        List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat("Unexpected number of violated constraints", constraintViolations.size(), equalTo(1));
        Result<Constraint> result = constraintViolations.get(0);
        assertThat("Expected constraint " + ruleName, result, result(constraint(ruleName)));
        List<Row> violatedBeans = result.getRows();
        assertThat("Unexpected number of violations", violatedBeans.size(), equalTo(0));

        store.commitTransaction();
    }
}
