package org.jqassistant.plugin.jee.jpa.test;

import com.buschmais.jqassistant.plugin.java.api.model.FieldDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import org.jqassistant.plugin.jee.jpa.test.set.persistencecontext.jakarta.JakartaTypeWithPersistenceContext;
import org.jqassistant.plugin.jee.jpa.test.set.persistencecontext.javax.JavaxTypeWithPersistenceContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.buschmais.jqassistant.core.report.api.model.Result.Status.SUCCESS;
import static com.buschmais.jqassistant.plugin.java.test.assertj.FieldDescriptorCondition.fieldDescriptor;
import static com.buschmais.jqassistant.plugin.java.test.assertj.TypeDescriptorCondition.typeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * Tests for JPA concepts.
 */
class JpaConceptsIT extends AbstractJavaPluginIT {

    @ParameterizedTest
    @MethodSource("classesWithPersistenceContextAndEntityManager")
    void persistenceContext(Class<?> typeWithPersistenceContext, Class<?> entityManager) throws Exception {
        scanClasses(typeWithPersistenceContext);
        assertThat(applyConcept("jpa:PersistenceContextField").getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        List<FieldDescriptor> members = query("MATCH (e:JPA:PersistenceContext:InjectionPoint:JEE) RETURN e").getColumn("e");
        assertThat(members).haveExactly(1, fieldDescriptor(typeWithPersistenceContext, "entityManager"));
        final List<TypeDescriptor> injectables = query("MATCH (e:Type:Injectable) RETURN e").getColumn("e");
        assertThat(injectables).haveExactly(1, typeDescriptor(entityManager));
        store.commitTransaction();
    }

    private static Stream<Arguments> classesWithPersistenceContextAndEntityManager() {
        return Stream.of(
                Arguments.of(JavaxTypeWithPersistenceContext.class, javax.persistence.EntityManager.class),
                Arguments.of(JakartaTypeWithPersistenceContext.class, jakarta.persistence.EntityManager.class));
    }

}
