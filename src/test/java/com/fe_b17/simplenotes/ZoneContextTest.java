package com.fe_b17.simplenotes;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class ZoneContextTest {

    @AfterEach
    void tearDown() {
        ZoneContext.clear();
    }

    @Test
    void testSetAndGetZoneId() {
        ZoneId berlin = ZoneId.of("Europe/Berlin");
        ZoneContext.set(berlin);
        assertEquals(berlin, ZoneContext.get());
    }

    @Test
    void testGetReturnsSystemDefaultIfNotSet() {
        ZoneContext.clear();
        assertEquals(ZoneId.systemDefault(), ZoneContext.get());
    }

    @Test
    void testClearRemovesZoneId() {
        ZoneContext.set(ZoneId.of("UTC"));
        ZoneContext.clear();
        assertEquals(ZoneId.systemDefault(), ZoneContext.get());
    }
}