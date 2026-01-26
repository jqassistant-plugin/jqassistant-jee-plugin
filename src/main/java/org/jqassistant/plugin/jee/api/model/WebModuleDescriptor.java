package org.jqassistant.plugin.jee.api.model;

public interface WebModuleDescriptor extends EnterpriseApplicationModuleDescriptor, WebDescriptor {

    String getContextRoot();

    void setContextRoot(String value);
}
