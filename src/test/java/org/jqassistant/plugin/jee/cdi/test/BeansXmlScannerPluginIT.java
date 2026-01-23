package org.jqassistant.plugin.jee.cdi.test;

import java.io.File;
import java.io.IOException;
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

import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.params.provider.Arguments.of;

/**
 * Tests for the CDI concepts.
 */
class BeansXmlScannerPluginIT extends AbstractJavaPluginIT {

    /**
     * Verifies scanning of the beans descriptor.
     *
     * @throws IOException
     *         If the test fails.
     */
    @ParameterizedTest
    @MethodSource("beansXmlParameters")
    void beansDescriptor(String resourceRoot, String expectedVersion, Class<?> expectedAltenativeBean, Class<?> expectedAlternativeStereotype,
            Class<?> expectedDecorator, Class<?> expectedInterceptor) {
        scanClassPathDirectory(new File(getClassesDirectory(BeansXmlScannerPluginIT.class), resourceRoot)); // for XML documents
        store.beginTransaction();
        List<Object> column = query("MATCH (beans:CDI:Beans:Xml:File) RETURN beans").getColumn("beans");
        assertThat(column.size(), equalTo(1));
        BeansXmlDescriptor beansXmlDescriptor = (BeansXmlDescriptor) column.get(0);
        assertThat(beansXmlDescriptor.getFileName(), equalTo("/META-INF/beans.xml"));
        assertThat(beansXmlDescriptor.getVersion(), equalTo(expectedVersion));
        assertThat(beansXmlDescriptor.getBeanDiscoveryMode(), equalTo("annotated"));
        assertThat(beansXmlDescriptor.getAlternatives(), hasItems(typeDescriptor(expectedAltenativeBean), typeDescriptor(expectedAlternativeStereotype)));
        assertThat(beansXmlDescriptor.getDecorators(), hasItems(typeDescriptor(expectedDecorator)));
        assertThat(beansXmlDescriptor.getInterceptors(), hasItems(typeDescriptor(expectedInterceptor)));
        store.commitTransaction();
    }

    static Stream<Arguments> beansXmlParameters() {
        return Stream.of( //
                of("cdi/1_1", "1.1", JavaxAlternativeBean.class, JavaxAlternativeStereotype.class, JavaxDecoratorBean.class,
                        JavaxCustomInterceptor.class), //
                of("cdi/5_0", "5.0", JakartaAlternativeBean.class, JakartaAlternativeStereotype.class, JakartaDecoratorBean.class,
                        JakartaCustomInterceptor.class));
    }

    @Test
    void empty() {
        scanClassPathDirectory(new File(getClassesDirectory(BeansXmlScannerPluginIT.class), "cdi/empty"));
        store.beginTransaction();
        List<Object> column = query("MATCH (beans:CDI:Beans:Xml:File) RETURN beans").getColumn("beans");
        assertThat(column.size(), equalTo(1));
        BeansXmlDescriptor beansXmlDescriptor = (BeansXmlDescriptor) column.get(0);
        assertThat(beansXmlDescriptor.getFileName(), equalTo("/META-INF/beans.xml"));
        assertThat(beansXmlDescriptor.isXmlWellFormed(), equalTo(false));
        store.commitTransaction();
    }
}
