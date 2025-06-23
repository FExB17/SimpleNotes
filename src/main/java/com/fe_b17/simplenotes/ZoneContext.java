package com.fe_b17.simplenotes;

import java.time.ZoneId;

public class ZoneContext {
    private static final ThreadLocal<ZoneId> zoneHolder = new ThreadLocal<>();

    public static void set(ZoneId zoneId) {
        zoneHolder.set(zoneId);
    }

    public static ZoneId get() {
        ZoneId zone = zoneHolder.get();
        return zone != null ? zone : ZoneId.systemDefault(); // Fallback
    }

    public static void clear() {
        zoneHolder.remove();
    }
}
