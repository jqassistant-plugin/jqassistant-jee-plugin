package org.jqassistant.plugin.jee.jee6.impl.scanner;

import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin.Requires;
import com.buschmais.jqassistant.plugin.common.api.model.DirectoryDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.DirectoryResource;

/**
 *
 */
@Requires(DirectoryDescriptor.class)
public class WarClassesDirectoryScannerPlugin extends AbstractWarClassesResourceScannerPlugin<DirectoryResource, DirectoryDescriptor> {

}
