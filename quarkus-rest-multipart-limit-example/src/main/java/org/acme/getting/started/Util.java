package org.acme.getting.started;

/**
 * Utility class for byte conversions.
 */
public class Util {
    private Util() {
        // Prevent instantiation
    }

    public static long bytesToMegabytes(long bytes) {
        return bytes / (1024 * 1024);
    }
}
