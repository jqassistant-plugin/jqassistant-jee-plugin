package org.jqassistant.plugin.jee.jee6.api.model;

import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("HttpMethod")
public interface HttpMethodDescriptor extends WebDescriptor, NamedDescriptor {
}
