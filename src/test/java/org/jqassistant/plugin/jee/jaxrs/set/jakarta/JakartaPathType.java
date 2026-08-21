package org.jqassistant.plugin.jee.jaxrs.set.jakarta;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/test")
public class JakartaPathType {

    @GET
    public String test() {
        return "test";
    }
}
