package org.jqassistant.plugin.jee.jee6.api.model;

import java.util.List;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("ServletMapping")
public interface ServletMappingDescriptor extends WebDescriptor {

    @Relation("ON_URL_PATTERN")
    List<UrlPatternDescriptor> getUrlPatterns();

}
