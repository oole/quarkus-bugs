package org.acme.getting.started;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * This example is not working. The reges does not match the path as expected.
 * 
 * Call this endpoint using cURL: curl -X GET http://localhost:8080/hello/foo123e4567-e89b-12d3-a456-426614174000/second/bar
 */
@Path("/hello/{first:foo[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/second/{third}")
public class PathReproducer {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String greeting(@PathParam("first")String first, @PathParam("third")String third) {
        return "first:%s, third:%s".formatted(first, third);
    }

}