package org.jqassistant.plugin.jee.cdi.api.model;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Label;

/**
 * Defines label "CDI" which is applied to all nodes generated by this plugin.
 */
@Label("CDI")
@Abstract
public interface CdiDescriptor extends Descriptor {
}
