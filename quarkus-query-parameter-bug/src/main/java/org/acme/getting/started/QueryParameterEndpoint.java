package org.acme.getting.started;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

/**
 * This is an example endpoint to showcase a change in behavor for the @QueryParam annotated method parameter.
 */
@Path("/hello")
public class QueryParameterEndpoint {

    public static final String IS_NULL = "query parameter is null";

    public static final String IS_EMPTY = "query parameter is empty";

    public static final String IS_VALID = "query parameter is present";

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String endpoint(@QueryParam("first")String first, @QueryParam("second")String second) {
        if (first == null) {
            return IS_NULL;
        } else if (first.isEmpty()) {
            // In quarkus 3.20.3 with quarkus-rest this case is not reached.
            return IS_EMPTY;
        }
        return IS_VALID;
    }
}