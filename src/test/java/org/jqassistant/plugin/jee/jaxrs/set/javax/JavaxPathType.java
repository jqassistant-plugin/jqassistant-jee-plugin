package org.jqassistant.plugin.jee.jaxrs.set.javax;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class JavaxPathType {

    @GET
    public String test() {
        return "test";
    }
}
