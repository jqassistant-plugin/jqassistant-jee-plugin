package org.jqassistant.plugin.jee.transaction.concept;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Row;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.plugin.java.api.model.MethodDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import com.buschmais.jqassistant.plugin.java.test.assertj.MethodDescriptorCondition;
import com.buschmais.jqassistant.plugin.java.test.assertj.TypeDescriptorCondition;
import org.jqassistant.plugin.jee.transaction.set.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.buschmais.jqassistant.plugin.java.test.assertj.MethodDescriptorCondition.methodDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.assertj.TypeDescriptorCondition.typeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionPropagationIT extends AbstractJavaPluginIT {

    @ParameterizedTest
    @ValueSource(classes = {JavaxStatelessEjb.class, JakartaStatelessEjb.class})
    public void transactionPropagationEjbDefault(Class<?> testClass) throws RuleException, NoSuchMethodException {
        scanClasses(testClass);

        final Result<Concept> conceptResult = applyConcept("jee-transaction:TransactionalMethod");
        store.beginTransaction();

        assertThat(conceptResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        assertThat(conceptResult.getRows().size()).isEqualTo(5);

        Row row1  = conceptResult.getRows().get(0);
        assertTransactionalMethodWithTxSemanticsAndAdditionalConfiguration(row1, typeDescriptor(testClass), methodDescriptor(testClass, "anotherTransactionalMethodWithRequiredSemantics"), "REQUIRED", false);

        Row row2  = conceptResult.getRows().get(1);
        assertTransactionalMethodWithTxSemanticsAndAdditionalConfiguration(row2, typeDescriptor(testClass), methodDescriptor(testClass, "neverTransactionalCallingRequiredTransactionalTransitively"), "NEVER", false);

        Row row3  = conceptResult.getRows().get(2);
        assertTransactionalMethodWithTxSemanticsAndAdditionalConfiguration(row3, typeDescriptor(testClass), methodDescriptor(testClass, "requiredTransactionalCallingRequiredTransactionalTransitively"), "REQUIRED", false);

        Row row4  = conceptResult.getRows().get(3);
        assertTransactionalMethodWithTxSemanticsAndAdditionalConfiguration(row4, typeDescriptor(testClass), methodDescriptor(testClass, "transactionalMethodWithNeverSemantics"), "NEVER", false);

        Row row5  = conceptResult.getRows().get(4);
        assertTransactionalMethodWithTxSemanticsAndAdditionalConfiguration(row5, typeDescriptor(testClass), methodDescriptor(testClass, "transactionalMethodWithRequiredSemantics"), "REQUIRED", false);

        store.commitTransaction();
    }



    @ParameterizedTest
    @ValueSource(classes = {JavaxEjbTypeAndMethodLevelTransactionPropagation.class, JakartaEjbTypeAndMethodLevelTransactionPropagation.class})
    public void transactionPropagationEjbTypeAndMethodLevel(Class<?> testClass) throws RuleException, NoSuchMethodException {
        scanClasses(testClass);

        final Result<Concept> conceptResult = applyConcept("jee-transaction:TransactionalMethod");
        store.beginTransaction();

        assertThat(conceptResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        assertThat(conceptResult.getRows().size()).isEqualTo(3);

        Row row1  = conceptResult.getRows().get(0);
        assertTransactionalMethodWithTxSemanticsAndAdditionalConfiguration(row1, typeDescriptor(testClass), methodDescriptor(testClass, "transactionalMethodMandatory"), "MANDATORY", false);

        Row row2  = conceptResult.getRows().get(1);
        assertTransactionalMethodWithTxSemanticsAndAdditionalConfiguration(row2, typeDescriptor(testClass), methodDescriptor(testClass, "transactionalMethodNever"), "NEVER", false);

        Row row3  = conceptResult.getRows().get(2);
        assertTransactionalMethodWithTxSemanticsAndAdditionalConfiguration(row3, typeDescriptor(testClass), methodDescriptor(testClass, "transactionalMethodRequired"), "REQUIRED", false);

        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = {JavaxEjbMethodLevelTransactionPropagation.class, JakartaEjbMethodLevelTransactionPropagation.class})
    public void transactionPropagationEjbMethodLevel(Class<?> testClass) throws RuleException, NoSuchMethodException {
        scanClasses(testClass);

        final Result<Concept> conceptResult = applyConcept("jee-transaction:TransactionalMethod");
        store.beginTransaction();

        assertThat(conceptResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        assertThat(conceptResult.getRows().size()).isEqualTo(1);

        Row row1  = conceptResult.getRows().get(0);
        assertTransactionalMethodWithTxSemanticsAndAdditionalConfiguration(row1, typeDescriptor(testClass), methodDescriptor(testClass, "transactionalMethodMandatory"), "MANDATORY", false);

        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = {JavaxTypeAndMethodLevelTransactionPropagation.class, JakartaTypeAndMethodLevelTransactionPropagation.class})
    public void transactionPropagationType(Class<?> testClass) throws RuleException, NoSuchMethodException {
        scanClasses(testClass);

        final Result<Concept> conceptResult = applyConcept("jee-transaction:TransactionalMethod");
        store.beginTransaction();

        assertThat(conceptResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        assertThat(conceptResult.getRows().size()).isEqualTo(4);

        Row row1  = conceptResult.getRows().get(0);
        assertTransactionalMethodWithTxSemanticsAndAdditionalConfiguration(row1, typeDescriptor(testClass), methodDescriptor(testClass, "transactionalMethodMandatoryAndRollbackOnException"), "MANDATORY", true);

        Row row2  = conceptResult.getRows().get(1);
        assertTransactionalMethodWithTxSemanticsAndAdditionalConfiguration(row2, typeDescriptor(testClass), methodDescriptor(testClass, "transactionalMethodNeverAndRollbackOnRuntimeException"), "NEVER", true);

        Row row3  = conceptResult.getRows().get(2);
        assertTransactionalMethodWithTxSemanticsAndAdditionalConfiguration(row3, typeDescriptor(testClass), methodDescriptor(testClass, "transactionalMethodRequiredAndRollbackOnRuntimeException"), "REQUIRED", true);

        Row row4  = conceptResult.getRows().get(3);
        assertTransactionalMethodWithTxSemanticsAndAdditionalConfiguration(row4, typeDescriptor(testClass), methodDescriptor(testClass, "transactionalMethodRequiresNewAndDefaultRollback"), "REQUIRES_NEW", false);

        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = {JavaxMethodLevelTransactionPropagation.class, JakartaMethodLevelTransactionPropagation.class})
    public void transactionPropagationMethod(Class<?> testClass) throws RuleException, NoSuchMethodException {
        scanClasses(testClass);

        final Result<Concept> conceptResult = applyConcept("jee-transaction:TransactionalMethod");
        store.beginTransaction();

        assertThat(conceptResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        assertThat(conceptResult.getRows().size()).isEqualTo(4);

        Row row1 = conceptResult.getRows().get(0);
        assertTransactionalMethodWithTxSemanticsAndAdditionalConfiguration(row1, typeDescriptor(testClass), methodDescriptor(testClass, "transactionalMethodNeverAndRollbackOnRuntimeException"), "NEVER", true);

        Row row2 = conceptResult.getRows().get(1);
        assertTransactionalMethodWithTxSemanticsAndAdditionalConfiguration(row2, typeDescriptor(testClass), methodDescriptor(testClass, "transactionalMethodRequiredAndDefaultRollback"), "REQUIRED", false);

        Row row3 = conceptResult.getRows().get(2);
        assertTransactionalMethodWithTxSemanticsAndAdditionalConfiguration(row3, typeDescriptor(testClass), methodDescriptor(testClass, "transactionalMethodRequiredAndRollbackOnRuntimeException"), "REQUIRED", true);

        Row row4 = conceptResult.getRows().get(3);
        assertTransactionalMethodWithTxSemanticsAndAdditionalConfiguration(row4, typeDescriptor(testClass), methodDescriptor(testClass, "transactionalMethodRequiresNewAndDefaultRollback"), "REQUIRES_NEW", false);

        store.commitTransaction();
    }

    private void assertTransactionalMethodWithTxSemanticsAndAdditionalConfiguration(Row row, TypeDescriptorCondition typeDescriptorCondition, MethodDescriptorCondition methodDescriptorCondition, String transactionPropagation, boolean withAdditionalConfiguration) {
        assertThat((TypeDescriptor) row.getColumns().get("Type").getValue()).is(typeDescriptorCondition);
        assertThat((MethodDescriptor) row.getColumns().get("TransactionalMethod").getValue()).is(methodDescriptorCondition);
        assertThat(row.getColumns().get("TransactionPropagation").getLabel()).isEqualTo(transactionPropagation);
        assertThat(row.getColumns().get("AdditionalTransactionConfiguration").getLabel()).isEqualTo(String.valueOf(withAdditionalConfiguration));
    }
}
