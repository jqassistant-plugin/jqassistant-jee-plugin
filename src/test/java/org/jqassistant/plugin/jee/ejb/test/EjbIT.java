package org.jqassistant.plugin.jee.ejb.test;

import com.buschmais.jqassistant.core.report.api.model.Column;
import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Row;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.plugin.java.api.model.MethodDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.java.test.assertj.TypeDescriptorCondition;
import org.jqassistant.plugin.jee.ejb.test.set.beans.jakarta.*;
import org.jqassistant.plugin.jee.ejb.test.set.beans.javax.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.assertj.MethodDescriptorCondition.methodDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.assertj.TypeDescriptorCondition.typeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the ejb concepts.
 */
class EjbIT extends AbstractJavaPluginIT {

    /**
     * Verifies the concept "ejb:StatelessSessionBean".
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxStatelessLocalBean.class, JakartaStatelessLocalBean.class})
    public void statelessSessionBean(Class<?> classToScan) throws RuleException {
        scanClasses(classToScan);
        assertThat(applyConcept("ejb:StatelessSessionBean").getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        assertThat(query("MATCH (ejb:Java:Type:Stateless:JEE:EJB) RETURN ejb").<TypeDescriptor>getColumn("ejb"))
                .haveExactly(1, typeDescriptor(classToScan));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "ejb:StatefulSessionBean".
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxStatefulBean.class, JakartaStatefulBean.class})
    public void statefulSessionBean(Class<?> classToScan) throws RuleException {
        scanClasses(classToScan);
        assertThat(applyConcept("ejb:StatefulSessionBean").getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        assertThat(query("MATCH (ejb:Java:Type:Stateful:JEE:EJB) RETURN ejb").<TypeDescriptor>getColumn("ejb"))
                .haveExactly(1, typeDescriptor(classToScan));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "ejb:SingletonBean".
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxSingletonBean.class, JakartaSingletonBean.class})
    void singletonBean(Class<?> classToScan) throws Exception {
        scanClasses(classToScan);
        assertThat(applyConcept("ejb:SingletonBean").getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        assertThat(query("MATCH (ejb:Java:Type:Singleton:JEE:EJB) RETURN ejb").<TypeDescriptor>getColumn("ejb"))
                .haveExactly(1, typeDescriptor(classToScan));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "ejb:MessageDrivenBean".
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxMessageDrivenBean.class, JakartaMessageDrivenBean.class})
    void messageDrivenBean(Class<?> classToScan) throws RuleException {
        scanClasses(classToScan);
        assertThat(applyConcept("ejb:MessageDrivenBean").getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        assertThat(query("MATCH (ejb:Type:Java:MessageDriven:JEE:EJB) RETURN ejb").<TypeDescriptor>getColumn("ejb"))
                .haveExactly(1, typeDescriptor(classToScan));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "ejb:Local".
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxStatelessLocalBean.class, JakartaStatelessLocalBean.class})
    void localSessionBean(Class<?> classToScan) throws RuleException {
        scanClasses(classToScan);
        assertThat(applyConcept("ejb:Local").getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        assertThat(query("MATCH (type:Type:Local) RETURN type").<TypeDescriptor>getColumn("type"))
                .haveExactly(1, typeDescriptor(classToScan));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "ejb:Remote".
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxStatelessRemoteBean.class, JakartaStatelessRemoteBean.class})
    void remoteSessionBean(Class<?> classToScan) throws RuleException {
        scanClasses(classToScan);
        assertThat(applyConcept("ejb:Remote").getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        assertThat(query("MATCH (type:Type:Remote) RETURN type").<TypeDescriptor>getColumn("type"))
                .haveExactly(1, typeDescriptor(classToScan));
        store.commitTransaction();
    }

    /**
     * Verifies the provided concept "ejb:EJB".
     */
    @ParameterizedTest
    @MethodSource({"provideBeansToTest"})
    void providedConceptEjb(Class<?>[] beansToScan, Class<?>[] actualEjbs) throws RuleException {
        // Scan ScheduledBean as well to check if it is correctly NOT identified as an EJB
        scanClasses(beansToScan);

        final Result<Concept> conceptResult = applyConcept("ejb:EJB");
        assertThat(conceptResult.getStatus()).isEqualTo(SUCCESS);

        store.beginTransaction();

        final List<TypeDescriptor> conceptResultTypes = conceptResult.getRows().stream()
                .map(Row::getColumns)
                .map(columns -> columns.get("EJB"))
                .map(Column::getValue)
                .map(TypeDescriptor.class::cast)
                .collect(Collectors.toList());
        assertExactlyAllTestEjbs(conceptResultTypes, actualEjbs);

        final List<TypeDescriptor> allEjbs = query("MATCH (ejb:Java:Type:JEE:EJB) RETURN ejb").getColumn("ejb");
        assertExactlyAllTestEjbs(allEjbs, actualEjbs);
        store.commitTransaction();
    }

    /**
     * Verifies the concept "ejb:Schedule".
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxScheduledBean.class, JakartaScheduledBean.class})
    void scheduleMethod(Class<?> classToScan) throws RuleException, NoSuchMethodException {
        scanClasses(classToScan);
        assertThat(applyConcept("ejb:Schedule").getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        assertThat(query("MATCH (timer:Method:Schedule) RETURN timer").<MethodDescriptor>getColumn("timer"))
                .haveExactly(1, methodDescriptor(classToScan, "invokeTimer"));
        store.commitTransaction();
    }

    @ParameterizedTest
    @MethodSource({"provideBeansToTest"})
    void ejbInjectable(Class<?>[] beansToScan, Class<?>[] actualEjbs) throws RuleException {
        // Scan ScheduledBean as well to check if it is correctly NOT identified as an EJB
        scanClasses(beansToScan);
        final Result<Concept> conceptResult = applyConcept("ejb:Injectable");
        assertThat(conceptResult.getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        final List<TypeDescriptor> conceptResultTypes = conceptResult.getRows().stream()
                .map(Row::getColumns)
                .map(columns -> columns.get("EJB"))
                .map(Column::getValue)
                .map(TypeDescriptor.class::cast)
                .collect(Collectors.toList());
        assertExactlyAllTestEjbs(conceptResultTypes, actualEjbs);

        final List<TypeDescriptor> injectableTypes =
                query("MATCH (injectableType:Java:Type:Injectable) RETURN injectableType").getColumn("injectableType");
        assertExactlyAllTestEjbs(injectableTypes, actualEjbs);
        store.commitTransaction();
    }

    @ParameterizedTest
    @MethodSource({"provideBeansToTest"})
    void providedConceptJeeInjectable(Class<?>[] beansToScan, Class<?>[] actualEjbs) throws RuleException {
        // Scan ScheduledBean as well to check if it is correctly NOT identified as an EJB
        scanClasses(beansToScan);
        final Result<Concept> conceptResult = applyConcept("jee-injection:Injectable");
        assertThat(conceptResult.getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        final List<TypeDescriptor> conceptResultTypes = conceptResult.getRows().stream()
                .map(Row::getColumns)
                .map(columns -> columns.get("Injectable"))
                .map(Column::getValue)
                .map(TypeDescriptor.class::cast)
                .collect(Collectors.toList());
        assertExactlyAllTestEjbs(conceptResultTypes, actualEjbs);

        final List<TypeDescriptor> injectableTypes =
                query("MATCH (injectableType:Java:Type:Injectable) RETURN injectableType").getColumn("injectableType");
        assertExactlyAllTestEjbs(injectableTypes, actualEjbs);
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "ejb:ScheduleMethodInEjbContext" results in no violations when applied to valid beans.
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxScheduledEJB.class, JakartaScheduledEJB.class})
    void scheduleMethodWithoutEjb_No_Violation(Class<?> classToScan) throws RuleException {
        scanClasses(classToScan);
        final String ruleName = "ejb:ScheduleMethodInEjbContext";
        assertThat(validateConstraint(ruleName).getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();

        final List<Result<Constraint>> constraintResults = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintResults).as("There should be exactly 1 constraint result").hasSize(1);
        final Result<Constraint> result = constraintResults.get(0);
        assertThat(result.getRule().getId()).as("Constraint ID should be correct").isEqualTo(ruleName);
        assertThat(result.getRows()).as("There should be no constraint violations").isEmpty();
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "ejb:ScheduleMethodInEjbContext".
     */
    @ParameterizedTest
    @ValueSource(classes = {JavaxScheduledBean.class, JakartaScheduledBean.class})
    void scheduleMethodWithoutEjb(Class<?> classToScan) throws RuleException {
        scanClasses(classToScan);
        final String ruleName = "ejb:ScheduleMethodInEjbContext";
        assertThat(validateConstraint(ruleName).getStatus()).isEqualTo(FAILURE);

        store.beginTransaction();

        final List<Result<Constraint>> constraintResult = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintResult).as("There should be exactly 1 constraint result").hasSize(1);
        final Result<Constraint> result = constraintResult.get(0);
        assertThat(result.getRule().getId()).as("Constraint ID should be correct").isEqualTo(ruleName);

        final List<Row> violations = result.getRows();
        assertThat(violations).as("There should be exactly 1 constraint violation").hasSize(1);
        assertThat(violations.get(0).getColumns().get("invalidBean").getValue()).as("Bean name should be correct").isEqualTo(classToScan.getName());
        assertThat(violations.get(0).getColumns().get("scheduledMethodName").getValue()).as("Method name schuld be correct").isEqualTo("invokeTimer");

        store.commitTransaction();
    }

    private static Stream<Arguments> provideBeansToTest() {
        Class<?>[] scannableJavaxBeans = new Class<?>[] {
            JavaxStatelessLocalBean.class,
            JavaxStatelessRemoteBean.class,
            JavaxStatefulBean.class,
            JavaxMessageDrivenBean.class,
            JavaxSingletonBean.class,
            JavaxScheduledEJB.class,
            JavaxScheduledBean.class
        };

        Class<?>[] actualJavaxEjbs = new Class<?>[] {
            JavaxStatelessLocalBean.class,
            JavaxStatelessRemoteBean.class,
            JavaxStatefulBean.class,
            JavaxMessageDrivenBean.class,
            JavaxSingletonBean.class,
            JavaxScheduledEJB.class
        };

        Class<?>[] scannableJakartaBeans = new Class<?>[] {
            JakartaStatelessLocalBean.class,
            JakartaStatelessRemoteBean.class,
            JakartaStatefulBean.class,
            JakartaMessageDrivenBean.class,
            JakartaSingletonBean.class,
            JakartaScheduledEJB.class,
            JakartaScheduledBean.class
        };

        Class<?>[] actualJakartaEjbs = new Class<?>[] {
            JakartaStatelessLocalBean.class,
            JakartaStatelessRemoteBean.class,
            JakartaStatefulBean.class,
            JakartaMessageDrivenBean.class,
            JakartaSingletonBean.class,
            JakartaScheduledEJB.class
        };

        return Stream.of(
            Arguments.of(scannableJavaxBeans, actualJavaxEjbs),
            Arguments.of(scannableJakartaBeans, actualJakartaEjbs)
        );
    }

    private static void assertExactlyAllTestEjbs(List<TypeDescriptor> actualTypes, Class<?>[] expectedTypes) {
        TypeDescriptorCondition[] expectedTypeDescriptors = Arrays.stream(expectedTypes)
                .map(TypeDescriptorCondition::typeDescriptor)
                .toArray(TypeDescriptorCondition[]::new);
        assertThat(actualTypes).hasSize(6);
        for(TypeDescriptorCondition typeDescriptorCondition : expectedTypeDescriptors) {
            assertThat(actualTypes).haveExactly(1, typeDescriptorCondition);
        }
    }
}
