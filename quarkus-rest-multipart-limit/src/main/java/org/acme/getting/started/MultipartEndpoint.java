package org.acme.getting.started;

import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/process")
public class MultipartEndpoint {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response processMultiPart( final MultipartFormDataInput input) {
        return Response.ok("foo").build();
    }
}