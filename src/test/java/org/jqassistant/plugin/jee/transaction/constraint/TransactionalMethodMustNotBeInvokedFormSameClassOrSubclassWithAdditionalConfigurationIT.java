package org.jqassistant.plugin.jee.transaction.constraint;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Row;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.plugin.java.api.model.MethodDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import org.jqassistant.plugin.jee.transaction.set.JakartaTransactionalClassWithAdditionalConfiguration;
import org.jqassistant.plugin.jee.transaction.set.JakartaTransactionalClassWithAdditionalConfigurationAndNestedClass;
import org.jqassistant.plugin.jee.transaction.set.JakartaTransactionalMethodWithAdditionalConfiguration;
import org.jqassistant.plugin.jee.transaction.set.JavaxTransactionalClassWithAdditionalConfiguration;
import org.jqassistant.plugin.jee.transaction.set.JavaxTransactionalClassWithAdditionalConfigurationAndNestedClass;
import org.jqassistant.plugin.jee.transaction.set.JavaxTransactionalMethodWithAdditionalConfiguration;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.config_attribute.jakarta.JakartaCallingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.config_attribute.jakarta.JakartaGenericClassWithTransactionalMethodAndAdditionalConfig;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.config_attribute.jakarta.JakartaOverridingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.config_attribute.javax.JavaxCallingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.config_attribute.javax.JavaxGenericClassWithTransactionalMethodAndAdditionalConfig;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.config_attribute.javax.JavaxOverridingSubClassOfTransactionalClassWithTransactionalMethodAndAdditionalConfig;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.config_attribute.jakarta.JakartaCallingSubClassOfSimpleTransactionClassWithConfig;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.config_attribute.jakarta.JakartaOverridingSubClassOfSimpleTransactionalClassWithConfig;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.config_attribute.jakarta.JakartaSimpleTransactionalClassWithConfig;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.config_attribute.javax.JavaxCallingSubClassOfSimpleTransactionClassWithConfig;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.config_attribute.javax.JavaxOverridingSubClassOfSimpleTransactionalClassWithConfig;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.config_attribute.javax.JavaxSimpleTransactionalClassWithConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.FAILURE;
import static com.buschmais.jqassistant.plugin.java.test.assertj.MethodDescriptorCondition.methodDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.assertj.TypeDescriptorCondition.typeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.jqassistant.plugin.jee.SimpleMethodDescriptorCondition.simpleMethodDescriptor;

public class TransactionalMethodMustNotBeInvokedFormSameClassOrSubclassWithAdditionalConfigurationIT extends AbstractJavaPluginIT {

    @ParameterizedTest
    @ValueSource(classes = {JavaxTransactionalMethodWithAdditionalConfiguration.class, JakartaTransactionalMethodWithAdditionalConfiguration.class,
            JavaxTransactionalClassWithAdditionalConfiguration.class, JakartaTransactionalClassWithAdditionalConfiguration.class})
    void transactionMethodsWithAdditionalConfigurationMustNotBeCalledDirectlyWithViolations(Class<?> clazz) throws Exception {
        scanClasses(clazz);
        assertThat(validateConstraint("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass")
                .getStatus()).isEqualTo(FAILURE);

        store.beginTransaction();

        final List<Result<Constraint>> constraintViolations =
                new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations).hasSize(1);

        assertThat(constraintViolations.get(0).getRule().getId())
                .isEqualTo("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass");

        final Result<Constraint> result = constraintViolations.get(0);
        assertThat(result.getRows()).hasSize(3);

        final Row row1 = result.getRows().get(0);
        assertThat(row1.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(clazz));
        assertThat(row1.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "requiredTransactionalCallingMethodWithAdditionalConfigurationTransitively"));
        assertThat(row1.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(row1.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "transactionalMethodWithNeverSemanticsAndRollbackOnException"));
        assertThat(row1.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(row1.getColumns().get("TargetMethodSetsAdditionalConfiguration").getLabel()).isEqualTo("true");

        final Row row2 = result.getRows().get(1);
        assertThat(row2.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(clazz));
        assertThat(row2.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "transactionalMethodWithRequiredSemanticsCallingMethodsWithAdditionalConfiguration"));
        assertThat(row2.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(row2.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "transactionalMethodWithNeverSemanticsAndRollbackOnException"));
        assertThat(row2.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(row2.getColumns().get("TargetMethodSetsAdditionalConfiguration").getLabel()).isEqualTo("true");

        final Row row3 = result.getRows().get(2);
        assertThat(row3.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(clazz));
        assertThat(row3.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "transactionalMethodWithRequiredSemanticsCallingMethodsWithAdditionalConfiguration"));
        assertThat(row3.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(row3.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "transactionalMethodWithRequiredSemanticsAndRollbackOnException"));
        assertThat(row3.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(row3.getColumns().get("TargetMethodSetsAdditionalConfiguration").getLabel()).isEqualTo("true");

        store.commitTransaction();
    }

    @Test
    void transactionalMethodMustNotBeInvokedFromSameClassSimpleInheritanceJakarta() throws Exception {
        scanClasses(JakartaSimpleTransactionalClassWithConfig.class, JakartaCallingSubClassOfSimpleTransactionClassWithConfig.class,
                JakartaOverridingSubClassOfSimpleTransactionalClassWithConfig.class);
        assertThat(validateConstraint("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass").getStatus()).isEqualTo(FAILURE);
        store.beginTransaction();
        final List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations).hasSize(1);
        final Result<Constraint> result = constraintViolations.get(0);
        assertThat(result.getRule().getId()).isEqualTo("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass");
        assertThat(result.getRows()).hasSize(2);
        final Map<String, Row> resultMap = result.getRows().stream().collect(Collectors.toMap(row -> (row.getColumns().get("Type").getLabel()), Function.identity()));

        final Row callingSubClassOfSimpleTransactionalClassWithConfigResult = resultMap.get(JakartaCallingSubClassOfSimpleTransactionClassWithConfig.class.getName());
        assertThat(callingSubClassOfSimpleTransactionalClassWithConfigResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaCallingSubClassOfSimpleTransactionClassWithConfig.class));
        assertThat(callingSubClassOfSimpleTransactionalClassWithConfigResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaCallingSubClassOfSimpleTransactionClassWithConfig.class, "transactionalMethodWithAdditionalConfiguration"));
        assertThat(callingSubClassOfSimpleTransactionalClassWithConfigResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(callingSubClassOfSimpleTransactionalClassWithConfigResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JakartaCallingSubClassOfSimpleTransactionClassWithConfig.class, "void methodWithAdditionalConfigurationAttribute()"));
        assertThat(callingSubClassOfSimpleTransactionalClassWithConfigResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(callingSubClassOfSimpleTransactionalClassWithConfigResult.getColumns().get("TargetMethodSetsAdditionalConfiguration").getLabel()).isEqualTo("true");

        final Row overridingSubClassOfSimpleTransactionalClassWithAdditionalConfigResult = resultMap.get(JakartaOverridingSubClassOfSimpleTransactionalClassWithConfig.class.getName());
        assertThat(overridingSubClassOfSimpleTransactionalClassWithAdditionalConfigResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaOverridingSubClassOfSimpleTransactionalClassWithConfig.class));
        assertThat(overridingSubClassOfSimpleTransactionalClassWithAdditionalConfigResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaOverridingSubClassOfSimpleTransactionalClassWithConfig.class, "methodWithOverriddenConfigurationAttribute"));
        assertThat(overridingSubClassOfSimpleTransactionalClassWithAdditionalConfigResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(overridingSubClassOfSimpleTransactionalClassWithAdditionalConfigResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaSimpleTransactionalClassWithConfig.class, "methodWithOverriddenConfigurationAttribute"));
        assertThat(overridingSubClassOfSimpleTransactionalClassWithAdditionalConfigResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(overridingSubClassOfSimpleTransactionalClassWithAdditionalConfigResult.getColumns().get("TargetMethodSetsAdditionalConfiguration").getLabel()).isEqualTo("true");

        store.commitTransaction();
    }

    @Test
    void transactionalMethodMustNotBeInvokedFromSameClassSimpleInheritanceJavax() throws Exception {
        scanClasses(JavaxSimpleTransactionalClassWithConfig.class, JavaxCallingSubClassOfSimpleTransactionClassWithConfig.class,
                JavaxOverridingSubClassOfSimpleTransactionalClassWithConfig.class);
        assertThat(validateConstraint("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass").getStatus()).isEqualTo(FAILURE);
        store.beginTransaction();
        final List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations).hasSize(1);
        final Result<Constraint> result = constraintViolations.get(0);
        assertThat(result.getRule().getId()).isEqualTo("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass");
        assertThat(result.getRows()).hasSize(2);
        final Map<String, Row> resultMap = result.getRows().stream().collect(Collectors.toMap(row -> (row.getColumns().get("Type").getLabel()), Function.identity()));

        final Row callingSubClassOfSimpleTransactionalClassWithConfigResult = resultMap.get(JavaxCallingSubClassOfSimpleTransactionClassWithConfig.class.getName());
        assertThat(callingSubClassOfSimpleTransactionalClassWithConfigResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxCallingSubClassOfSimpleTransactionClassWithConfig.class));
        assertThat(callingSubClassOfSimpleTransactionalClassWithConfigResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxCallingSubClassOfSimpleTransactionClassWithConfig.class, "transactionalMethodWithAdditionalConfiguration"));
        assertThat(callingSubClassOfSimpleTransactionalClassWithConfigResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(callingSubClassOfSimpleTransactionalClassWithConfigResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JavaxCallingSubClassOfSimpleTransactionClassWithConfig.class, "void methodWithAdditionalConfigurationAttribute()"));
        assertThat(callingSubClassOfSimpleTransactionalClassWithConfigResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(callingSubClassOfSimpleTransactionalClassWithConfigResult.getColumns().get("TargetMethodSetsAdditionalConfiguration").getLabel()).isEqualTo("true");

        final Row overridingSubClassOfSimpleTransactionalClassWithAdditionalConfigResult = resultMap.get(JavaxOverridingSubClassOfSimpleTransactionalClassWithConfig.class.getName());
        assertThat(overridingSubClassOfSimpleTransactionalClassWithAdditionalConfigResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxOverridingSubClassOfSimpleTransactionalClassWithConfig.class));
        assertThat(overridingSubClassOfSimpleTransactionalClassWithAdditionalConfigResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxOverridingSubClassOfSimpleTransactionalClassWithConfig.class, "methodWithOverriddenConfigurationAttribute"));
        assertThat(overridingSubClassOfSimpleTransactionalClassWithAdditionalConfigResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(overridingSubClassOfSimpleTransactionalClassWithAdditionalConfigResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxSimpleTransactionalClassWithConfig.class, "methodWithOverriddenConfigurationAttribute"));
        assertThat(overridingSubClassOfSimpleTransactionalClassWithAdditionalConfigResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(overridingSubClassOfSimpleTransactionalClassWithAdditionalConfigResult.getColumns().get("TargetMethodSetsAdditionalConfiguration").getLabel()).isEqualTo("true");

        store.commitTransaction();
    }

    @Test
    void transactionalMethodMustNotBeInvokedFromSameClassGenericInheritanceJakarta() throws Exception {
        scanClasses(JakartaGenericClassWithTransactionalMethodAndAdditionalConfig.class, JakartaCallingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig.class,
                JakartaOverridingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig.class);
        assertThat(validateConstraint("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass").getStatus()).isEqualTo(FAILURE);
        store.beginTransaction();
        final List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations).hasSize(1);
        final Result<Constraint> result = constraintViolations.get(0);
        assertThat(result.getRule().getId()).isEqualTo("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass");
        assertThat(result.getRows()).hasSize(2);
        final Map<String, Row> resultMap = result.getRows().stream().collect(Collectors.toMap(row -> (row.getColumns().get("Type").getLabel()), Function.identity()));

        final Row callingSubClassOfGenericTransactionalClassWithConfigResult = resultMap.get(JakartaCallingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig.class.getName());
        assertThat(callingSubClassOfGenericTransactionalClassWithConfigResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaCallingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig.class));
        assertThat(callingSubClassOfGenericTransactionalClassWithConfigResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaCallingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig.class, "anotherMethodWithRequiredSemantics"));
        assertThat(callingSubClassOfGenericTransactionalClassWithConfigResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(callingSubClassOfGenericTransactionalClassWithConfigResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JakartaCallingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig.class, "void methodWithAdditionalConfigurationAttribute(java.lang.Object)"));
        assertThat(callingSubClassOfGenericTransactionalClassWithConfigResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(callingSubClassOfGenericTransactionalClassWithConfigResult.getColumns().get("TargetMethodSetsAdditionalConfiguration").getLabel()).isEqualTo("true");

        final Row overridingSubClassOfGenericTransactionalClassWithAdditionalConfigResult = resultMap.get(JakartaOverridingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig.class.getName());
        assertThat(overridingSubClassOfGenericTransactionalClassWithAdditionalConfigResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaOverridingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig.class));
        assertThat(overridingSubClassOfGenericTransactionalClassWithAdditionalConfigResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JakartaOverridingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig.class, "void methodWithOverriddenConfigurationAttribute(java.lang.Long)"));
        assertThat(overridingSubClassOfGenericTransactionalClassWithAdditionalConfigResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(overridingSubClassOfGenericTransactionalClassWithAdditionalConfigResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JakartaGenericClassWithTransactionalMethodAndAdditionalConfig.class, "void methodWithOverriddenConfigurationAttribute(java.lang.Object)"));
        assertThat(overridingSubClassOfGenericTransactionalClassWithAdditionalConfigResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(overridingSubClassOfGenericTransactionalClassWithAdditionalConfigResult.getColumns().get("TargetMethodSetsAdditionalConfiguration").getLabel()).isEqualTo("true");

        store.commitTransaction();
    }

    @Test
    void transactionalMethodMustNotBeInvokedFromSameClassGenericInheritanceJavax() throws Exception {
        scanClasses(JavaxGenericClassWithTransactionalMethodAndAdditionalConfig.class, JavaxCallingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig.class,
                JavaxOverridingSubClassOfTransactionalClassWithTransactionalMethodAndAdditionalConfig.class);
        assertThat(validateConstraint("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass").getStatus()).isEqualTo(FAILURE);
        store.beginTransaction();
        final List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations).hasSize(1);
        final Result<Constraint> result = constraintViolations.get(0);
        assertThat(result.getRule().getId()).isEqualTo("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass");
        assertThat(result.getRows()).hasSize(2);
        final Map<String, Row> resultMap = result.getRows().stream().collect(Collectors.toMap(row -> (row.getColumns().get("Type").getLabel()), Function.identity()));

        final Row callingSubClassOfGenericTransactionalClassWithConfigResult = resultMap.get(JavaxCallingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig.class.getName());
        assertThat(callingSubClassOfGenericTransactionalClassWithConfigResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxCallingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig.class));
        assertThat(callingSubClassOfGenericTransactionalClassWithConfigResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxCallingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig.class, "anotherMethodWithRequiredSemantics"));
        assertThat(callingSubClassOfGenericTransactionalClassWithConfigResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(callingSubClassOfGenericTransactionalClassWithConfigResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JavaxCallingSubClassOfGenericClassWithTransactionalMethodAndAdditionalConfig.class, "void methodWithAdditionalConfigurationAttribute(java.lang.Object)"));
        assertThat(callingSubClassOfGenericTransactionalClassWithConfigResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(callingSubClassOfGenericTransactionalClassWithConfigResult.getColumns().get("TargetMethodSetsAdditionalConfiguration").getLabel()).isEqualTo("true");

        final Row overridingSubClassOfGenericTransactionalClassWithAdditionalConfigResult = resultMap.get(JavaxOverridingSubClassOfTransactionalClassWithTransactionalMethodAndAdditionalConfig.class.getName());
        assertThat(overridingSubClassOfGenericTransactionalClassWithAdditionalConfigResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxOverridingSubClassOfTransactionalClassWithTransactionalMethodAndAdditionalConfig.class));
        assertThat(overridingSubClassOfGenericTransactionalClassWithAdditionalConfigResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JavaxOverridingSubClassOfTransactionalClassWithTransactionalMethodAndAdditionalConfig.class, "void methodWithOverriddenConfigurationAttribute(java.lang.Long)"));
        assertThat(overridingSubClassOfGenericTransactionalClassWithAdditionalConfigResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(overridingSubClassOfGenericTransactionalClassWithAdditionalConfigResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JavaxGenericClassWithTransactionalMethodAndAdditionalConfig.class, "void methodWithOverriddenConfigurationAttribute(java.lang.Object)"));
        assertThat(overridingSubClassOfGenericTransactionalClassWithAdditionalConfigResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(overridingSubClassOfGenericTransactionalClassWithAdditionalConfigResult.getColumns().get("TargetMethodSetsAdditionalConfiguration").getLabel()).isEqualTo("true");

        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = {JavaxTransactionalClassWithAdditionalConfigurationAndNestedClass.class, JakartaTransactionalClassWithAdditionalConfigurationAndNestedClass.class})
    void transactionalMethodMustNotBeInvokedFromNestedClass(Class<?> clazz) throws Exception {
        final Class<?> nestedClass = clazz.getDeclaredClasses()[0];
        scanClasses(clazz, nestedClass);
        assertThat(validateConstraint("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass")
                .getStatus()).isEqualTo(FAILURE);

        store.beginTransaction();

        final List<Result<Constraint>> constraintViolations =
                new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations).hasSize(1);

        assertThat(constraintViolations.get(0).getRule().getId())
                .isEqualTo("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass");

        final Result<Constraint> result = constraintViolations.get(0);
        assertThat(result.getRows()).hasSize(1);

        final Row nestedClassRow = result.getRows().get(0);
        assertThat(nestedClassRow.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(nestedClass));
        assertThat(nestedClassRow.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(nestedClass, "methodCallingOuterMethodWithAdditionalConfiguration"));
        assertThat(nestedClassRow.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NONE");
        assertThat(nestedClassRow.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "methodWithAdditionalConfigurationAttribute"));
        assertThat(nestedClassRow.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");
        assertThat(nestedClassRow.getColumns().get("TargetMethodSetsAdditionalConfiguration").getLabel()).isEqualTo("true");
    }
}
