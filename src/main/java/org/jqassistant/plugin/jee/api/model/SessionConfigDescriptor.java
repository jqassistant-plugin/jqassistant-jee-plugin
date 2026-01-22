package org.jqassistant.plugin.jee.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("SessionConfig")
public interface SessionConfigDescriptor extends WebDescriptor {
    Integer getSessionTimeout();

    void setSessionTimeout(Integer integer);
}
