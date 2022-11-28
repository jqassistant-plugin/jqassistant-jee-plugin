package org.jqassistant.plugin.jee.jee6.impl.scanner;

import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractZipArchiveScannerPlugin;

import org.jqassistant.plugin.jee.jee6.api.model.EnterpriseApplicationArchiveDescriptor;
import org.jqassistant.plugin.jee.jee6.api.scanner.EnterpriseApplicationScope;

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
