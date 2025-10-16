package org.jqassistant.plugin.jee.transaction.concept;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Row;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.plugin.java.api.model.MethodDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
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

        final Result<Concept> conceptResult = applyConcept("jee-transaction:TransactionPropagation");
        store.beginTransaction();

        assertThat(conceptResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        assertThat(conceptResult.getRows().size()).isEqualTo(1);

        Row row1  = conceptResult.getRows().get(0);
        assertThat((TypeDescriptor) row1.getColumns().get("TransactionalType").getValue())
                .is(typeDescriptor(testClass));
        assertThat((MethodDescriptor) row1.getColumns().get("TransactionalMethod").getValue())
                .is(methodDescriptor(testClass, "transactionalMethod"));
        assertThat(row1.getColumns().get("TransactionPropagation").getLabel()).isEqualTo("REQUIRED");

        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = {JavaxTransactionPropagatingEjb.class, JakartaTransactionPropagatingEjb.class})
    public void transactionPropagationEjb(Class<?> testClass) throws RuleException, NoSuchMethodException {
        scanClasses(testClass);

        final Result<Concept> conceptResult = applyConcept("jee-transaction:TransactionPropagation");
        store.beginTransaction();

        assertThat(conceptResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        assertThat(conceptResult.getRows().size()).isEqualTo(1);

        Row row1  = conceptResult.getRows().get(0);
        assertThat((TypeDescriptor) row1.getColumns().get("TransactionalType").getValue())
                .is(typeDescriptor(testClass));
        assertThat((MethodDescriptor) row1.getColumns().get("TransactionalMethod").getValue())
                .is(methodDescriptor(testClass, "transactionalMethodMandatory"));
        assertThat(row1.getColumns().get("TransactionPropagation").getLabel()).isEqualTo("MANDATORY");

        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = {JavaxTransactionPropagatingType.class, JakartaTransactionPropagatingType.class})
    public void transactionPropagationType(Class<?> testClass) throws RuleException, NoSuchMethodException {
        scanClasses(testClass);

        final Result<Concept> conceptResult = applyConcept("jee-transaction:TransactionPropagation");
        store.beginTransaction();

        assertThat(conceptResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        assertThat(conceptResult.getRows().size()).isEqualTo(3);

        Row row1  = conceptResult.getRows().get(0);
        assertThat((TypeDescriptor) row1.getColumns().get("TransactionalType").getValue())
                .is(typeDescriptor(testClass));
        assertThat((MethodDescriptor) row1.getColumns().get("TransactionalMethod").getValue())
                .is(methodDescriptor(testClass, "transactionalMethodMandatory"));
        assertThat(row1.getColumns().get("TransactionPropagation").getLabel()).isEqualTo("MANDATORY");

        Row row2  = conceptResult.getRows().get(1);
        assertThat((TypeDescriptor) row2.getColumns().get("TransactionalType").getValue())
                .is(typeDescriptor(testClass));
        assertThat((MethodDescriptor) row2.getColumns().get("TransactionalMethod").getValue())
                .is(methodDescriptor(testClass, "transactionalMethodNever"));
        assertThat(row2.getColumns().get("TransactionPropagation").getLabel()).isEqualTo("NEVER");

        Row row3  = conceptResult.getRows().get(2);
        assertThat((TypeDescriptor) row3.getColumns().get("TransactionalType").getValue())
                .is(typeDescriptor(testClass));
        assertThat((MethodDescriptor) row3.getColumns().get("TransactionalMethod").getValue())
                .is(methodDescriptor(testClass, "transactionalMethodRequiresNew"));
        assertThat(row3.getColumns().get("TransactionPropagation").getLabel()).isEqualTo("REQUIRES_NEW");

        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = {JavaxTransactionPropagatingMethod.class, JakartaTransactionPropagatingMethod.class})
    public void transactionPropagationMethod(Class<?> testClass) throws RuleException, NoSuchMethodException {
        scanClasses(testClass);

        final Result<Concept> conceptResult = applyConcept("jee-transaction:TransactionPropagation");
        store.beginTransaction();

        assertThat(conceptResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        assertThat(conceptResult.getRows().size()).isEqualTo(2);

        Row row1  = conceptResult.getRows().get(0);
        assertThat((TypeDescriptor) row1.getColumns().get("TransactionalType").getValue())
                .is(typeDescriptor(testClass));
        assertThat((MethodDescriptor) row1.getColumns().get("TransactionalMethod").getValue())
                .is(methodDescriptor(testClass, "transactionalMethodRequired"));
        assertThat(row1.getColumns().get("TransactionPropagation").getLabel()).isEqualTo("REQUIRED");

        Row row2  = conceptResult.getRows().get(1);
        assertThat((TypeDescriptor) row2.getColumns().get("TransactionalType").getValue())
                .is(typeDescriptor(testClass));
        assertThat((MethodDescriptor) row2.getColumns().get("TransactionalMethod").getValue())
                .is(methodDescriptor(testClass, "transactionalMethodRequiresNew"));
        assertThat(row2.getColumns().get("TransactionPropagation").getLabel()).isEqualTo("REQUIRES_NEW");

        store.commitTransaction();
    }
}
