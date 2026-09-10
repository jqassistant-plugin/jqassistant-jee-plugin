package org.jqassistant.plugin.jee.servlet;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.core.rule.api.model.RuleException;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;
import org.jqassistant.plugin.jee.servlet.set.jakarta.JakartaWebFilter;
import org.jqassistant.plugin.jee.servlet.set.jakarta.JakartaWebServlet;
import org.jqassistant.plugin.jee.servlet.set.javax.JavaxWebFilter;
import org.jqassistant.plugin.jee.servlet.set.javax.JavaxWebServlet;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.buschmais.jqassistant.plugin.java.test.assertj.TypeDescriptorCondition.typeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;

public class ServletIT extends AbstractJavaPluginIT {

    @ParameterizedTest
    @ValueSource(classes = { JakartaWebFilter.class, JavaxWebFilter.class})
    void conceptWebFilter(Class<?> clazz) throws RuleException {
        scanClasses(clazz);

        final Result<Concept> conceptResult = applyConcept("servlet:WebFilter");
        store.beginTransaction();

        assertThat(conceptResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        assertThat(conceptResult.getRows()).hasSize(1);

        assertThat(conceptResult.getRows().get(0).getColumns().get("WebFilter").getValue())
                .asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(clazz));

        final List<TypeDescriptor> webFilters = query("MATCH (t:ServletAPI:Web:Filter:JEE:Injectable) RETURN t")
                .getColumn("t");
        assertThat(webFilters).hasSize(1);
        assertThat(webFilters).haveExactly(1, typeDescriptor(clazz));

        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = { JakartaWebServlet.class, JavaxWebServlet.class})
    void conceptWebServlet(Class<?> clazz) throws RuleException {
        scanClasses(clazz);

        final Result<Concept> conceptResult = applyConcept("servlet:WebServlet");
        store.beginTransaction();

        assertThat(conceptResult.getStatus()).isEqualTo(Result.Status.SUCCESS);
        assertThat(conceptResult.getRows()).hasSize(1);

        assertThat(conceptResult.getRows().get(0).getColumns().get("WebServlet").getValue())
                .asInstanceOf(type(TypeDescriptor.class))
                .is(typeDescriptor(clazz));

        final List<TypeDescriptor> webFilters = query("MATCH (t:ServletAPI:Web:Servlet:JEE:Injectable) RETURN t")
                .getColumn("t");
        assertThat(webFilters).hasSize(1);
        assertThat(webFilters).haveExactly(1, typeDescriptor(clazz));

        store.commitTransaction();
    }

    @ParameterizedTest
    @ValueSource(classes = { JakartaWebFilter.class, JavaxWebFilter.class, JakartaWebServlet.class, JavaxWebServlet.class})
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
