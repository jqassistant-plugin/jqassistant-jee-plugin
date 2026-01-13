package org.jqassistant.plugin.jee.resource;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.plugin.java.api.model.FieldDescriptor;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.jqassistant.plugin.jee.resource.set.JakartaClassWithResource;
import org.jqassistant.plugin.jee.resource.set.JavaxClassWithResource;
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
 * Tests for resource concepts.
 */
class ResourceIT extends AbstractJavaPluginIT {

    private static Stream<Arguments> classesWithResourceAndQueue() {
        return Stream.of(
                Arguments.of(JavaxClassWithResource.class, javax.jms.Queue.class),
                Arguments.of(JakartaClassWithResource.class, jakarta.jms.Queue.class));
    }

    @ParameterizedTest
    @MethodSource("classesWithResourceAndQueue")
    void resourceField(Class<?> typeWithPersistenceContext, Class<?> resourceType) throws Exception {
        scanClasses(typeWithPersistenceContext);
        final Result<Concept> conceptResult = applyConcept("jee-resource:ResourceField");
        assertThat(conceptResult.getStatus()).isEqualTo(SUCCESS);
        store.beginTransaction();
        final List<FieldDescriptor> members = query("MATCH (e:JEE:InjectionPoint:Resource) RETURN e").getColumn("e");
        assertThat(members).hasSize(1);
        assertThat(members).haveExactly(1, fieldDescriptor(typeWithPersistenceContext, "queue"));
        final List<TypeDescriptor> injectables = query("MATCH (e:Type:Injectable) RETURN e").getColumn("e");
        // Total number of injectables is not asserted, since it might depend on the set of other executed concepts.
        assertThat(injectables).haveExactly(1, typeDescriptor(resourceType));
        store.commitTransaction();
    }



}
