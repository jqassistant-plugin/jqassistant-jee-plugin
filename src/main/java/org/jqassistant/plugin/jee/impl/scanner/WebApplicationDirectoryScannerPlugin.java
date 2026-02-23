package org.jqassistant.plugin.jee.impl.scanner;

import java.io.File;

import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractDirectoryScannerPlugin;

import org.jqassistant.plugin.jee.api.model.WebApplicationDescriptor;
import org.jqassistant.plugin.jee.api.model.WebApplicationDirectoryDescriptor;

import static com.buschmais.jqassistant.core.scanner.api.DefaultScope.NONE;

public class WebApplicationDirectoryScannerPlugin
        extends AbstractDirectoryScannerPlugin<WebApplicationDirectoryDescriptor> {

    @Override
    protected Scope getRequiredScope() {
        return NONE;
    }

    @Override
    protected WebApplicationDirectoryDescriptor getContainerDescriptor(File container, ScannerContext scannerContext) {
        return scannerContext.getStore().create(WebApplicationDirectoryDescriptor.class);
    }

    @Override
    protected void enterContainer(File container, WebApplicationDirectoryDescriptor containerDescriptor, ScannerContext scannerContext) {
        scannerContext.push(WebApplicationDescriptor.class, containerDescriptor);
    }

    @Override
    protected void leaveContainer(File container, WebApplicationDirectoryDescriptor containerDescriptor, ScannerContext scannerContext) {
        scannerContext.pop(WebApplicationDescriptor.class);
    }
}
