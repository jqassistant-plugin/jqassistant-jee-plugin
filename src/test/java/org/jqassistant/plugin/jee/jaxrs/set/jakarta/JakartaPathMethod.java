package org.jqassistant.plugin.jee.jaxrs.set.jakarta;

import jakarta.ws.rs.Path;

public class JakartaPathMethod {

    @Path("/test")
    public String test() {
        return "test";
    }
}
