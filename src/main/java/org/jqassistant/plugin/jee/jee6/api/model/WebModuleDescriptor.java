package org.jqassistant.plugin.jee.jee6.api.model;

public interface WebModuleDescriptor extends EnterpriseApplicationModuleDescriptor, WebDescriptor {

    String getContextRoot();

    void setContextRoot(String value);
}
