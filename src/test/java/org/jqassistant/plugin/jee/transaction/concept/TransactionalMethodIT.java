package org.jqassistant.plugin.jee.transaction.concept;

import com.buschmais.jqassistant.plugin.java.api.model.MethodDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import org.jqassistant.plugin.jee.transaction.set.*;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta.JakartaCallingSubClassOfGenericClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta.JakartaCallingSubClassOfGenericTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta.JakartaGenericClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.jakarta.JakartaGenericTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxCallingSubClassOfGenericClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxCallingSubClassOfGenericTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxGenericClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.generic.javax.JavaxGenericTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta.JakartaCallingSubClassOfSimpleClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta.JakartaCallingSubClassOfSimpleTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta.JakartaSimpleClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.jakarta.JakartaSimpleTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax.JavaxCallingSubClassOfSimpleClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax.JavaxCallingSubClassOfSimpleTransactionalClass;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax.JavaxSimpleClassWithTransactionalMethod;
import org.jqassistant.plugin.jee.transaction.set.inheritance.simple.javax.JavaxSimpleTransactionalClass;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.assertj.MethodDescriptorCondition.methodDescriptor;
import static org.jqassistant.plugin.jee.SimpleMethodDescriptorCondition.simpleMethodDescriptor;

class TransactionalMethodIT extends AbstractJavaPluginIT {

    @Test
    void transactionalMethod() throws Exception {
        scanClasses(
                JavaxTransactionalClass.class, JavaxTransactionalMethod.class,
                JavaxStatelessEjb.class, JavaxStatefulEjb.class, JavaxSingletonEjb.class, JavaxMessageDrivenEjb.class,
                JavaxCallingSubClassOfSimpleClassWithTransactionalMethod.class, JavaxCallingSubClassOfSimpleTransactionalClass.class,
                JavaxSimpleClassWithTransactionalMethod.class, JavaxSimpleTransactionalClass.class,
                JakartaTransactionalClass.class, JakartaTransactionalMethod.class, JakartaStatelessEjb.class,
                JakartaStatefulEjb.class, JakartaSingletonEjb.class, JakartaMessageDrivenEjb.class,
                JakartaCallingSubClassOfSimpleClassWithTransactionalMethod.class, JakartaCallingSubClassOfSimpleTransactionalClass.class,
                JakartaSimpleClassWithTransactionalMethod.class, JakartaSimpleTransactionalClass.class,
                JakartaNonTransactionalSubClassOfTransactionalMethod.class, JavaxNonTransactionalSubClassOfTransactionalMethod.class,
                JakartaGenericTransactionalClass.class, JavaxGenericTransactionalClass.class,
                JakartaCallingSubClassOfGenericTransactionalClass.class, JavaxCallingSubClassOfGenericTransactionalClass.class,
                JakartaGenericClassWithTransactionalMethod.class, JavaxGenericClassWithTransactionalMethod.class,
                JakartaCallingSubClassOfGenericClassWithTransactionalMethod.class, JavaxCallingSubClassOfGenericClassWithTransactionalMethod.class
                );
        assertThat(applyConcept("jee-transaction:TransactionalMethod").getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        final List<MethodDescriptor> methods = query("MATCH (m:JEE:Method:Transactional) RETURN m").getColumn("m");
        assertThat(methods).hasSize(36);

        // method level annotations
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxTransactionalMethod.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaTransactionalMethod.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxSimpleClassWithTransactionalMethod.class, "method"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaSimpleClassWithTransactionalMethod.class, "method"));
        assertThat(methods).doNotHave(methodDescriptor(JakartaNonTransactionalSubClassOfTransactionalMethod.class, "transactionalMethod"));
        assertThat(methods).doNotHave(methodDescriptor(JavaxNonTransactionalSubClassOfTransactionalMethod.class, "transactionalMethod"));

        // class level annotations
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxTransactionalClass.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaTransactionalClass.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxStatelessEjb.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaStatelessEjb.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxStatefulEjb.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaStatefulEjb.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxSingletonEjb.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaSingletonEjb.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxMessageDrivenEjb.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaMessageDrivenEjb.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxCallingSubClassOfSimpleClassWithTransactionalMethod.class, "anotherMethod"));
        assertThat(methods).haveExactly(1, simpleMethodDescriptor(JavaxCallingSubClassOfSimpleClassWithTransactionalMethod.class, "void method()"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaCallingSubClassOfSimpleClassWithTransactionalMethod.class, "anotherMethod"));
        assertThat(methods).haveExactly(1, simpleMethodDescriptor(JakartaCallingSubClassOfSimpleClassWithTransactionalMethod.class, "void method()"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxCallingSubClassOfSimpleTransactionalClass.class, "anotherMethod"));
        assertThat(methods).haveExactly(1, simpleMethodDescriptor(JavaxCallingSubClassOfSimpleTransactionalClass.class, "void method()"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaCallingSubClassOfSimpleTransactionalClass.class, "anotherMethod"));
        assertThat(methods).haveExactly(1, simpleMethodDescriptor(JakartaCallingSubClassOfSimpleTransactionalClass.class, "void method()"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxSimpleTransactionalClass.class, "method"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaSimpleTransactionalClass.class, "method"));

        assertThat(methods).haveExactly(1, methodDescriptor(JakartaGenericTransactionalClass.class, "method", Object.class));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxGenericTransactionalClass.class, "method", Object.class));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaCallingSubClassOfGenericTransactionalClass.class, "anotherMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxCallingSubClassOfGenericTransactionalClass.class, "anotherMethod"));
        assertThat(methods).haveExactly(1, simpleMethodDescriptor(JakartaCallingSubClassOfGenericTransactionalClass.class, "void method(java.lang.Object)"));
        assertThat(methods).haveExactly(1, simpleMethodDescriptor(JavaxCallingSubClassOfGenericTransactionalClass.class, "void method(java.lang.Object)"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaGenericClassWithTransactionalMethod.class, "method", Object.class));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxGenericClassWithTransactionalMethod.class, "method", Object.class));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaCallingSubClassOfGenericClassWithTransactionalMethod.class, "anotherMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxCallingSubClassOfGenericClassWithTransactionalMethod.class, "anotherMethod"));
        assertThat(methods).haveExactly(1, simpleMethodDescriptor(JakartaCallingSubClassOfGenericClassWithTransactionalMethod.class, "void method(java.lang.Object)"));
        assertThat(methods).haveExactly(1, simpleMethodDescriptor(JavaxCallingSubClassOfGenericClassWithTransactionalMethod.class, "void method(java.lang.Object)"));

        store.commitTransaction();
    }
}
