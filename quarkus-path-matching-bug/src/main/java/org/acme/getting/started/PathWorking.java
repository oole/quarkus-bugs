package org.acme.getting.started;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * This example is working, the reges from the class wite Path annotation is working as expected
 */
@Path("/bye/{first:foo[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
public class PathWorking {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/second/{third}")
    public String greeting(@PathParam("first")String first, @PathParam("third")String third) {
        return "first:%s, third:%s".formatted(first, third);
    }

}