package org.jqassistant.plugin.jee.jee6.impl.scanner;

import java.io.File;
import java.io.IOException;

import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractDirectoryScannerPlugin;

import org.jqassistant.plugin.jee.jee6.api.model.WebApplicationDescriptor;
import org.jqassistant.plugin.jee.jee6.api.model.WebApplicationDirectoryDescriptor;
import org.jqassistant.plugin.jee.jee6.api.scanner.WebApplicationScope;

public class WebApplicationDirectoryScannerPlugin
        extends AbstractDirectoryScannerPlugin<WebApplicationDirectoryDescriptor> {

    @Override
    protected Scope getRequiredScope() {
        return WebApplicationScope.WAR;
    }

    @Override
    protected WebApplicationDirectoryDescriptor getContainerDescriptor(File container, ScannerContext scannerContext) {
        return scannerContext.getStore().create(WebApplicationDirectoryDescriptor.class);
    }

    @Override
    protected void enterContainer(File container, WebApplicationDirectoryDescriptor containerDescriptor, ScannerContext scannerContext) throws IOException {
        scannerContext.push(WebApplicationDescriptor.class, containerDescriptor);
    }

    @Override
    protected void leaveContainer(File container, WebApplicationDirectoryDescriptor containerDescriptor, ScannerContext scannerContext) throws IOException {
        scannerContext.pop(WebApplicationDescriptor.class);
    }
}
