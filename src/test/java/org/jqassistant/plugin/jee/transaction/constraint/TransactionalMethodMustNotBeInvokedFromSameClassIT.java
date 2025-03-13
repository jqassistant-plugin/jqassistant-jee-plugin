package org.jqassistant.plugin.jee.transaction.constraint;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Row;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.plugin.java.api.model.MethodDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import org.jqassistant.plugin.jee.transaction.set.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static com.buschmais.jqassistant.plugin.java.test.assertj.MethodDescriptorCondition.methodDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.assertj.TypeDescriptorCondition.typeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.FAILURE;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

class TransactionalMethodMustNotBeInvokedFromSameClassIT extends AbstractJavaPluginIT {

    @ParameterizedTest
    @ValueSource(classes = {JavaxTransactionalMethod.class, JakartaTransactionalMethod.class,
            JakartaTransactionalClass.class, JavaxTransactionalClass.class, MessageDrivenEjb.class,
            SingletonEjb.class, StatefulEjb.class, StatelessEjb.class})
    void transactionMethodsMustNotBeCalledDirectlyWithViolations(Class<?> clazz) throws Exception {
        scanClasses(clazz);
        assertThat(validateConstraint("jee-transaction:TransactionalMethodMustNotBeInvokedFromSameClass")
                .getStatus()).isEqualTo(FAILURE);

        store.beginTransaction();

        final List<Result<Constraint>> constraintViolations =
                new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations).hasSize(1);

        assertThat(constraintViolations.get(0).getRule().getId())
                .isEqualTo("jee-transaction:TransactionalMethodMustNotBeInvokedFromSameClass");

        final Result<Constraint> result = constraintViolations.get(0);
        assertThat(result.getRows()).hasSize(1);
        final Row row = result.getRows().get(0);
        assertThat(row.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(clazz));
        assertThat(row.getColumns().get("Method").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "callingTransactional"));
        assertThat(row.getColumns().get("TransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "transactionalMethod"));

        store.commitTransaction();
    }

}
