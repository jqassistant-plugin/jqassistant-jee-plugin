package org.jqassistant.plugin.jee.cdi.test;

import com.buschmais.jqassistant.core.report.api.model.Result;
import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.jqassistant.plugin.jee.cdi.test.set.beans.event.CustomEventConsumer;
import org.jqassistant.plugin.jee.cdi.test.set.beans.event.CustomEventProducer;
import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

/**
 * Tests for CDI event concepts.
 *
 * @author Aparna Chaudhary
 */
class CdiEventIT extends AbstractJavaPluginIT {

    /**
     * Verifies the concept "cdi:EventProducer".
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void eventProducerConcept() throws Exception {
        scanClasses(CustomEventProducer.class);
        assertThat(applyConcept("cdi:EventProducer").getStatus(), equalTo(Result.Status.SUCCESS));
        store.beginTransaction();
        assertThat("Expected EventProducer", query("MATCH (e:Type:Cdi:EventProducer) RETURN e").getColumn("e"), hasItem(typeDescriptor(CustomEventProducer.class)));
        store.commitTransaction();
    }

    /**
     * Verifies the concept {@code cdi:EventProducer} is not applied to invalid EventProducer classes.
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void testInvalid_EventProducer_Concept() throws Exception {
        scanClasses(CdiEventIT.class);
        assertThat(applyConcept("cdi:EventProducer").getStatus(), equalTo(Result.Status.WARNING));
        store.beginTransaction();
        assertThat("Unexpected EventProducer", query("MATCH (e:Type:Cdi:EventProducer) RETURN e").getRows(), empty());
        store.commitTransaction();
    }

    /**
     * Verifies the concept "cdi:EventConsumer".
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void eventConsumerConcept() throws Exception {
        scanClasses(CustomEventConsumer.class);
        assertThat(applyConcept("cdi:EventConsumer").getStatus(), equalTo(Result.Status.SUCCESS));
        store.beginTransaction();
        assertThat("Expected EventConsumer", query("MATCH (e:Type:Cdi:EventConsumer) RETURN e").getColumn("e"), hasItem(typeDescriptor(CustomEventConsumer.class)));
        store.commitTransaction();
    }

    /**
     * Verifies the concept {@code cdi:EventConsumer} is not applied to invalid EventConsumer classes.
     *
     * @throws java.io.IOException
     *             If the test fails.
     */
    @Test
    void testInvalid_EventConsumer_Concept() throws Exception {
        scanClasses(CdiEventIT.class);
        assertThat(applyConcept("cdi:EventConsumer").getStatus(), equalTo(Result.Status.WARNING));
        store.beginTransaction();
        assertThat("Unexpected EventConsumer", query("MATCH (e:Type:Cdi:EventConsumer) RETURN e").getRows(), empty());
        store.commitTransaction();
    }
}
