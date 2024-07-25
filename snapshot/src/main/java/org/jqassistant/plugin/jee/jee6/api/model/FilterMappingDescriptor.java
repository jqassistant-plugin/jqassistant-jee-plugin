package org.jqassistant.plugin.jee.jee6.api.model;

import java.util.List;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("FilterMapping")
public interface FilterMappingDescriptor extends WebDescriptor {

    @Relation("ON_URL_PATTERN")
    List<UrlPatternDescriptor> getUrlPatterns();

    @Relation("ON_SERVLET")
    ServletDescriptor getServlet();

    void setServlet(ServletDescriptor servletDescriptor);

    @Relation("USES_DISPATCHER")
    List<DispatcherDescriptor> getDispatchers();

}
