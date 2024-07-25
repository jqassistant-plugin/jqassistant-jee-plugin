package org.jqassistant.plugin.jee.ejb3.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Row;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.jqassistant.plugin.jee.ejb3.test.set.beans.*;
import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.FAILURE;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.core.test.matcher.ConstraintMatcher.constraint;
import static com.buschmais.jqassistant.core.test.matcher.ResultMatcher.result;
import static com.buschmais.jqassistant.plugin.java.test.matcher.MethodDescriptorMatcher.methodDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

/**
 * Tests for the EJB3 concepts.
 */
class Ejb3IT extends AbstractJavaPluginIT {

    /**
     * Verifies the concept "ejb3:StatelessSessionBean".
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    public void statelessSessionBean() throws RuleException {
        scanClasses(StatelessLocalBean.class);
        assertThat(applyConcept("ejb3:StatelessSessionBean").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (ejb:Type:Stateless:Ejb) RETURN ejb").getColumn("ejb"), hasItem(typeDescriptor(StatelessLocalBean.class)));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "ejb3:StatefulSessionBean".
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    public void statefulSessionBean() throws RuleException {
        scanClasses(StatefulBean.class);
        assertThat(applyConcept("ejb3:StatefulSessionBean").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (ejb:Type:Stateful:Ejb) RETURN ejb").getColumn("ejb"), hasItem(typeDescriptor(StatefulBean.class)));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "ejb3:SingletonBean".
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void singletonBean() throws Exception {
        scanClasses(SingletonBean.class);
        assertThat(applyConcept("ejb3:SingletonBean").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (ejb:Type:Singleton:Ejb) RETURN ejb").getColumn("ejb"), hasItem(typeDescriptor(SingletonBean.class)));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "ejb3:MessageDrivenBean".
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void messageDrivenBean() throws RuleException {
        scanClasses(MessageDrivenBean.class);
        assertThat(applyConcept("ejb3:MessageDrivenBean").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (ejb:Type:MessageDriven:Ejb) RETURN ejb").getColumn("ejb"), hasItem(typeDescriptor(MessageDrivenBean.class)));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "ejb3:Local".
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void localSessionBean() throws RuleException {
        scanClasses(StatelessLocalBean.class);
        assertThat(applyConcept("ejb3:Local").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (ejb:Type:Local:Ejb) RETURN ejb").getColumn("ejb"), hasItem(typeDescriptor(StatelessLocalBean.class)));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "ejb3:Remote".
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void remoteSessionBean() throws RuleException {
        scanClasses(StatelessRemoteBean.class);
        assertThat(applyConcept("ejb3:Remote").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (ejb:Type:Remote:Ejb) RETURN ejb").getColumn("ejb"), hasItem(typeDescriptor(StatelessRemoteBean.class)));
        store.commitTransaction();
    }

    /**
     * Verifies the analysis group "ejb3:EnterpriseJavaBean".
     *
     */
    @Test
    void enterpriseJavaBean() throws RuleException {
        scanClasses(StatelessLocalBean.class, StatelessRemoteBean.class, StatefulBean.class, MessageDrivenBean.class);
        executeGroup("ejb3:EJB");
        store.beginTransaction();
        assertThat(query("MATCH (ejb:Type:Ejb) RETURN ejb").getColumn("ejb"), hasItems(typeDescriptor(StatelessLocalBean.class),
                typeDescriptor(StatelessRemoteBean.class), typeDescriptor(StatefulBean.class), typeDescriptor(MessageDrivenBean.class)));
        assertThat(query("MATCH (ejb:Type:Ejb:Local) RETURN ejb").getColumn("ejb"), hasItem(typeDescriptor(StatelessLocalBean.class)));
        assertThat(query("MATCH (ejb:Type:Ejb:Remote) RETURN ejb").getColumn("ejb"), hasItem(typeDescriptor(StatelessRemoteBean.class)));
        store.commitTransaction();
    }

    /**
     * Verifies the concept "ejb3:Schedule".
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void scheduleMethod() throws RuleException, NoSuchMethodException {
        scanClasses(ScheduledBean.class);
        assertThat(applyConcept("ejb3:Schedule").getStatus(), equalTo(SUCCESS));
        store.beginTransaction();
        assertThat(query("MATCH (timer:Method:Schedule) RETURN timer").getColumn("timer"), hasItem(methodDescriptor(ScheduledBean.class, "invokeTimer")));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "ejb3:ScheduleMethodInEjbContext" results in no
     * violations when applied to valid beans.
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void scheduleMethodWithoutEjb_No_Violation() throws RuleException {
        scanClasses(ScheduledEJB.class);
        final String ruleName = "ejb3:ScheduleMethodInEjbContext";
        assertThat(validateConstraint(ruleName).getStatus(), equalTo(SUCCESS));
        store.beginTransaction();

        final List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat("Unexpected number of violated constraints", constraintViolations.size(), equalTo(1));
        final Result<Constraint> result = constraintViolations.get(0);
        assertThat("Expected constraint " + ruleName, result, result(constraint(ruleName)));
        final List<Row> violatedBeans = result.getRows();
        assertThat("Unexpected number of violations", violatedBeans.size(), equalTo(0));

        store.commitTransaction();
    }

    /**
     * Verifies the constraint "ejb3:ScheduleMethodInEjbContext".
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void scheduleMethodWithoutEjb() throws RuleException {
        scanClasses(ScheduledBean.class);
        final String ruleName = "ejb3:ScheduleMethodInEjbContext";
        assertThat(validateConstraint(ruleName).getStatus(), equalTo(FAILURE));
        store.beginTransaction();

        final List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat("Unexpected number of violated constraints", constraintViolations.size(), equalTo(1));
        final Result<Constraint> result = constraintViolations.get(0);
        assertThat("Expected constraint " + ruleName, result, result(constraint(ruleName)));

        final List<Row> violations = result.getRows();
        assertThat("Unexpected number of violations", violations, hasSize(1));
        assertThat("Unexpected bean name", ScheduledBean.class.getName(), equalTo(violations.get(0).getColumns().get("invalidBean").getValue()));
        assertThat("Unexpected method name", "invokeTimer", equalTo(violations.get(0).getColumns().get("scheduledMethodName").getValue()));

        store.commitTransaction();
    }
}
