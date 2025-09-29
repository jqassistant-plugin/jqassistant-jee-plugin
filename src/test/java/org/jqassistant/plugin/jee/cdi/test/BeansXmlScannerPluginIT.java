package org.jqassistant.plugin.jee.cdi.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.buschmais.jqassistant.plugin.java.test.AbstractJavaPluginIT;

import org.jqassistant.plugin.jee.cdi.api.model.BeansXmlDescriptor;
import org.jqassistant.plugin.jee.cdi.test.set.beans.alternative.jakarta.JakartaAlternativeBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.alternative.jakarta.JakartaAlternativeStereotype;
import org.jqassistant.plugin.jee.cdi.test.set.beans.alternative.javax.JavaxAlternativeBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.alternative.javax.JavaxAlternativeStereotype;
import org.jqassistant.plugin.jee.cdi.test.set.beans.decorator.jakarta.JakartaDecoratorBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.decorator.javax.JavaxDecoratorBean;
import org.jqassistant.plugin.jee.cdi.test.set.beans.interceptor.jakarta.JakartaCustomInterceptor;
import org.jqassistant.plugin.jee.cdi.test.set.beans.interceptor.javax.JavaxCustomInterceptor;
import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.plugin.java.test.matcher.TypeDescriptorMatcher.typeDescriptor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

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
    @Test
    void beansDescriptor() {
        scanClassPathDirectory(new File(getClassesDirectory(BeansXmlScannerPluginIT.class), "cdi")); // for XML documents
        store.beginTransaction();
        List<Object> column = query("MATCH (beans:CDI:Beans:Xml:File) RETURN beans").getColumn("beans");
        assertThat(column.size(), equalTo(1));
        BeansXmlDescriptor beansXmlDescriptor = (BeansXmlDescriptor) column.get(0);
        assertThat(beansXmlDescriptor.getFileName(), equalTo("/META-INF/beans.xml"));
        assertThat(beansXmlDescriptor.getVersion(), equalTo("1.1"));
        assertThat(beansXmlDescriptor.getBeanDiscoveryMode(), equalTo("annotated"));
        assertThat(beansXmlDescriptor.getAlternatives(),
                hasItems(typeDescriptor(JavaxAlternativeBean.class), typeDescriptor(JavaxAlternativeStereotype.class),
                        typeDescriptor(JakartaAlternativeBean.class), typeDescriptor(JakartaAlternativeStereotype.class)));
        assertThat(beansXmlDescriptor.getDecorators(), hasItems(typeDescriptor(JavaxDecoratorBean.class), typeDescriptor(JakartaDecoratorBean.class)));
        assertThat(beansXmlDescriptor.getInterceptors(), hasItems(typeDescriptor(JavaxCustomInterceptor.class), typeDescriptor(JakartaCustomInterceptor.class) ));
        store.commitTransaction();
    }

    @Test
    void invalidBeansDescriptor() {
        scanClassPathDirectory(new File(getClassesDirectory(BeansXmlScannerPluginIT.class), "cdi/invalid"));
        store.beginTransaction();
        List<Object> column = query("MATCH (beans:CDI:Beans:Xml:File) RETURN beans").getColumn("beans");
        assertThat(column.size(), equalTo(1));
        BeansXmlDescriptor beansXmlDescriptor = (BeansXmlDescriptor) column.get(0);
        assertThat(beansXmlDescriptor.getFileName(), equalTo("/META-INF/beans.xml"));
        assertThat(beansXmlDescriptor.isXmlWellFormed(), equalTo(false));
        store.commitTransaction();
    }
}
