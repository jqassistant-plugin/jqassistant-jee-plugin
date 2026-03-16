package org.jqassistant.plugin.jee;

import java.io.File;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import com.buschmais.jqassistant.plugin.java.api.scanner.ArtifactScopedTypeResolver;
import com.buschmais.jqassistant.plugin.java.api.scanner.TypeResolver;

import org.jqassistant.plugin.jee.api.model.ApplicationXmlDescriptor;
import org.jqassistant.plugin.jee.api.model.WebApplicationArchiveDescriptor;
import org.jqassistant.plugin.jee.api.model.WebXmlDescriptor;
import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.core.scanner.api.DefaultScope.NONE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Contains integration tests for JEE XML descriptors.
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
        scanner.getContext()
                .push(TypeResolver.class, new ArtifactScopedTypeResolver(warDescriptor));
        File webXml = new File(getClassesDirectory(XmlDescriptorScannerPluginIT.class), "jee/WEB-INF/web.xml");
        WebXmlDescriptor descriptor = scanner.scan(webXml, "/WEB-INF/web.xml", NONE);
        assertThat(descriptor.getVersion(), equalTo("3.0"));
        scanner.getContext()
                .pop(TypeResolver.class);
        store.commitTransaction();
    }

    /**
     * Verify scanning of application.xml descriptors.
     */
    @Test
    void applicationXml() {
        File webXml = new File(getClassesDirectory(XmlDescriptorScannerPluginIT.class), "jee/META-INF/application.xml");
        store.beginTransaction();
        Scanner scanner = getScanner();
        ApplicationXmlDescriptor descriptor = scanner.scan(webXml, "/META-INF/application.xml", NONE);
        assertThat(descriptor.getVersion(), equalTo("6"));
        store.commitTransaction();
    }

}
