package org.jqassistant.plugin.jee.api.scope;

import com.buschmais.jqassistant.core.scanner.api.Scope;

/**
 * Defines the scopes for Java web applications.
 */
public enum WebApplicationScope implements Scope {

    WAR;

    @Override
    public String getPrefix() {
        return "java-web";
    }

    @Override
    public String getName() {
        return name();
    }

}
