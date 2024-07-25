package org.jqassistant.plugin.jee.jee6.api.model;

import com.buschmais.jqassistant.plugin.common.api.model.ApplicationDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.ZipArchiveDescriptor;

/**
 * Describes an EAR archive.
 */
public interface EnterpriseApplicationArchiveDescriptor extends EnterpriseDescriptor, ApplicationDescriptor,
                                                                ZipArchiveDescriptor {
}
