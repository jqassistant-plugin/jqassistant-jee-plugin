package org.jqassistant.plugin.jee.transaction.concept;

import com.buschmais.jqassistant.plugin.java.api.model.MethodDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import org.jqassistant.plugin.jee.transaction.set.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.assertj.MethodDescriptorCondition.methodDescriptor;

class TransactionalMethodIT extends AbstractJavaPluginIT {

    @Test
    void transactionalMethod() throws Exception {
        scanClasses(
                JavaxTransactionalClass.class, JavaxTransactionalMethod.class,
                JavaxStatelessEjb.class, JavaxStatefulEjb.class, JavaxSingletonEjb.class, JavaxMessageDrivenEjb.class,
                JakartaTransactionalClass.class, JakartaTransactionalMethod.class, JakartaStatelessEjb.class,
                JakartaStatefulEjb.class, JakartaSingletonEjb.class, JakartaMessageDrivenEjb.class
                );
        assertThat(applyConcept("jee-transaction:TransactionalMethod").getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        final List<MethodDescriptor> methods = query("MATCH (m:JEE:Method:Transactional) RETURN m").getColumn("m");
        assertThat(methods).hasSize(12);

        // method level annotations
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxTransactionalMethod.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaTransactionalMethod.class, "transactionalMethod"));

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
        store.commitTransaction();
    }
}
