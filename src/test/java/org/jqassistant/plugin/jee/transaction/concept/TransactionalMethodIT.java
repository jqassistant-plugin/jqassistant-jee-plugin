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
                JakartaTransactionalClass.class, JakartaTransactionalMethod.class,
                JavaxTransactionalClass.class, JavaxTransactionalMethod.class,
                StatelessEjb.class, StatefulEjb.class, SingletonEjb.class, MessageDrivenEjb.class
                );
        assertThat(applyConcept("jee-transaction:TransactionalMethod").getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        final List<MethodDescriptor> methods = query("MATCH (m:JavaEE:Method:Transactional) RETURN m").getColumn("m");
        assertThat(methods).hasSize(8);

        // method level annotations
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxTransactionalMethod.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaTransactionalMethod.class, "transactionalMethod"));

        // class level annotations
        assertThat(methods).haveExactly(1, methodDescriptor(JavaxTransactionalClass.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(JakartaTransactionalClass.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(StatelessEjb.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(StatefulEjb.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(SingletonEjb.class, "transactionalMethod"));
        assertThat(methods).haveExactly(1, methodDescriptor(MessageDrivenEjb.class, "transactionalMethod"));
        store.commitTransaction();
    }
}
