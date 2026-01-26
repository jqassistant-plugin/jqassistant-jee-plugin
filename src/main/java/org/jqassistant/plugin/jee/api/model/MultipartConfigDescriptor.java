package org.jqassistant.plugin.jee.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("MultipartConfig")
public interface MultipartConfigDescriptor extends WebDescriptor {

    Long getFileSizeThreshold();

    void setFileSizeThreshold(Long fileSizeThreshold);

    String getLocation();

    void setLocation(String location);

    Long getMaxFileSize();

    void setMaxFileSize(Long maxFileSize);

    Long getMaxRequestSize();

    void setMaxRequestSize(Long maxRequestSize);
}
