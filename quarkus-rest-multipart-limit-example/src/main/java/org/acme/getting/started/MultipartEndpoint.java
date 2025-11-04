package org.acme.getting.started;

import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * A simple endpoint to demonstrate multipart form data processing.
 */
@Path("/process")
public class MultipartEndpoint {

    /**
     * Dummy processing of a multipart form data input.
     * 
     * @param input the input
     * @return the size of the uploaded parts
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response processMultiPart(final MultipartFormDataInput input) {
        // Dummy processing logic
        final var values = input.getValues();
        final var responseTemplate = """
                {
                  "parts": [
                    %s
                  ]
                }
                """;

        final String responseBody;
        if (!values.isEmpty()) {
            System.out.println(("entries: %s".formatted(values.size())));
            final var partInfos = new StringBuilder();
            for (var entry : values.entrySet()) {
                long totalSize = entry.getValue().stream()
                        .mapToLong(part -> {
                            part.isFileItem();
                            try (final var is = part.getFileItem().getInputStream()) {
                                long count = 0;
                                byte[] buffer = new byte[8192];
                                int read;
                                while ((read = is.read(buffer)) != -1) {
                                    count += read;
                                }
                                return count;
                            } catch (Exception e) {
                                return 0;
                            }
                        })
                        .sum();
                final var totalSizeMB = Util.bytesToMegabytes(totalSize);
                System.out.println(("totalSize: %s".formatted(totalSizeMB)));
                partInfos.append("\"%s\": \"%sM\",\n".formatted(entry.getKey(), totalSizeMB));
            }
            // Remove the last ",\n" if present
            int len = partInfos.length();
            if (len >= 2 && partInfos.substring(len - 2).equals(",\n")) {
                partInfos.setLength(len - 2);
            }
            responseBody = responseTemplate.formatted(partInfos.toString());
        } else {
            responseBody = responseTemplate.formatted("");
        }
        System.out.println(responseBody);
        return Response.ok(responseBody, MediaType.APPLICATION_JSON).build();
    }
}