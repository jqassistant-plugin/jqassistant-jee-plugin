package org.jqassistant.plugin.jee.jee6.test.scanner;

import java.io.File;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import com.buschmais.jqassistant.plugin.java.api.scanner.ArtifactScopedTypeResolver;
import com.buschmais.jqassistant.plugin.java.api.scanner.TypeResolver;

import org.jqassistant.plugin.jee.jee6.api.model.ApplicationXmlDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.WebApplicationArchiveDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.WebXmlDescriptor;
import org.jqassistant.plugin.jee.jee6.api.scanner.EnterpriseApplicationScope;
import org.jqassistant.plugin.jee.jee6.api.scanner.WebApplicationScope;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Contains integration tests for JavaEE XML descriptors.
 */
class XmlDescriptorScannerPluginIT extends AbstractPluginIT {

    /**
     * Verify scanning of web.xml descriptors.
     */
    @Test
    void webXml() {
        store.beginTransaction();
        Scanner scanner = getScanner();
        WebApplicationArchiveDescriptor warDescriptor = store.create(WebApplicationArchiveDescriptor.class);
        scanner.getContext().push(TypeResolver.class, new ArtifactScopedTypeResolver(warDescriptor));
        File webXml = new File(getClassesDirectory(XmlDescriptorScannerPluginIT.class), "jee6/WEB-INF/web.xml");
        WebXmlDescriptor descriptor = scanner.scan(webXml, "/WEB-INF/web.xml", WebApplicationScope.WAR);
        assertThat(descriptor.getVersion(), equalTo("3.0"));
        scanner.getContext().pop(TypeResolver.class);
        store.commitTransaction();
    }

    /**
     * Verify scanning of application.xml descriptors.
     */
    @Test
    void applicationXml() {
        File webXml = new File(getClassesDirectory(XmlDescriptorScannerPluginIT.class), "jee6/META-INF/application.xml");
        store.beginTransaction();
        Scanner scanner = getScanner();
        ApplicationXmlDescriptor descriptor = scanner.scan(webXml, "/META-INF/application.xml", EnterpriseApplicationScope.EAR);
        assertThat(descriptor.getVersion(), equalTo("6"));
        store.commitTransaction();
    }

}
