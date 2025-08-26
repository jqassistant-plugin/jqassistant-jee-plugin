package org.jqassistant.plugin.jee.jsf.test;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.core.rule.api.model.Concept;
import com.buschmais.jqassistant.core.rule.api.model.Constraint;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class JsfIT extends AbstractJavaPluginIT {

    /**
     * Verifies the concept "cdi:JSFManagedBeans".
     *
     * @throws IOException
     *             If the test fails.
     */
    @Test
    void JSFManagedBeanConcept() throws Exception {
        scanClasses(JsfManagedBean.class);
        Result<Concept> conceptResult = applyConcept("jsf:JSFManagedBeans");
        store.beginTransaction();
        assertThat(conceptResult.getStatus(), equalTo(Result.Status.SUCCESS));
        assertThat(conceptResult.getRows().size(), equalTo(1));
        assertThat(conceptResult.getRows().get(0).getColumns().get("JSF Managed Bean").getLabel(), endsWith("JsfManagedBean"));
        store.commitTransaction();
    }

    /**
     * Verifies the constraint "cdi:CDIBeansInsteadOfJSFManagedBeans".
     *
     * @throws IOException
     *             If the test fails.
     */
    @Test
    void JSFManagedBeanConstraint() throws Exception {
        scanClasses(JsfManagedBean.class);
        Result<Constraint> constraintResult = validateConstraint("jsf:CDIBeansInsteadOfJSFManagedBeans");
        store.beginTransaction();
        assertThat(constraintResult.getStatus(), equalTo(Result.Status.FAILURE));
        assertThat(constraintResult.getRows().size(), equalTo(1));
        assertThat(constraintResult.getRows().get(0).getColumns().get("JSF Managed Bean").getLabel(), endsWith("JsfManagedBean"));
        store.commitTransaction();
    }
}
