package org.acme.getting.started;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

/**
 * 
 */
@Path("/upload")
public class ConsumesReproducer {

    @Context
    HttpHeaders m_requestHeaders;

    @PUT
    @Consumes({"image/png", "image/webp", "image/jpeg"})
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public String consumeMedia() {
        final var mediaType = m_requestHeaders.getMediaType();
        System.out.println("MediaType: " + mediaType);
        // The mediaType cannot be null, as otherwise the method *should* not be called
        // at all.
        if (mediaType == null) {
            throw new IllegalStateException("MediaType should not be null here, but is: " + mediaType);
        }
        return "success";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getMedia() {
        return "success";
    }

}