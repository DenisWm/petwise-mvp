package com.petwise.domain.utils;

import java.util.Locale;
import java.util.UUID;

/** Generates domain identifiers. */
public final class IDUtils {

    private IDUtils() {}

    /** Returns a random UUID as a 32-character lowercase hex string (no hyphens). */
    public static String uuid() {
        return UUID.randomUUID().toString().toLowerCase(Locale.ROOT).replace("-", "");
    }
}
