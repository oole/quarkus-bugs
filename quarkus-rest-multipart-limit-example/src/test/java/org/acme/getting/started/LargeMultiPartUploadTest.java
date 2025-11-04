package org.acme.getting.started;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.hamcrest.Matchers.*;

/**
 * Test uploading a multipart file larger than 2GB
 */
@QuarkusTest
class LargeMultiPartUploadTest {

    // Simulate a part larger than 30MB using custom InputStream
    private static final long PART_SIZE = 30L * 1024 * 1024; // For testing purposes, use 30MB, configured limit is 29

    private LargeInputStream getInputStream(long partSize) {
        System.out.println("Starting large multipart upload test...");

        System.out.println("Expected part size: %s megabytes".formatted(Util.bytesToMegabytes(partSize)));

        return new LargeInputStream(partSize);
    }

    /**
     * Runs.
     * 
     * @throws IOException
     */
    @Test
    void testLargeMultipartUploadStream() {
        final var stream = getInputStream(PART_SIZE);

        final var mpSpec = new MultiPartSpecBuilder(stream).controlName("id2").fileName("id2.txt")
                .mimeType("plain/json").build();

        RestAssured
                .given()
                .multiPart(mpSpec)
                .when()
                .post("/process")
                .then()
                .statusCode(is(200));

        System.out.println("Written megabytes: %s".formatted(Util.bytesToMegabytes(stream.getWritten())));
    }

    /**
     * Expected to fail.
     * 
     * @throws IOException
     */
    @Test
    void testLargeMultipartUpload() throws IOException {
        final var stream = getInputStream(PART_SIZE);

        // read the stream into a string
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[8192];
        int read;
        while ((read = stream.read(buffer)) != -1) {
            sb.append(new String(buffer, 0, read));
        }
        final var readString = sb.toString();
        System.out.println(
                "Actual part size: %s megabytes".formatted(Util.bytesToMegabytes(readString.getBytes().length)));

        final var mpSpec = new MultiPartSpecBuilder(readString)
                .controlName("id2")
                .fileName("id2.txt")
                .mimeType("plain/json").build();

        RestAssured
                .given()
                .multiPart(mpSpec)
                .when()
                .post("/process")
                .then()
                .statusCode(is(200)); // will return 413 Payload Too Large

        System.out.println("Written megabytes: %s".formatted(Util.bytesToMegabytes(stream.getWritten())));
    }

    // Custom InputStream that simulates a large file without allocating memory
    static class LargeInputStream extends ByteArrayInputStream {
        private long remaining;
        private long written;

        public LargeInputStream(long size) {
            super(new byte[0]);
            this.remaining = size;
        }

        @Override
        public int read() {
            if (remaining > 0) {
                remaining--;
                written++;
                return 'A';
            }
            return -1;
        }

        final long getWritten() {
            return written;
        }

        @Override
        public int read(byte[] b, int off, int len) {
            if (remaining == 0)
                return -1;
            int toRead = (int) Math.min(len, remaining);
            for (int i = 0; i < toRead; i++) {
                b[off + i] = 'A';
            }
            written += toRead;
            remaining -= toRead;
            return toRead;
        }
    }
}