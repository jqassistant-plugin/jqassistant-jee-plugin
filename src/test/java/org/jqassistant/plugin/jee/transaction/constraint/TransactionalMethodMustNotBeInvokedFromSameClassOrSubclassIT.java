package org.jqassistant.plugin.jee.transaction.constraint;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.report.api.model.Row;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.plugin.java.api.model.MethodDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import org.jqassistant.plugin.jee.transaction.set.*;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta.JakartaCallingSubClassOfGenericClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta.JakartaCallingSubClassOfGenericNonTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta.JakartaCallingSubClassOfGenericTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta.JakartaGenericClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta.JakartaGenericTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta.JakartaOverridingSubClassOfGenericClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta.JakartaOverridingSubClassOfGenericNonTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta.JakartaOverridingSubClassOfGenericTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxCallingSubClassOfGenericClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxCallingSubClassOfGenericNonTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxCallingSubClassOfGenericTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxGenericClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.GenericNonTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxGenericTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxOverridingSubClassOfGenericClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxOverridingSubClassOfGenericNonTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxOverridingSubClassOfGenericTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta.*;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.SimpleNonTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.buschmais.jqassistant.plugin.java.test.assertj.MethodDescriptorCondition.methodDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.assertj.TypeDescriptorCondition.typeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.FAILURE;
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.jqassistant.plugin.jee.SimpleMethodDescriptorCondition.simpleMethodDescriptor;

class TransactionalMethodMustNotBeInvokedFromSameClassOrSubclassIT extends AbstractJavaPluginIT {

    @ParameterizedTest
    @ValueSource(classes = {JavaxTransactionalMethod.class, JakartaTransactionalMethod.class,
            JakartaTransactionalClass.class, JavaxTransactionalClass.class, JavaxMessageDrivenEjb.class, JakartaMessageDrivenEjb.class,
            JavaxSingletonEjb.class, JakartaSingletonEjb.class, JavaxStatefulEjb.class, JakartaStatefulEjb.class, JavaxStatelessEjb.class, JakartaStatelessEjb.class})
    void transactionMethodsMustNotBeCalledDirectlyWithViolations(Class<?> clazz) throws Exception {
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
        assertThat(result.getRows()).hasSize(2);

        final Row row1 = result.getRows().get(0);
        assertThat(row1.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(clazz));
        assertThat(row1.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "neverTransactionalCallingRequiredTransactionalTransitively"));
        assertThat(row1.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(row1.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "transactionalMethodWithRequiredSemantics"));
        assertThat(row1.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        final Row row2 = result.getRows().get(1);
        assertThat(row2.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(clazz));
        assertThat(row2.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "transactionalMethodWithNeverSemantics"));
        assertThat(row2.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(row2.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "transactionalMethodWithRequiredSemantics"));
        assertThat(row2.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        store.commitTransaction();
    }

    @Test
    void transactionalMethodMustNotBeInvokedFromSameClassSimpleInheritanceJakarta() throws Exception {
        scanClasses(SimpleNonTransactionalClass.class, JakartaSimpleTransactionalClass.class, JakartaSimpleClassWithTransactionalMethod.class,
                JakartaCallingSubClassOfSimpleNonTransactionalClass.class, JakartaCallingSubClassOfSimpleTransactionalClass.class, JakartaCallingSubClassOfSimpleClassWithTransactionalMethod.class,
                JakartaOverridingSubClassOfSimpleNonTransactionalClass.class, JakartaOverridingSubClassOfSimpleTransactionalClass.class, JakartaOverridingSubClassOfSimpleClassWithTransactionalMethod.class);
        assertThat(validateConstraint("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass").getStatus()).isEqualTo(FAILURE);
        store.beginTransaction();
        final List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations).hasSize(1);
        final Result<Constraint> result = constraintViolations.get(0);
        assertThat(result.getRule().getId()).isEqualTo("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass");
        assertThat(result.getRows()).hasSize(4);
        final Map<String, Row> resultMap = result.getRows().stream().collect(Collectors.toMap(row -> (row.getColumns().get("Type").getLabel()), Function.identity()));

        final Row callingSubClassOfSimpleTransactionalClassResult = resultMap.get(JakartaCallingSubClassOfSimpleTransactionalClass.class.getName());
        assertThat(callingSubClassOfSimpleTransactionalClassResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaCallingSubClassOfSimpleTransactionalClass.class));
        assertThat(callingSubClassOfSimpleTransactionalClassResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaCallingSubClassOfSimpleTransactionalClass.class, "transactionalMethodWithNeverSemantics"));
        assertThat(callingSubClassOfSimpleTransactionalClassResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(callingSubClassOfSimpleTransactionalClassResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JakartaCallingSubClassOfSimpleTransactionalClass.class, "void methodWithRequiredSemantics()"));
        assertThat(callingSubClassOfSimpleTransactionalClassResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        final Row callingSubClassOfSimpleClassWithTransactionalMethodResult = resultMap.get(JakartaCallingSubClassOfSimpleClassWithTransactionalMethod.class.getName());
        assertThat(callingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaCallingSubClassOfSimpleClassWithTransactionalMethod.class));
        assertThat(callingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaCallingSubClassOfSimpleClassWithTransactionalMethod.class, "transactionalMethodWithNeverSemantics"));
        assertThat(callingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(callingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JakartaCallingSubClassOfSimpleClassWithTransactionalMethod.class, "void methodWithRequiredSemantics()"));
        assertThat(callingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        final Row overridingSubClassOfSimpleTransactionalClassResult = resultMap.get(JakartaOverridingSubClassOfSimpleTransactionalClass.class.getName());
        assertThat(overridingSubClassOfSimpleTransactionalClassResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaOverridingSubClassOfSimpleTransactionalClass.class));
        assertThat(overridingSubClassOfSimpleTransactionalClassResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaOverridingSubClassOfSimpleTransactionalClass.class, "methodWithOverriddenSemantics"));
        assertThat(overridingSubClassOfSimpleTransactionalClassResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(overridingSubClassOfSimpleTransactionalClassResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaSimpleTransactionalClass.class, "methodWithOverriddenSemantics"));
        assertThat(overridingSubClassOfSimpleTransactionalClassResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        final Row overridingSubClassOfSimpleClassWithTransactionalMethodResult = resultMap.get(JakartaOverridingSubClassOfSimpleClassWithTransactionalMethod.class.getName());
        assertThat(overridingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaOverridingSubClassOfSimpleClassWithTransactionalMethod.class));
        assertThat(overridingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaOverridingSubClassOfSimpleClassWithTransactionalMethod.class, "methodWithOverriddenSemantics"));
        assertThat(overridingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(overridingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaSimpleClassWithTransactionalMethod.class, "methodWithOverriddenSemantics"));
        assertThat(overridingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        store.commitTransaction();
    }

    @Test
    void transactionalMethodMustNotBeInvokedFromSameClassSimpleInheritanceJavax() throws Exception {
        scanClasses(SimpleNonTransactionalClass.class, JavaxSimpleTransactionalClass.class, JavaxSimpleClassWithTransactionalMethod.class,
                JavaxCallingSubClassOfSimpleNonTransactionalClass.class, JavaxCallingSubClassOfSimpleTransactionalClass.class, JavaxCallingSubClassOfSimpleClassWithTransactionalMethod.class,
                JavaxOverridingSubClassOfSimpleNonTransactionalClass.class, JavaxOverridingSubClassOfSimpleTransactionalClass.class, JavaxOverridingSubClassOfSimpleClassWithTransactionalMethod.class);
        assertThat(validateConstraint("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass").getStatus()).isEqualTo(FAILURE);
        store.beginTransaction();
        final List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations).hasSize(1);
        final Result<Constraint> result = constraintViolations.get(0);
        assertThat(result.getRule().getId()).isEqualTo("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass");
        assertThat(result.getRows()).hasSize(4);
        final Map<String, Row> resultMap = result.getRows().stream().collect(Collectors.toMap(row -> (row.getColumns().get("Type").getLabel()), Function.identity()));

        final Row callingSubClassOfSimpleTransactionalClassResult = resultMap.get(JavaxCallingSubClassOfSimpleTransactionalClass.class.getName());
        assertThat(callingSubClassOfSimpleTransactionalClassResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxCallingSubClassOfSimpleTransactionalClass.class));
        assertThat(callingSubClassOfSimpleTransactionalClassResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxCallingSubClassOfSimpleTransactionalClass.class, "transactionalMethodWithNeverSemantics"));
        assertThat(callingSubClassOfSimpleTransactionalClassResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(callingSubClassOfSimpleTransactionalClassResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JavaxCallingSubClassOfSimpleTransactionalClass.class, "void methodWithRequiredSemantics()"));
        assertThat(callingSubClassOfSimpleTransactionalClassResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        final Row callingSubClassOfSimpleClassWithTransactionalMethodResult = resultMap.get(JavaxCallingSubClassOfSimpleClassWithTransactionalMethod.class.getName());
        assertThat(callingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxCallingSubClassOfSimpleClassWithTransactionalMethod.class));
        assertThat(callingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxCallingSubClassOfSimpleClassWithTransactionalMethod.class, "transactionalMethodWithNeverSemantics"));
        assertThat(callingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(callingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JavaxCallingSubClassOfSimpleClassWithTransactionalMethod.class, "void methodWithRequiredSemantics()"));
        assertThat(callingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        final Row overridingSubClassOfSimpleTransactionalClassResult = resultMap.get(JavaxOverridingSubClassOfSimpleTransactionalClass.class.getName());
        assertThat(overridingSubClassOfSimpleTransactionalClassResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxOverridingSubClassOfSimpleTransactionalClass.class));
        assertThat(overridingSubClassOfSimpleTransactionalClassResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxOverridingSubClassOfSimpleTransactionalClass.class, "methodWithOverriddenSemantics"));
        assertThat(overridingSubClassOfSimpleTransactionalClassResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(overridingSubClassOfSimpleTransactionalClassResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxSimpleTransactionalClass.class, "methodWithOverriddenSemantics"));
        assertThat(overridingSubClassOfSimpleTransactionalClassResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        final Row overridingSubClassOfSimpleClassWithTransactionalMethodResult = resultMap.get(JavaxOverridingSubClassOfSimpleClassWithTransactionalMethod.class.getName());
        assertThat(overridingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxOverridingSubClassOfSimpleClassWithTransactionalMethod.class));
        assertThat(overridingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxOverridingSubClassOfSimpleClassWithTransactionalMethod.class, "methodWithOverriddenSemantics"));
        assertThat(overridingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(overridingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxSimpleClassWithTransactionalMethod.class, "methodWithOverriddenSemantics"));
        assertThat(overridingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        store.commitTransaction();
    }

    @Test
    void transactionalMethodMustNotBeInvokedFromSameClassGenericInheritanceJakarta() throws Exception {
        scanClasses(GenericNonTransactionalClass.class, JakartaGenericTransactionalClass.class, JakartaGenericClassWithTransactionalMethod.class,
                JakartaCallingSubClassOfGenericNonTransactionalClass.class, JakartaCallingSubClassOfGenericTransactionalClass.class,
                JakartaCallingSubClassOfGenericClassWithTransactionalMethod.class, JakartaOverridingSubClassOfGenericNonTransactionalClass.class,
                JakartaOverridingSubClassOfGenericTransactionalClass.class, JakartaOverridingSubClassOfGenericClassWithTransactionalMethod.class);
        assertThat(validateConstraint("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass").getStatus()).isEqualTo(FAILURE);
        store.beginTransaction();
        final List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations).hasSize(1);
        final Result<Constraint> result = constraintViolations.get(0);
        assertThat(result.getRule().getId()).isEqualTo("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass");
        assertThat(result.getRows()).hasSize(4);
        final Map<String, Row> resultMap = result.getRows().stream().collect(Collectors.toMap(row -> (row.getColumns().get("Type").getLabel()), Function.identity()));

        final Row callingSubClassOfGenericClassWithTransactionalMethodResult = resultMap.get(JakartaCallingSubClassOfGenericClassWithTransactionalMethod.class.getName());
        assertThat(callingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaCallingSubClassOfGenericClassWithTransactionalMethod.class));
        assertThat(callingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaCallingSubClassOfGenericClassWithTransactionalMethod.class, "callingMethodWithNeverSemantics"));
        assertThat(callingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(callingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JakartaCallingSubClassOfGenericClassWithTransactionalMethod.class, "void methodWithOverriddenSemantics(java.lang.Object)"));
        assertThat(callingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        final Row callingSubClassOfGenericTransactionalClassResult = resultMap.get(JakartaCallingSubClassOfGenericTransactionalClass.class.getName());
        assertThat(callingSubClassOfGenericTransactionalClassResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaCallingSubClassOfGenericTransactionalClass.class));
        assertThat(callingSubClassOfGenericTransactionalClassResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaCallingSubClassOfGenericTransactionalClass.class, "callingMethodWithNeverSemantics"));
        assertThat(callingSubClassOfGenericTransactionalClassResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(callingSubClassOfGenericTransactionalClassResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JakartaCallingSubClassOfGenericTransactionalClass.class, "void methodWithOverriddenSemantics(java.lang.Object)"));
        assertThat(callingSubClassOfGenericTransactionalClassResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        final Row overridingSubClassOfGenericTransactionalClassResult = resultMap.get(JakartaOverridingSubClassOfGenericTransactionalClass.class.getName());
        assertThat(overridingSubClassOfGenericTransactionalClassResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaOverridingSubClassOfGenericTransactionalClass.class));
        assertThat(overridingSubClassOfGenericTransactionalClassResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaOverridingSubClassOfGenericTransactionalClass.class, "methodWithOverriddenSemantics", Long.class));
        assertThat(overridingSubClassOfGenericTransactionalClassResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(overridingSubClassOfGenericTransactionalClassResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaGenericTransactionalClass.class, "methodWithOverriddenSemantics", Object.class));
        assertThat(overridingSubClassOfGenericTransactionalClassResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        final Row overridingSubClassOfGenericClassWithTransactionalMethodResult = resultMap.get(JakartaOverridingSubClassOfGenericClassWithTransactionalMethod.class.getName());
        assertThat(overridingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaOverridingSubClassOfGenericClassWithTransactionalMethod.class));
        assertThat(overridingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaOverridingSubClassOfGenericClassWithTransactionalMethod.class, "methodWithOverriddenSemantics", Long.class));
        assertThat(overridingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(overridingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaGenericClassWithTransactionalMethod.class, "methodWithOverriddenSemantics", Object.class));
        assertThat(overridingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        store.commitTransaction();
    }

    @Test
    void transactionalMethodMustNotBeInvokedFromSameClassGenericInheritanceJavax() throws Exception {
        scanClasses(GenericNonTransactionalClass.class, JavaxGenericTransactionalClass.class, JavaxGenericClassWithTransactionalMethod.class,
                JavaxCallingSubClassOfGenericNonTransactionalClass.class, JavaxCallingSubClassOfGenericTransactionalClass.class,
                JavaxCallingSubClassOfGenericClassWithTransactionalMethod.class, JavaxOverridingSubClassOfGenericNonTransactionalClass.class,
                JavaxOverridingSubClassOfGenericTransactionalClass.class, JavaxOverridingSubClassOfGenericClassWithTransactionalMethod.class);
        assertThat(validateConstraint("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass").getStatus()).isEqualTo(FAILURE);
        store.beginTransaction();
        final List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations).hasSize(1);
        final Result<Constraint> result = constraintViolations.get(0);
        assertThat(result.getRule().getId()).isEqualTo("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass");
        assertThat(result.getRows()).hasSize(4);
        final Map<String, Row> resultMap = result.getRows().stream().collect(Collectors.toMap(row -> (row.getColumns().get("Type").getLabel()), Function.identity()));

        final Row callingSubClassOfGenericClassWithTransactionalMethodResult = resultMap.get(JavaxCallingSubClassOfGenericClassWithTransactionalMethod.class.getName());
        assertThat(callingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxCallingSubClassOfGenericClassWithTransactionalMethod.class));
        assertThat(callingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxCallingSubClassOfGenericClassWithTransactionalMethod.class, "callingMethodWithNeverSemantics"));
        assertThat(callingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(callingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JavaxCallingSubClassOfGenericClassWithTransactionalMethod.class, "void methodWithOverriddenSemantics(java.lang.Object)"));
        assertThat(callingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        final Row callingSubClassOfGenericTransactionalClassResult = resultMap.get(JavaxCallingSubClassOfGenericTransactionalClass.class.getName());
        assertThat(callingSubClassOfGenericTransactionalClassResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxCallingSubClassOfGenericTransactionalClass.class));
        assertThat(callingSubClassOfGenericTransactionalClassResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxCallingSubClassOfGenericTransactionalClass.class, "callingMethodWithNeverSemantics"));
        assertThat(callingSubClassOfGenericTransactionalClassResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(callingSubClassOfGenericTransactionalClassResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JavaxCallingSubClassOfGenericTransactionalClass.class, "void methodWithOverriddenSemantics(java.lang.Object)"));
        assertThat(callingSubClassOfGenericTransactionalClassResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        final Row overridingSubClassOfGenericTransactionalClassResult = resultMap.get(JavaxOverridingSubClassOfGenericTransactionalClass.class.getName());
        assertThat(overridingSubClassOfGenericTransactionalClassResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxOverridingSubClassOfGenericTransactionalClass.class));
        assertThat(overridingSubClassOfGenericTransactionalClassResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxOverridingSubClassOfGenericTransactionalClass.class, "methodWithOverriddenSemantics", Long.class));
        assertThat(overridingSubClassOfGenericTransactionalClassResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(overridingSubClassOfGenericTransactionalClassResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxGenericTransactionalClass.class, "methodWithOverriddenSemantics", Object.class));
        assertThat(overridingSubClassOfGenericTransactionalClassResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        final Row overridingSubClassOfGenericClassWithTransactionalMethodResult = resultMap.get(JavaxOverridingSubClassOfGenericClassWithTransactionalMethod.class.getName());
        assertThat(overridingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxOverridingSubClassOfGenericClassWithTransactionalMethod.class));
        assertThat(overridingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxOverridingSubClassOfGenericClassWithTransactionalMethod.class, "methodWithOverriddenSemantics", Long.class));
        assertThat(overridingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(overridingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxGenericClassWithTransactionalMethod.class, "methodWithOverriddenSemantics", Object.class));
        assertThat(overridingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        store.commitTransaction();
    }



    @ParameterizedTest
    @ValueSource(classes = {JavaxTransactionalClassWithNestedClass.class, JakartaTransactionalClassWithNestedClass.class})
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
        assertThat(result.getRows()).hasSize(2);

        final Row nonTransactionalMethodRow = result.getRows().get(0);
        assertThat(nonTransactionalMethodRow.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(nestedClass));
        assertThat(nonTransactionalMethodRow.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(nestedClass, "nonTransactionalMethod"));
        assertThat(nonTransactionalMethodRow.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NONE");
        assertThat(nonTransactionalMethodRow.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "transactionalMethodWithRequiredSemantics"));
        assertThat(nonTransactionalMethodRow.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        final Row methodWithNeverSemanticsRow = result.getRows().get(1);
        assertThat(methodWithNeverSemanticsRow.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(nestedClass));
        assertThat(methodWithNeverSemanticsRow.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(nestedClass, "transactionalMethodWithNeverSemantics"));
        assertThat(methodWithNeverSemanticsRow.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(methodWithNeverSemanticsRow.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "transactionalMethodWithRequiredSemantics"));
        assertThat(methodWithNeverSemanticsRow.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = {JavaxSubClassWithCallingNestedClass.class, JakartaSubClassWithCallingNestedClass.class})
    void transactionalMethodMustNotBeInvokedFromNestedClassInSubClass(Class<?> clazz) throws Exception {
        final Class<?> nestedClass = clazz.getDeclaredClasses()[0];
        final Class<?> superClass = clazz.getSuperclass();
        scanClasses(clazz, nestedClass, superClass);
        assertThat(validateConstraint("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass")
                .getStatus()).isEqualTo(FAILURE);

        store.beginTransaction();

        final List<Result<Constraint>> constraintViolations =
                new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations).hasSize(1);

        assertThat(constraintViolations.get(0).getRule().getId())
                .isEqualTo("jee-transaction:TransactionChangingMethodMustNotBeInvokedFromSameClassOrSubclass");

        final Result<Constraint> result = constraintViolations.get(0);
        assertThat(result.getRows()).hasSize(2);

        final Row nonTransactionalMethodRow = result.getRows().get(0);
        assertThat(nonTransactionalMethodRow.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(nestedClass));
        assertThat(nonTransactionalMethodRow.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(nestedClass, "nonTransactionalMethod"));
        assertThat(nonTransactionalMethodRow.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NONE");
        assertThat(nonTransactionalMethodRow.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(superClass, "methodWithRequiredSemantics"));
        assertThat(nonTransactionalMethodRow.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        final Row methodWithNeverSemanticsRow = result.getRows().get(1);
        assertThat(methodWithNeverSemanticsRow.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(nestedClass));
        assertThat(methodWithNeverSemanticsRow.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(nestedClass, "transactionalMethodWithNeverSemantics"));
        assertThat(methodWithNeverSemanticsRow.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NEVER");
        assertThat(methodWithNeverSemanticsRow.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(superClass, "methodWithRequiredSemantics"));
        assertThat(methodWithNeverSemanticsRow.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = {JavaxNonTransactionalMethodCallingTransactionalMethod.class, JakartaNonTransactionalMethodCallingTransactionalMethod.class})
    void nonTransactionalMethodCallingTransactionalMethod(Class<?> clazz) throws Exception {
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
        assertThat(result.getRows()).hasSize(2);

        final Row directlyCallingMethodRow = result.getRows().get(0);
        assertThat(directlyCallingMethodRow.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(clazz));
        assertThat(directlyCallingMethodRow.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "nonTransactionalMethodCallingTransactionalRequiredMethodDirectly"));
        assertThat(directlyCallingMethodRow.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NONE");
        assertThat(directlyCallingMethodRow.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "transactionalMethodWithRequiredSemantics"));
        assertThat(directlyCallingMethodRow.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

        final Row transitivelyCallingMethodRow = result.getRows().get(1);
        assertThat(transitivelyCallingMethodRow.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(clazz));
        assertThat(transitivelyCallingMethodRow.getColumns().get("SourceMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "nonTransactionalMethodCallingTransactionalRequiredMethodTransitively"));
        assertThat(transitivelyCallingMethodRow.getColumns().get("SourceTransactionalSemantics").getLabel()).isEqualTo("NONE");
        assertThat(transitivelyCallingMethodRow.getColumns().get("TargetTransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(clazz, "transactionalMethodWithRequiredSemantics"));
        assertThat(transitivelyCallingMethodRow.getColumns().get("TargetTransactionalSemantics").getLabel()).isEqualTo("REQUIRED");

    }

}
