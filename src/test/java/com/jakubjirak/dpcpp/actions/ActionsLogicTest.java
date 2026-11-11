/*
 * Copyright (c) 2026 Jakub Jirák.
 * Licensed under the Apache License, Version 2.0. See LICENSE for details.
 */
package com.jakubjirak.dpcpp.actions;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActionsLogicTest {

    @Test
    void toolchainSummaryListsEveryInstallation() {
        String text = ConfigureToolchainAction.describe(List.of(Paths.get("/opt/intel/oneapi"), Paths.get("/home/u/intel/oneapi")));
        assertTrue(text.contains("• " + Paths.get("/opt/intel/oneapi")));
        assertTrue(text.contains("• " + Paths.get("/home/u/intel/oneapi")));
        assertTrue(text.contains("icpx"));
    }

    @Test
    void offeredAnalysesAreRealToolCollections() {
        assertArrayEquals(new String[]{"gpu-hotspots", "gpu-offload", "memory-access"}, ProfileWithVTuneAction.ANALYSES);
        assertArrayEquals(new String[]{"roofline", "offload"}, AdvisorRooflineAction.ANALYSES);
    }
}
