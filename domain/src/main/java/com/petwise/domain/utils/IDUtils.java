package com.petwise.domain.utils;

import java.util.UUID;

/**
 * Utility class for generating domain identifiers.
 *
 * <p>This is a non-instantiable utility class — all methods are static.
 */
public final class IDUtils {

    private IDUtils() {}

    /**
     * Generates a new random UUID formatted as a 32-character lowercase hex string with no hyphens.
     *
     * <p>Example output: {@code "550e8400e29b41d4a716446655440000"}
     *
     * @return a unique 32-character hex string
     */
    public static String uuid() {
        return UUID.randomUUID().toString().toLowerCase().replace("-", "");
    }
}
