package org.jqassistant.plugin.jee.ejb.test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.buschmais.jqassistant.core.report.api.model.Column;
import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Row;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.plugin.java.api.model.MethodDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.jqassistant.plugin.jee.ejb.test.set.beans.*;
import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.assertj.MethodDescriptorCondition.methodDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.assertj.TypeDescriptorCondition.typeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the ejb concepts.
 */
class ejbIT extends AbstractJavaPluginIT {

    /**
     * Verifies the concept "ejb:StatelessSessionBean".
     */
    @Test
    public void statelessSessionBean() throws RuleException {
        scanClasses(StatelessLocalBean.class);
        assertThat(applyConcept("ejb:StatelessSessionBean").getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        assertThat(query("MATCH (ejb:Type:Stateless:EJB) RETURN ejb").<TypeDescriptor>getColumn("ejb"))
                .haveExactly(1, typeDescriptor(StatelessLocalBean.class));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "ejb:StatefulSessionBean".
     */
    @Test
    public void statefulSessionBean() throws RuleException {
        scanClasses(StatefulBean.class);
        assertThat(applyConcept("ejb:StatefulSessionBean").getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        assertThat(query("MATCH (ejb:Type:Stateful:EJB) RETURN ejb").<TypeDescriptor>getColumn("ejb"))
                .haveExactly(1, typeDescriptor(StatefulBean.class));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "ejb:SingletonBean".
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void singletonBean() throws Exception {
        scanClasses(SingletonBean.class);
        assertThat(applyConcept("ejb:SingletonBean").getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        assertThat(query("MATCH (ejb:Type:Singleton:EJB) RETURN ejb").<TypeDescriptor>getColumn("ejb"))
                .haveExactly(1, typeDescriptor(SingletonBean.class));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "ejb:MessageDrivenBean".
     */
    @Test
    void messageDrivenBean() throws RuleException {
        scanClasses(MessageDrivenBean.class);
        assertThat(applyConcept("ejb:MessageDrivenBean").getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        assertThat(query("MATCH (ejb:Type:MessageDriven:EJB) RETURN ejb").<TypeDescriptor>getColumn("ejb"))
                .haveExactly(1, typeDescriptor(MessageDrivenBean.class));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "ejb:Local".
     */
    @Test
    void localSessionBean() throws RuleException {
        scanClasses(StatelessLocalBean.class);
        assertThat(applyConcept("ejb:Local").getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        assertThat(query("MATCH (type:Type:Local) RETURN type").<TypeDescriptor>getColumn("type"))
                .haveExactly(1, typeDescriptor(StatelessLocalBean.class));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "ejb:Remote".
     */
    @Test
    void remoteSessionBean() throws RuleException {
        scanClasses(StatelessRemoteBean.class);
        assertThat(applyConcept("ejb:Remote").getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        assertThat(query("MATCH (type:Type:Remote) RETURN type").<TypeDescriptor>getColumn("type"))
                .haveExactly(1, typeDescriptor(StatelessRemoteBean.class));
        store.commitTransaction();
    }

    /**
     * Verifies the analysis group "ejb:EnterpriseJavaBean".
     *
     */
    @Test
    void enterpriseJavaBean() throws RuleException {
        scanClasses(StatelessLocalBean.class, StatelessRemoteBean.class, StatefulBean.class, MessageDrivenBean.class, SingletonBean.class);
        executeGroup("ejb:EJB");
        store.beginTransaction();

        final List<TypeDescriptor> allEjbs = query("MATCH (ejb:Type:EJB) RETURN ejb").getColumn("ejb");
        assertThat(allEjbs).hasSize(5);
        assertThat(allEjbs).haveExactly(1, typeDescriptor(StatelessLocalBean.class));
        assertThat(allEjbs).haveExactly(1, typeDescriptor(MessageDrivenBean.class));
        assertThat(allEjbs).haveExactly(1, typeDescriptor(StatelessLocalBean.class));
        assertThat(allEjbs).haveExactly(1, typeDescriptor(StatelessRemoteBean.class));
        assertThat(allEjbs).haveExactly(1, typeDescriptor(SingletonBean.class));

        assertThat(query("MATCH (type:Type:EJB:Local) RETURN type").<TypeDescriptor>getColumn("type")).haveExactly(1, typeDescriptor(StatelessLocalBean.class));
        assertThat(query("MATCH (type:Type:EJB:Remote) RETURN type").<TypeDescriptor>getColumn("type")).haveExactly(1, typeDescriptor(StatelessRemoteBean.class));
        store.commitTransaction();
    }

    /**
     * Verifies the provided concept "ejb:EJB".
     */
    @Test
    void providedConceptEjb() throws RuleException {
        scanClasses(StatelessLocalBean.class, StatelessRemoteBean.class, StatefulBean.class, MessageDrivenBean.class, SingletonBean.class);

        final Result<Concept> conceptResult = applyConcept("ejb:EJB");
        assertThat(conceptResult.getStatus()).isEqualTo(SUCCESS);

        store.beginTransaction();

        final List<TypeDescriptor> conceptResultTypes = conceptResult.getRows().stream()
                .map(Row::getColumns)
                .map(columns -> columns.get("EJB"))
                .map(Column::getValue)
                .map(TypeDescriptor.class::cast)
                .collect(Collectors.toList());

        assertThat(conceptResultTypes).hasSize(5);
        assertThat(conceptResultTypes).haveExactly(1, typeDescriptor(StatelessLocalBean.class));
        assertThat(conceptResultTypes).haveExactly(1, typeDescriptor(MessageDrivenBean.class));
        assertThat(conceptResultTypes).haveExactly(1, typeDescriptor(StatelessLocalBean.class));
        assertThat(conceptResultTypes).haveExactly(1, typeDescriptor(StatelessRemoteBean.class));
        assertThat(conceptResultTypes).haveExactly(1, typeDescriptor(SingletonBean.class));

        final List<TypeDescriptor> allEjbs = query("MATCH (ejb:Type:EJB) RETURN ejb").getColumn("ejb");
        assertThat(allEjbs).hasSize(5);
        assertThat(allEjbs).haveExactly(1, typeDescriptor(StatelessLocalBean.class));
        assertThat(allEjbs).haveExactly(1, typeDescriptor(MessageDrivenBean.class));
        assertThat(allEjbs).haveExactly(1, typeDescriptor(StatelessLocalBean.class));
        assertThat(allEjbs).haveExactly(1, typeDescriptor(StatelessRemoteBean.class));
        assertThat(allEjbs).haveExactly(1, typeDescriptor(SingletonBean.class));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "ejb:Schedule".
     */
    @Test
    void scheduleMethod() throws RuleException, NoSuchMethodException {
        scanClasses(ScheduledBean.class);
        assertThat(applyConcept("ejb:Schedule").getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        assertThat(query("MATCH (timer:Method:Schedule) RETURN timer").<MethodDescriptor>getColumn("timer"))
                .haveExactly(1, methodDescriptor(ScheduledBean.class, "invokeTimer"));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "ejb:ScheduleMethodInEjbContext" results in no violations when applied to valid beans.
     */
    @Test
    void scheduleMethodWithoutEjb_No_Violation() throws RuleException {
        scanClasses(ScheduledEJB.class);
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
    @Test
    void scheduleMethodWithoutEjb() throws RuleException {
        scanClasses(ScheduledBean.class);
        final String ruleName = "ejb:ScheduleMethodInEjbContext";
        assertThat(validateConstraint(ruleName).getStatus()).isEqualTo(FAILURE);

        store.beginTransaction();

        final List<Result<Constraint>> constraintResult = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintResult).as("There should be exactly 1 constraint result").hasSize(1);
        final Result<Constraint> result = constraintResult.get(0);
        assertThat(result.getRule().getId()).as("Constraint ID should be correct").isEqualTo(ruleName);

        final List<Row> violations = result.getRows();
        assertThat(violations).as("There should be exactly 1 constraint violation").hasSize(1);
        assertThat(violations.get(0).getColumns().get("invalidBean").getValue()).as("Bean name should be correct").isEqualTo(ScheduledBean.class.getName());
        assertThat(violations.get(0).getColumns().get("scheduledMethodName").getValue()).as("Method name schuld be correct").isEqualTo("invokeTimer");

        store.commitTransaction();
    }
}
