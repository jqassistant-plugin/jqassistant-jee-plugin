package org.jqassistant.plugin.jee.jaxrs;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import org.jqassistant.plugin.jee.jaxrs.set.jakarta.JakartaProviderType;
import org.jqassistant.plugin.jee.jaxrs.set.javax.JavaxProviderType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.buschmais.jqassistant.plugin.java.test.assertj.TypeDescriptorCondition.typeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

public class JaxRsProviderIT extends AbstractJavaPluginIT {

    @ParameterizedTest
    @ValueSource(classes = { JakartaProviderType.class, JavaxProviderType.class})
    void provider(Class<?> clazz) throws RuleException {
        scanClasses(clazz);

        final Result<Concept> conceptResult = applyConcept("jax-rs:Provider");
        store.beginTransaction();

        assertThat(conceptResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        assertThat(conceptResult.getRows()).hasSize(1);

        assertThat(conceptResult.getRows().get(0).getColumns().get("Provider").getValue())
                .asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(clazz));

        final List<TypeDescriptor> paths = query("MATCH (t:JaxRS:Provider:JEE:Injectable) RETURN t")
                .getColumn("t");
        assertThat(paths).hasSize(1);
        assertThat(paths).haveExactly(1, typeDescriptor(clazz));

        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = { JakartaProviderType.class, JavaxProviderType.class})
    void providedConceptInjectableProvider(Class<?> clazz) throws RuleException {
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
