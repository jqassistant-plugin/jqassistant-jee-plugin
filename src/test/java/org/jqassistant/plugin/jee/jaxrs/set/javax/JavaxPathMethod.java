package org.jqassistant.plugin.jee.jaxrs.set.javax;

import javax.ws.rs.Path;

public class JavaxPathMethod {

    @Path("/test")
    public String test() {
        return "test";
    }
}
