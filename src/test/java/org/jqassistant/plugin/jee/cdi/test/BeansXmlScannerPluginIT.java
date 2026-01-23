package org.jqassistant.plugin.jee.cdi.test;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.jqassistant.plugin.jee.api.model.cdi.BeansXmlDescriptor;
import org.jqassistant.plugin.jee.cdi.test.set.beans.alternative.jakarta.JakartaAlternativeBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.alternative.jakarta.JakartaAlternativeStereotype;
import org.jqassistant.plugin.jee.cdi.test.set.beans.alternative.javax.JavaxAlternativeBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.alternative.javax.JavaxAlternativeStereotype;
import org.jqassistant.plugin.jee.cdi.test.set.beans.decorator.jakarta.JakartaDecoratorBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.decorator.javax.JavaxDecoratorBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.interceptor.jakarta.JakartaCustomInterceptor;
import org.jqassistant.plugin.jee.cdi.test.set.beans.interceptor.javax.JavaxCustomInterceptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static com.buschmais.jqassistant.plugin.java.test.assertj.TypeDescriptorCondition.typeDescriptor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

/**
 * Tests for the CDI concepts.
 */
class BeansXmlScannerPluginIT extends AbstractJavaPluginIT {

    /**
     * Verifies scanning of the beans.xml descriptors containing alternatives, decorators and interceptors.
     */
    @ParameterizedTest
    @MethodSource("beansXmlParameters")
    void beansDescriptor(String resourceRoot, String expectedVersion, Class<?> expectedAlternativeBean, Class<?> expectedAlternativeStereotype,
            Class<?> expectedDecorator, Class<?> expectedInterceptor) {
        scanClassPathDirectory(new File(getClassesDirectory(BeansXmlScannerPluginIT.class), resourceRoot)); // for XML documents
        store.beginTransaction();
        List<Object> column = query("MATCH (beans:CDI:Beans:Xml:File) RETURN beans").getColumn("beans");
        assertThat(column.size()).isEqualTo(1);
        BeansXmlDescriptor beansXmlDescriptor = (BeansXmlDescriptor) column.get(0);
        assertThat(beansXmlDescriptor.getFileName()).isEqualTo("/META-INF/beans.xml");
        assertThat(beansXmlDescriptor.getVersion()).isEqualTo(expectedVersion);
        assertThat(beansXmlDescriptor.getBeanDiscoveryMode()).isEqualTo("annotated");
        assertThat(beansXmlDescriptor.getAlternatives()).hasSize(2)
                .haveExactly(1, typeDescriptor(expectedAlternativeBean))
                .haveExactly(1, typeDescriptor(expectedAlternativeStereotype));
        assertThat(beansXmlDescriptor.getDecorators()).hasSize(1)
                .haveExactly(1, typeDescriptor(expectedDecorator));
        assertThat(beansXmlDescriptor.getInterceptors()).hasSize(1)
                .haveExactly(1, typeDescriptor(expectedInterceptor));
        store.commitTransaction();
    }

    static Stream<Arguments> beansXmlParameters() {
        return Stream.of( //
                of("cdi/1_1", "1.1", JavaxAlternativeBean.class, JavaxAlternativeStereotype.class, JavaxDecoratorBean.class, JavaxCustomInterceptor.class), //
                of("cdi/5_0", "5.0", JakartaAlternativeBean.class, JakartaAlternativeStereotype.class, JakartaDecoratorBean.class,
                        JakartaCustomInterceptor.class));
    }

    /**
     * Verifies scanning of an empty beans.xml descriptor.
     */
    @Test
    void empty() {
        scanClassPathDirectory(new File(getClassesDirectory(BeansXmlScannerPluginIT.class), "cdi/empty"));
        store.beginTransaction();
        List<Object> column = query("MATCH (beans:CDI:Beans:Xml:File) RETURN beans").getColumn("beans");
        assertThat(column.size()).isEqualTo(1);
        BeansXmlDescriptor beansXmlDescriptor = (BeansXmlDescriptor) column.get(0);
        assertThat(beansXmlDescriptor.getFileName()).isEqualTo("/META-INF/beans.xml");
        assertThat(beansXmlDescriptor.isXmlWellFormed()).isTrue();
        store.commitTransaction();
    }

    /**
     * Verifies scanning of an invalid beans.xml descriptor which must not fail as first CDI revisions allowed empty marker files.
     */
    @Test
    void invalid() {
        scanClassPathDirectory(new File(getClassesDirectory(BeansXmlScannerPluginIT.class), "cdi/invalid"));
        store.beginTransaction();
        List<Object> column = query("MATCH (beans:CDI:Beans:Xml:File) RETURN beans").getColumn("beans");
        assertThat(column.size()).isEqualTo(1);
        BeansXmlDescriptor beansXmlDescriptor = (BeansXmlDescriptor) column.get(0);
        assertThat(beansXmlDescriptor.getFileName()).isEqualTo("/META-INF/beans.xml");
        assertThat(beansXmlDescriptor.isXmlWellFormed()).isEqualTo(false);
        store.commitTransaction();
    }
}
