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
        assertThat(methods).hasSize(104);

        // method level annotations
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxTransactionalMethod.class, "transactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxTransactionalMethod.class, "transactionalMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxTransactionalMethod.class, "anotherTransactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxTransactionalMethod.class, "requiredTransactionalCallingRequiredTransactionalTransitively"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxTransactionalMethod.class, "neverTransactionalCallingRequiredTransactionalTransitively"));

        assertThat(methods).haveExactly(1, methodDescriptor(JakartaTransactionalMethod.class, "transactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaTransactionalMethod.class, "transactionalMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaTransactionalMethod.class, "anotherTransactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaTransactionalMethod.class, "requiredTransactionalCallingRequiredTransactionalTransitively"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaTransactionalMethod.class, "neverTransactionalCallingRequiredTransactionalTransitively"));

        assertThat(methods).haveExactly(1, methodDescriptor(JavaxSimpleClassWithTransactionalMethod.class, "methodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxSimpleClassWithTransactionalMethod.class, "methodWithOverriddenSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxSimpleClassWithTransactionalMethod.class, "methodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxSimpleClassWithTransactionalMethod.class, "methodWithOverriddenSemantics"));

        assertThat(methods).haveExactly(1, methodDescriptor(JakartaSimpleClassWithTransactionalMethod.class, "methodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaSimpleClassWithTransactionalMethod.class, "methodWithOverriddenSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaSimpleClassWithTransactionalMethod.class, "methodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaSimpleClassWithTransactionalMethod.class, "methodWithOverriddenSemantics"));

        assertThat(methods).doNotHave(methodDescriptor(JakartaNonTransactionalSubClassOfTransactionalMethod.class, "transactionalMethodWithRequiredSemantics"));
        assertThat(methods).doNotHave(methodDescriptor(JavaxNonTransactionalSubClassOfTransactionalMethod.class, "transactionalMethodWithRequiredSemantics"));

        // class level annotations
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxTransactionalClass.class, "transactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxTransactionalClass.class, "transactionalMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxTransactionalClass.class, "anotherTransactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxTransactionalClass.class, "requiredTransactionalCallingRequiredTransactionalTransitively"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxTransactionalClass.class, "neverTransactionalCallingRequiredTransactionalTransitively"));

        assertThat(methods).haveExactly(1, methodDescriptor(JakartaTransactionalClass.class, "transactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaTransactionalClass.class, "transactionalMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaTransactionalClass.class, "anotherTransactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaTransactionalClass.class, "requiredTransactionalCallingRequiredTransactionalTransitively"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaTransactionalClass.class, "neverTransactionalCallingRequiredTransactionalTransitively"));

        assertThat(methods).haveExactly(1, methodDescriptor(JavaxStatelessEjb.class, "transactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxStatelessEjb.class, "transactionalMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxStatelessEjb.class, "anotherTransactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxStatelessEjb.class, "requiredTransactionalCallingRequiredTransactionalTransitively"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxStatelessEjb.class, "neverTransactionalCallingRequiredTransactionalTransitively"));

        assertThat(methods).haveExactly(1, methodDescriptor(JakartaStatelessEjb.class, "transactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaStatelessEjb.class, "transactionalMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaStatelessEjb.class, "anotherTransactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaStatelessEjb.class, "requiredTransactionalCallingRequiredTransactionalTransitively"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaStatelessEjb.class, "neverTransactionalCallingRequiredTransactionalTransitively"));

        assertThat(methods).haveExactly(1, methodDescriptor(JavaxStatefulEjb.class, "transactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxStatefulEjb.class, "transactionalMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxStatefulEjb.class, "anotherTransactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxStatefulEjb.class, "requiredTransactionalCallingRequiredTransactionalTransitively"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxStatefulEjb.class, "neverTransactionalCallingRequiredTransactionalTransitively"));

        assertThat(methods).haveExactly(1, methodDescriptor(JakartaStatefulEjb.class, "transactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaStatefulEjb.class, "transactionalMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaStatefulEjb.class, "anotherTransactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaStatefulEjb.class, "requiredTransactionalCallingRequiredTransactionalTransitively"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaStatefulEjb.class, "neverTransactionalCallingRequiredTransactionalTransitively"));

        assertThat(methods).haveExactly(1, methodDescriptor(JavaxSingletonEjb.class, "transactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxSingletonEjb.class, "transactionalMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxSingletonEjb.class, "anotherTransactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxSingletonEjb.class, "requiredTransactionalCallingRequiredTransactionalTransitively"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxSingletonEjb.class, "neverTransactionalCallingRequiredTransactionalTransitively"));

        assertThat(methods).haveExactly(1, methodDescriptor(JakartaSingletonEjb.class, "transactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaSingletonEjb.class, "transactionalMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaSingletonEjb.class, "anotherTransactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaSingletonEjb.class, "requiredTransactionalCallingRequiredTransactionalTransitively"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaSingletonEjb.class, "neverTransactionalCallingRequiredTransactionalTransitively"));

        assertThat(methods).haveExactly(1, methodDescriptor(JavaxMessageDrivenEjb.class, "transactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxMessageDrivenEjb.class, "transactionalMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxMessageDrivenEjb.class, "anotherTransactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxMessageDrivenEjb.class, "requiredTransactionalCallingRequiredTransactionalTransitively"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxMessageDrivenEjb.class, "neverTransactionalCallingRequiredTransactionalTransitively"));

        assertThat(methods).haveExactly(1, methodDescriptor(JakartaMessageDrivenEjb.class, "transactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaMessageDrivenEjb.class, "transactionalMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaMessageDrivenEjb.class, "anotherTransactionalMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaMessageDrivenEjb.class, "requiredTransactionalCallingRequiredTransactionalTransitively"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaMessageDrivenEjb.class, "neverTransactionalCallingRequiredTransactionalTransitively"));

        assertThat(methods).haveExactly(1, methodDescriptor(JavaxCallingSubClassOfSimpleClassWithTransactionalMethod.class, "anotherMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxCallingSubClassOfSimpleClassWithTransactionalMethod.class, "transactionalMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, simpleMethodDescriptor(JavaxCallingSubClassOfSimpleClassWithTransactionalMethod.class, "void methodWithRequiredSemantics()"));

        assertThat(methods).haveExactly(1, methodDescriptor(JakartaCallingSubClassOfSimpleClassWithTransactionalMethod.class, "anotherMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaCallingSubClassOfSimpleClassWithTransactionalMethod.class, "transactionalMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, simpleMethodDescriptor(JakartaCallingSubClassOfSimpleClassWithTransactionalMethod.class, "void methodWithRequiredSemantics()"));

        assertThat(methods).haveExactly(1, methodDescriptor(JavaxCallingSubClassOfSimpleTransactionalClass.class, "anotherMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxCallingSubClassOfSimpleTransactionalClass.class, "transactionalMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, simpleMethodDescriptor(JavaxCallingSubClassOfSimpleTransactionalClass.class, "void methodWithRequiredSemantics()"));

        assertThat(methods).haveExactly(1, methodDescriptor(JakartaCallingSubClassOfSimpleTransactionalClass.class, "anotherMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaCallingSubClassOfSimpleTransactionalClass.class, "transactionalMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, simpleMethodDescriptor(JakartaCallingSubClassOfSimpleTransactionalClass.class, "void methodWithRequiredSemantics()"));

        assertThat(methods).haveExactly(1, methodDescriptor(JavaxSimpleTransactionalClass.class, "methodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxSimpleTransactionalClass.class, "methodWithOverriddenSemantics"));

        assertThat(methods).haveExactly(1, methodDescriptor(JakartaSimpleTransactionalClass.class, "methodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaSimpleTransactionalClass.class, "methodWithOverriddenSemantics"));

        assertThat(methods).haveExactly(1, methodDescriptor(JakartaGenericTransactionalClass.class, "methodWithRequiredSemantics", Object.class));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaGenericTransactionalClass.class, "methodWithOverriddenSemantics", Object.class));

        assertThat(methods).haveExactly(1, methodDescriptor(JavaxGenericTransactionalClass.class, "methodWithRequiredSemantics", Object.class));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxGenericTransactionalClass.class, "methodWithOverriddenSemantics", Object.class));

        assertThat(methods).haveExactly(1, methodDescriptor(JakartaCallingSubClassOfGenericTransactionalClass.class, "anotherMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaCallingSubClassOfGenericTransactionalClass.class, "callingMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, simpleMethodDescriptor(JakartaCallingSubClassOfGenericTransactionalClass.class, "void methodWithOverriddenSemantics(java.lang.Object)"));

        assertThat(methods).haveExactly(1, methodDescriptor(JavaxCallingSubClassOfGenericTransactionalClass.class, "anotherMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxCallingSubClassOfGenericTransactionalClass.class, "callingMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, simpleMethodDescriptor(JavaxCallingSubClassOfGenericTransactionalClass.class, "void methodWithOverriddenSemantics(java.lang.Object)"));

        assertThat(methods).haveExactly(1, methodDescriptor(JakartaGenericClassWithTransactionalMethod.class, "methodWithRequiredSemantics", Object.class));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaGenericClassWithTransactionalMethod.class, "methodWithOverriddenSemantics", Object.class));

        assertThat(methods).haveExactly(1, methodDescriptor(JavaxGenericClassWithTransactionalMethod.class, "methodWithRequiredSemantics", Object.class));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxGenericClassWithTransactionalMethod.class, "methodWithOverriddenSemantics", Object.class));

        assertThat(methods).haveExactly(1, methodDescriptor(JakartaCallingSubClassOfGenericClassWithTransactionalMethod.class, "anotherMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaCallingSubClassOfGenericClassWithTransactionalMethod.class, "callingMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, simpleMethodDescriptor(JakartaCallingSubClassOfGenericClassWithTransactionalMethod.class, "void methodWithOverriddenSemantics(java.lang.Object)"));

        assertThat(methods).haveExactly(1, methodDescriptor(JavaxCallingSubClassOfGenericClassWithTransactionalMethod.class, "anotherMethodWithRequiredSemantics"));
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxCallingSubClassOfGenericClassWithTransactionalMethod.class, "callingMethodWithNeverSemantics"));
        assertThat(methods).haveExactly(1, simpleMethodDescriptor(JavaxCallingSubClassOfGenericClassWithTransactionalMethod.class, "void methodWithOverriddenSemantics(java.lang.Object)"));

        store.commitTransaction();
    }
}
