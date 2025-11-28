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
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta.JakartaGenericNonTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta.JakartaGenericTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta.JakartaOverridingSubClassOfGenericClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta.JakartaOverridingSubClassOfGenericNonTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta.JakartaOverridingSubClassOfGenericTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxCallingSubClassOfGenericClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxCallingSubClassOfGenericNonTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxCallingSubClassOfGenericTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxGenericClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxGenericNonTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxGenericTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxOverridingSubClassOfGenericClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxOverridingSubClassOfGenericNonTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxOverridingSubClassOfGenericTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta.JakartaCallingSubClassOfSimpleClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta.JakartaCallingSubClassOfSimpleNonTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta.JakartaCallingSubClassOfSimpleTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta.JakartaOverridingSubClassOfSimpleClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta.JakartaOverridingSubClassOfSimpleNonTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta.JakartaOverridingSubClassOfSimpleTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta.JakartaSimpleClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta.JakartaSimpleNonTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta.JakartaSimpleTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax.JavaxCallingSubClassOfSimpleClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax.JavaxCallingSubClassOfSimpleNonTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax.JavaxCallingSubClassOfSimpleTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax.JavaxOverridingSubClassOfSimpleClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax.JavaxOverridingSubClassOfSimpleNonTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax.JavaxOverridingSubClassOfSimpleTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax.JavaxSimpleClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax.JavaxSimpleNonTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax.JavaxSimpleTransactionalClass;
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

class TransactionalMethodMustNotBeInvokedFromSameSubclassIT extends AbstractJavaPluginIT {

    @ParameterizedTest
    @ValueSource(classes = {JavaxTransactionalMethod.class, JakartaTransactionalMethod.class,
            JakartaTransactionalClass.class, JavaxTransactionalClass.class, JavaxMessageDrivenEjb.class, JakartaMessageDrivenEjb.class,
            JavaxSingletonEjb.class, JakartaSingletonEjb.class, JavaxStatefulEjb.class, JakartaStatefulEjb.class, JavaxStatelessEjb.class, JakartaStatelessEjb.class})
    void transactionMethodsMustNotBeCalledDirectlyWithViolations(Class<?> clazz) throws Exception {
        scanClasses(clazz);
        assertThat(validateConstraint("jee-transaction:TransactionalMethodMustNotBeInvokedFromSameClassOrSubclass")
                .getStatus()).isEqualTo(FAILURE);

        store.beginTransaction();

        final List<Result<Constraint>> constraintViolations =
                new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations).hasSize(1);

        assertThat(constraintViolations.get(0).getRule().getId())
                .isEqualTo("jee-transaction:TransactionalMethodMustNotBeInvokedFromSameClassOrSubclass");

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

    @Test
    void transactionalMethodMustNotBeInvokedFromSameClassSimpleInheritanceJakarta() throws Exception {
        scanClasses(JakartaSimpleNonTransactionalClass.class, JakartaSimpleTransactionalClass.class, JakartaSimpleClassWithTransactionalMethod.class,
                JakartaCallingSubClassOfSimpleNonTransactionalClass.class, JakartaCallingSubClassOfSimpleTransactionalClass.class, JakartaCallingSubClassOfSimpleClassWithTransactionalMethod.class,
                JakartaOverridingSubClassOfSimpleNonTransactionalClass.class, JakartaOverridingSubClassOfSimpleTransactionalClass.class, JakartaOverridingSubClassOfSimpleClassWithTransactionalMethod.class);
        assertThat(validateConstraint("jee-transaction:TransactionalMethodMustNotBeInvokedFromSameClassOrSubclass").getStatus()).isEqualTo(FAILURE);
        store.beginTransaction();
        final List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations).hasSize(1);
        final Result<Constraint> result = constraintViolations.get(0);
        assertThat(result.getRule().getId()).isEqualTo("jee-transaction:TransactionalMethodMustNotBeInvokedFromSameClassOrSubclass");
        assertThat(result.getRows()).hasSize(4);
        final Map<String, Row> resultMap = result.getRows().stream().collect(Collectors.toMap(row -> (row.getColumns().get("Type").getLabel()), Function.identity()));

        final Row callingSubClassOfSimpleTransactionalClassResult = resultMap.get(JakartaCallingSubClassOfSimpleTransactionalClass.class.getName());
        assertThat(callingSubClassOfSimpleTransactionalClassResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaCallingSubClassOfSimpleTransactionalClass.class));
        assertThat(callingSubClassOfSimpleTransactionalClassResult.getColumns().get("Method").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaCallingSubClassOfSimpleTransactionalClass.class, "anotherMethod"));
        assertThat(callingSubClassOfSimpleTransactionalClassResult.getColumns().get("TransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JakartaCallingSubClassOfSimpleTransactionalClass.class, "void method()"));

        final Row callingSubClassOfSimpleClassWithTransactionalMethodResult = resultMap.get(JakartaCallingSubClassOfSimpleClassWithTransactionalMethod.class.getName());
        assertThat(callingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaCallingSubClassOfSimpleClassWithTransactionalMethod.class));
        assertThat(callingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("Method").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaCallingSubClassOfSimpleClassWithTransactionalMethod.class, "anotherMethod"));
        assertThat(callingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("TransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JakartaCallingSubClassOfSimpleClassWithTransactionalMethod.class, "void method()"));

        final Row overridingSubClassOfSimpleTransactionalClassResult = resultMap.get(JakartaOverridingSubClassOfSimpleTransactionalClass.class.getName());
        assertThat(overridingSubClassOfSimpleTransactionalClassResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaOverridingSubClassOfSimpleTransactionalClass.class));
        assertThat(overridingSubClassOfSimpleTransactionalClassResult.getColumns().get("Method").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaOverridingSubClassOfSimpleTransactionalClass.class, "method"));
        assertThat(overridingSubClassOfSimpleTransactionalClassResult.getColumns().get("TransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaSimpleTransactionalClass.class, "method"));

        final Row overridingSubClassOfSimpleClassWithTransactionalMethodResult = resultMap.get(JakartaOverridingSubClassOfSimpleClassWithTransactionalMethod.class.getName());
        assertThat(overridingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaOverridingSubClassOfSimpleClassWithTransactionalMethod.class));
        assertThat(overridingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("Method").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaOverridingSubClassOfSimpleClassWithTransactionalMethod.class, "method"));
        assertThat(overridingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("TransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaSimpleClassWithTransactionalMethod.class, "method"));

        store.commitTransaction();
    }

    @Test
    void transactionalMethodMustNotBeInvokedFromSameClassSimpleInheritanceJavax() throws Exception {
        scanClasses(JavaxSimpleNonTransactionalClass.class, JavaxSimpleTransactionalClass.class, JavaxSimpleClassWithTransactionalMethod.class,
                JavaxCallingSubClassOfSimpleNonTransactionalClass.class, JavaxCallingSubClassOfSimpleTransactionalClass.class, JavaxCallingSubClassOfSimpleClassWithTransactionalMethod.class,
                JavaxOverridingSubClassOfSimpleNonTransactionalClass.class, JavaxOverridingSubClassOfSimpleTransactionalClass.class, JavaxOverridingSubClassOfSimpleClassWithTransactionalMethod.class);
        assertThat(validateConstraint("jee-transaction:TransactionalMethodMustNotBeInvokedFromSameClassOrSubclass").getStatus()).isEqualTo(FAILURE);
        store.beginTransaction();
        final List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations).hasSize(1);
        final Result<Constraint> result = constraintViolations.get(0);
        assertThat(result.getRule().getId()).isEqualTo("jee-transaction:TransactionalMethodMustNotBeInvokedFromSameClassOrSubclass");
        assertThat(result.getRows()).hasSize(4);
        final Map<String, Row> resultMap = result.getRows().stream().collect(Collectors.toMap(row -> (row.getColumns().get("Type").getLabel()), Function.identity()));

        final Row callingSubClassOfSimpleTransactionalClassResult = resultMap.get(JavaxCallingSubClassOfSimpleTransactionalClass.class.getName());
        assertThat(callingSubClassOfSimpleTransactionalClassResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxCallingSubClassOfSimpleTransactionalClass.class));
        assertThat(callingSubClassOfSimpleTransactionalClassResult.getColumns().get("Method").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxCallingSubClassOfSimpleTransactionalClass.class, "anotherMethod"));
        assertThat(callingSubClassOfSimpleTransactionalClassResult.getColumns().get("TransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JavaxCallingSubClassOfSimpleTransactionalClass.class, "void method()"));

        final Row callingSubClassOfSimpleClassWithTransactionalMethodResult = resultMap.get(JavaxCallingSubClassOfSimpleClassWithTransactionalMethod.class.getName());
        assertThat(callingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxCallingSubClassOfSimpleClassWithTransactionalMethod.class));
        assertThat(callingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("Method").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxCallingSubClassOfSimpleClassWithTransactionalMethod.class, "anotherMethod"));
        assertThat(callingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("TransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JavaxCallingSubClassOfSimpleClassWithTransactionalMethod.class, "void method()"));

        final Row overridingSubClassOfSimpleTransactionalClassResult = resultMap.get(JavaxOverridingSubClassOfSimpleTransactionalClass.class.getName());
        assertThat(overridingSubClassOfSimpleTransactionalClassResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxOverridingSubClassOfSimpleTransactionalClass.class));
        assertThat(overridingSubClassOfSimpleTransactionalClassResult.getColumns().get("Method").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxOverridingSubClassOfSimpleTransactionalClass.class, "method"));
        assertThat(overridingSubClassOfSimpleTransactionalClassResult.getColumns().get("TransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxSimpleTransactionalClass.class, "method"));

        final Row overridingSubClassOfSimpleClassWithTransactionalMethodResult = resultMap.get(JavaxOverridingSubClassOfSimpleClassWithTransactionalMethod.class.getName());
        assertThat(overridingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxOverridingSubClassOfSimpleClassWithTransactionalMethod.class));
        assertThat(overridingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("Method").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxOverridingSubClassOfSimpleClassWithTransactionalMethod.class, "method"));
        assertThat(overridingSubClassOfSimpleClassWithTransactionalMethodResult.getColumns().get("TransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxSimpleClassWithTransactionalMethod.class, "method"));

        store.commitTransaction();
    }

    @Test
    void transactionalMethodMustNotBeInvokedFromSameClassGenericInheritanceJakarta() throws Exception {
        scanClasses(JakartaGenericNonTransactionalClass.class, JakartaGenericTransactionalClass.class, JakartaGenericClassWithTransactionalMethod.class,
                JakartaCallingSubClassOfGenericNonTransactionalClass.class, JakartaCallingSubClassOfGenericTransactionalClass.class,
                JakartaCallingSubClassOfGenericClassWithTransactionalMethod.class, JakartaOverridingSubClassOfGenericNonTransactionalClass.class,
                JakartaOverridingSubClassOfGenericTransactionalClass.class, JakartaOverridingSubClassOfGenericClassWithTransactionalMethod.class);
        assertThat(validateConstraint("jee-transaction:TransactionalMethodMustNotBeInvokedFromSameClassOrSubclass").getStatus()).isEqualTo(FAILURE);
        store.beginTransaction();
        final List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations).hasSize(1);
        final Result<Constraint> result = constraintViolations.get(0);
        assertThat(result.getRule().getId()).isEqualTo("jee-transaction:TransactionalMethodMustNotBeInvokedFromSameClassOrSubclass");
        assertThat(result.getRows()).hasSize(4);
        final Map<String, Row> resultMap = result.getRows().stream().collect(Collectors.toMap(row -> (row.getColumns().get("Type").getLabel()), Function.identity()));

        final Row callingSubClassOfGenericClassWithTransactionalMethodResult = resultMap.get(JakartaCallingSubClassOfGenericClassWithTransactionalMethod.class.getName());
        assertThat(callingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaCallingSubClassOfGenericClassWithTransactionalMethod.class));
        assertThat(callingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("Method").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaCallingSubClassOfGenericClassWithTransactionalMethod.class, "anotherMethod"));
        assertThat(callingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("TransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JakartaCallingSubClassOfGenericClassWithTransactionalMethod.class, "void method(java.lang.Object)"));

        final Row callingSubClassOfGenericTransactionalClassResult = resultMap.get(JakartaCallingSubClassOfGenericTransactionalClass.class.getName());
        assertThat(callingSubClassOfGenericTransactionalClassResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaCallingSubClassOfGenericTransactionalClass.class));
        assertThat(callingSubClassOfGenericTransactionalClassResult.getColumns().get("Method").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaCallingSubClassOfGenericTransactionalClass.class, "anotherMethod"));
        assertThat(callingSubClassOfGenericTransactionalClassResult.getColumns().get("TransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JakartaCallingSubClassOfGenericTransactionalClass.class, "void method(java.lang.Object)"));

        final Row overridingSubClassOfGenericTransactionalClassResult = resultMap.get(JakartaOverridingSubClassOfGenericTransactionalClass.class.getName());
        assertThat(overridingSubClassOfGenericTransactionalClassResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaOverridingSubClassOfGenericTransactionalClass.class));
        assertThat(overridingSubClassOfGenericTransactionalClassResult.getColumns().get("Method").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaOverridingSubClassOfGenericTransactionalClass.class, "method", Long.class));
        assertThat(overridingSubClassOfGenericTransactionalClassResult.getColumns().get("TransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaGenericTransactionalClass.class, "method", Object.class));

        final Row overridingSubClassOfGenericClassWithTransactionalMethodResult = resultMap.get(JakartaOverridingSubClassOfGenericClassWithTransactionalMethod.class.getName());
        assertThat(overridingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JakartaOverridingSubClassOfGenericClassWithTransactionalMethod.class));
        assertThat(overridingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("Method").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaOverridingSubClassOfGenericClassWithTransactionalMethod.class, "method", Long.class));
        assertThat(overridingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("TransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JakartaGenericClassWithTransactionalMethod.class, "method", Object.class));

        store.commitTransaction();
    }

    @Test
    void transactionalMethodMustNotBeInvokedFromSameClassGenericInheritanceJavax() throws Exception {
        scanClasses(JavaxGenericNonTransactionalClass.class, JavaxGenericTransactionalClass.class, JavaxGenericClassWithTransactionalMethod.class,
                JavaxCallingSubClassOfGenericNonTransactionalClass.class, JavaxCallingSubClassOfGenericTransactionalClass.class,
                JavaxCallingSubClassOfGenericClassWithTransactionalMethod.class, JavaxOverridingSubClassOfGenericNonTransactionalClass.class,
                JavaxOverridingSubClassOfGenericTransactionalClass.class, JavaxOverridingSubClassOfGenericClassWithTransactionalMethod.class);
        assertThat(validateConstraint("jee-transaction:TransactionalMethodMustNotBeInvokedFromSameClassOrSubclass").getStatus()).isEqualTo(FAILURE);
        store.beginTransaction();
        final List<Result<Constraint>> constraintViolations = new ArrayList<>(reportPlugin.getConstraintResults().values());
        assertThat(constraintViolations).hasSize(1);
        final Result<Constraint> result = constraintViolations.get(0);
        assertThat(result.getRule().getId()).isEqualTo("jee-transaction:TransactionalMethodMustNotBeInvokedFromSameClassOrSubclass");
        assertThat(result.getRows()).hasSize(4);
        final Map<String, Row> resultMap = result.getRows().stream().collect(Collectors.toMap(row -> (row.getColumns().get("Type").getLabel()), Function.identity()));

        final Row callingSubClassOfGenericClassWithTransactionalMethodResult = resultMap.get(JavaxCallingSubClassOfGenericClassWithTransactionalMethod.class.getName());
        assertThat(callingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxCallingSubClassOfGenericClassWithTransactionalMethod.class));
        assertThat(callingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("Method").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxCallingSubClassOfGenericClassWithTransactionalMethod.class, "anotherMethod"));
        assertThat(callingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("TransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JavaxCallingSubClassOfGenericClassWithTransactionalMethod.class, "void method(java.lang.Object)"));

        final Row callingSubClassOfGenericTransactionalClassResult = resultMap.get(JavaxCallingSubClassOfGenericTransactionalClass.class.getName());
        assertThat(callingSubClassOfGenericTransactionalClassResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxCallingSubClassOfGenericTransactionalClass.class));
        assertThat(callingSubClassOfGenericTransactionalClassResult.getColumns().get("Method").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxCallingSubClassOfGenericTransactionalClass.class, "anotherMethod"));
        assertThat(callingSubClassOfGenericTransactionalClassResult.getColumns().get("TransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(simpleMethodDescriptor(JavaxCallingSubClassOfGenericTransactionalClass.class, "void method(java.lang.Object)"));

        final Row overridingSubClassOfGenericTransactionalClassResult = resultMap.get(JavaxOverridingSubClassOfGenericTransactionalClass.class.getName());
        assertThat(overridingSubClassOfGenericTransactionalClassResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxOverridingSubClassOfGenericTransactionalClass.class));
        assertThat(overridingSubClassOfGenericTransactionalClassResult.getColumns().get("Method").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxOverridingSubClassOfGenericTransactionalClass.class, "method", Long.class));
        assertThat(overridingSubClassOfGenericTransactionalClassResult.getColumns().get("TransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxGenericTransactionalClass.class, "method", Object.class));

        final Row overridingSubClassOfGenericClassWithTransactionalMethodResult = resultMap.get(JavaxOverridingSubClassOfGenericClassWithTransactionalMethod.class.getName());
        assertThat(overridingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("Type").getValue()).asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(JavaxOverridingSubClassOfGenericClassWithTransactionalMethod.class));
        assertThat(overridingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("Method").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxOverridingSubClassOfGenericClassWithTransactionalMethod.class, "method", Long.class));
        assertThat(overridingSubClassOfGenericClassWithTransactionalMethodResult.getColumns().get("TransactionalMethod").getValue()).asInstanceOf(type(MethodDescriptor.class))
                .is(methodDescriptor(JavaxGenericClassWithTransactionalMethod.class, "method", Object.class));

        store.commitTransaction();
    }

}
