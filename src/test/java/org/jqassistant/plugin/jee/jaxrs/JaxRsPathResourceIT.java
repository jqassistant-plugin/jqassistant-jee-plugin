package org.jqassistant.plugin.jee.jaxrs;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import org.jqassistant.plugin.jee.jaxrs.set.jakarta.JakartaPathMethod;
import org.jqassistant.plugin.jee.jaxrs.set.jakarta.JakartaPathType;
import org.jqassistant.plugin.jee.jaxrs.set.javax.JavaxPathMethod;
import org.jqassistant.plugin.jee.jaxrs.set.javax.JavaxPathType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.buschmais.jqassistant.plugin.java.test.assertj.TypeDescriptorCondition.typeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

public class JaxRsPathResourceIT extends AbstractJavaPluginIT {

    @ParameterizedTest
    @ValueSource(classes = { JakartaPathType.class, JavaxPathType.class})
    void pathType(Class<?> clazz) throws RuleException {
        scanClasses(clazz);

        final Result<Concept> conceptResult = applyConcept("jax-rs:PathResource");
        store.beginTransaction();

        assertThat(conceptResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        assertThat(conceptResult.getRows()).hasSize(1);

        assertThat(conceptResult.getRows().get(0).getColumns().get("PathResource").getValue())
                .asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(clazz));

        final List<TypeDescriptor> paths = query("MATCH (t:JaxRS:PathResource:JEE:Injectable) RETURN t")
                .getColumn("t");
        assertThat(paths).hasSize(1);
        assertThat(paths).haveExactly(1, typeDescriptor(clazz));

        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = { JakartaPathMethod.class, JavaxPathMethod.class})
    void pathMethod(Class<?> clazz) throws RuleException {
        scanClasses(clazz);

        final Result<Concept> conceptResult = applyConcept("jax-rs:PathResource");
        store.beginTransaction();

        assertThat(conceptResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        assertThat(conceptResult.getRows()).hasSize(1);

        assertThat(conceptResult.getRows().get(0).getColumns().get("PathResource").getValue())
                .asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(clazz));

        final List<TypeDescriptor> paths = query("MATCH (t:JaxRS:PathResource:JEE:Injectable) RETURN t")
                .getColumn("t");
        assertThat(paths).hasSize(1);
        assertThat(paths).haveExactly(1, typeDescriptor(clazz));

        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = { JakartaPathType.class, JavaxPathType.class})
    void providedConceptInjectablePathType(Class<?> clazz) throws RuleException {
        scanClasses(clazz);

        final Result<Concept> conceptResult = applyConcept("jee-injection:Injectable");
        store.beginTransaction();

        assertThat(conceptResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        assertThat(conceptResult.getRows()).hasSize(1);

        assertThat(conceptResult.getRows().get(0).getColumns().get("Injectable").getValue())
                .asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(clazz));

        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = { JakartaPathMethod.class, JavaxPathMethod.class})
    void providedConceptInjectablePathMethod(Class<?> clazz) throws RuleException {
        scanClasses(clazz);

        final Result<Concept> conceptResult = applyConcept("jee-injection:Injectable");
        store.beginTransaction();

        assertThat(conceptResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        assertThat(conceptResult.getRows()).hasSize(1);

        assertThat(conceptResult.getRows().get(0).getColumns().get("Injectable").getValue())
                .asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(clazz));

        store.commitTransaction();
    }
}
