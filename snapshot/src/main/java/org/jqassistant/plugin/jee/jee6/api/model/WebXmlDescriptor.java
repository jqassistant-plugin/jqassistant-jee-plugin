package org.jqassistant.plugin.jee.jee6.api.model;

import java.util.List;

import com.buschmais.jqassistant.plugin.xml.api.model.XmlFileDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Relation;

public interface WebXmlDescriptor extends WebDescriptor, XmlFileDescriptor, VersionDescriptor {

    @Relation("HAS_SESSION_CONFIG")
    SessionConfigDescriptor getSessionConfig();

    void setSessionConfig(SessionConfigDescriptor sessionConfig);

    @Relation("HAS_SERVLET")
    List<ServletDescriptor> getServlets();

    @Relation("HAS_SERVLET_MAPPING")
    List<ServletMappingDescriptor> getServletMappings();

    @Relation("HAS_FILTER")
    List<FilterDescriptor> getFilters();

    @Relation("HAS_FILTER_MAPPING")
    List<FilterMappingDescriptor> getFilterMappings();

    @Relation("HAS_LISTENER")
    List<ListenerDescriptor> getListeners();

    @Relation("HAS_CONTEXT_PARAM")
    List<ParamValueDescriptor> getContextParams();

    @Relation("HAS_ERROR_PAGE")
    List<ErrorPageDescriptor> getErrorPages();

    @Relation("HAS_SECURITY_CONSTRAINT")
    List<SecurityConstraintDescriptor> getSecurityConstraints();

    @Relation("HAS_SECURITY_ROLE")
    List<SecurityRoleDescriptor> getSecurityRoles();

    @Relation("HAS_LOGIN_CONFIG")
    List<LoginConfigDescriptor> getLoginConfigs();
}
