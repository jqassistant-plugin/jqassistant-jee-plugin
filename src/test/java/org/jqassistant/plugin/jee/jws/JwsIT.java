package org.jqassistant.plugin.jee.jws;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import org.jqassistant.plugin.jee.jws.set.jakarta.JakartaWebService;
import org.jqassistant.plugin.jee.jws.set.javax.JavaxWebService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.buschmais.jqassistant.plugin.java.test.assertj.TypeDescriptorCondition.typeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

public class JwsIT extends AbstractJavaPluginIT {

    @ParameterizedTest
    @ValueSource(classes = { JakartaWebService.class, JavaxWebService.class})
    void conceptWebService(Class<?> clazz) throws RuleException {
        scanClasses(clazz);

        final Result<Concept> conceptResult = applyConcept("jws:WebService");
        store.beginTransaction();

        assertThat(conceptResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        assertThat(conceptResult.getRows()).hasSize(1);

        assertThat(conceptResult.getRows().get(0).getColumns().get("WebService").getValue())
                .asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(clazz));

        final List<TypeDescriptor> webServices = query("MATCH (t:JWS:WebService:JEE:Injectable) RETURN t")
                .getColumn("t");
        assertThat(webServices).hasSize(1);
        assertThat(webServices).haveExactly(1, typeDescriptor(clazz));

        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = { JakartaWebService.class, JavaxWebService.class})
    void providedConceptInjectable(Class<?> clazz) throws RuleException {
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
