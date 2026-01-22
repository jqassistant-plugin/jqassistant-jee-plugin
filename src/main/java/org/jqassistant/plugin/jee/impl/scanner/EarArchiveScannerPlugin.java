package org.jqassistant.plugin.jee.impl.scanner;

import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractZipArchiveScannerPlugin;

import org.jqassistant.plugin.jee.api.model.EnterpriseApplicationArchiveDescriptor;
import org.jqassistant.plugin.jee.api.scope.EnterpriseApplicationScope;

public class EarArchiveScannerPlugin
        extends AbstractZipArchiveScannerPlugin<EnterpriseApplicationArchiveDescriptor> {

    @Override
    protected String getExtension() {
        return ".ear";
    }

    @Override
    protected Scope createScope(Scope currentScope, EnterpriseApplicationArchiveDescriptor archive, ScannerContext context) {
        return EnterpriseApplicationScope.EAR;
    }

    @Override
    protected void destroyScope(ScannerContext scannerContext) {
    }
}
