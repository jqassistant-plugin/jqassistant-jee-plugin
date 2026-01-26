package org.jqassistant.plugin.jee.impl.scanner;

import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractZipArchiveScannerPlugin;
import com.buschmais.jqassistant.plugin.java.api.scanner.ArtifactScopedTypeResolver;
import com.buschmais.jqassistant.plugin.java.api.scanner.TypeResolver;

import org.jqassistant.plugin.jee.api.model.WebApplicationArchiveDescriptor;
import org.jqassistant.plugin.jee.api.scope.WebApplicationScope;

public class WebApplicationArchiveScannerPlugin extends AbstractZipArchiveScannerPlugin<WebApplicationArchiveDescriptor> {

    @Override
    protected String getExtension() {
        return ".war";
    }

    @Override
    protected Scope createScope(Scope currentScope, WebApplicationArchiveDescriptor archiveDescriptor, ScannerContext scannerContext) {
        TypeResolver typeResolver = new ArtifactScopedTypeResolver(archiveDescriptor, AbstractWarClassesResourceScannerPlugin.CLASSES_DIRECTORY);
        scannerContext.push(TypeResolver.class, typeResolver);
        return WebApplicationScope.WAR;
    }

    @Override
    protected void destroyScope(ScannerContext scannerContext) {
        scannerContext.pop(TypeResolver.class);
    }

}
