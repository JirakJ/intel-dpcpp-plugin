/*
 * Copyright (c) 2026 Jakub Jirák.
 * Licensed under the Apache License, Version 2.0. See LICENSE for details.
 */
package com.jakubjirak.dpcpp.analysis;

import com.jakubjirak.dpcpp.analysis.KernelOccupancyCalculator.KernelConfiguration;
import com.jakubjirak.dpcpp.analysis.KernelOccupancyCalculator.OccupancyResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KernelOccupancyCalculatorTest {

    private static OccupancyResult calc(int wg, int regs, int slm) {
        return KernelOccupancyCalculator.calculateOccupancy(new KernelConfiguration(wg, regs, slm));
    }

    @Test
    void wellSizedKernelFillsAllSlots() {
        OccupancyResult r = calc(256, 32, 0);
        assertEquals(100.0, r.theoreticalOccupancy, 1e-9);
        assertEquals("None", r.limitingFactor);
    }

    @Test
    void workGroupThatDoesNotDivideSlotsLeavesThemIdle() {
        // 48 work-items -> 3 threads per group; 128 slots / 3 = 42 groups -> 126 threads
        OccupancyResult r = calc(48, 32, 0);
        assertEquals(126.0 / 128.0 * 100.0, r.theoreticalOccupancy, 1e-9);
        assertEquals("Work-group size", r.limitingFactor);
    }

    @Test
    void oversizedWorkGroupCannotRun() {
        OccupancyResult r = calc(128 * 16 + 1, 32, 0);
        assertEquals(0.0, r.theoreticalOccupancy);
        assertEquals("Work-group size", r.limitingFactor);
    }

    @Test
    void sharedLocalMemoryLimitsResidentGroups() {
        // 64 KB per group -> 2 groups of 16 threads = 32/128 slots
        OccupancyResult r = calc(256, 32, 64 * 1024);
        assertEquals(25.0, r.theoreticalOccupancy, 1e-9);
        assertEquals("Local memory usage", r.limitingFactor);
    }

    @Test
    void groupLargerThanSlmCannotRun() {
        OccupancyResult r = calc(256, 32, 256 * 1024);
        assertEquals(0.0, r.theoreticalOccupancy);
        assertEquals("Local memory usage", r.limitingFactor);
    }

    @Test
    void registerSpillsReduceOccupancy() {
        OccupancyResult r = calc(256, 256, 0);
        assertEquals(50.0, r.theoreticalOccupancy, 1e-9);
        assertEquals("Register pressure", r.limitingFactor);
        assertTrue(r.suggestions.length > 0);
    }

    @Test
    void configurationRejectsInvalidValues() {
        assertThrows(IllegalArgumentException.class, () -> new KernelConfiguration(0, 32, 0));
        assertThrows(IllegalArgumentException.class, () -> new KernelConfiguration(64, -1, 0));
        assertThrows(IllegalArgumentException.class, () -> new KernelConfiguration(64, 32, -1));
    }

    @Test
    void suggestWorkGroupSizeHeuristic() {
        assertEquals(128, KernelOccupancyCalculator.suggestWorkGroupSize(96, 0));
        assertEquals(128, KernelOccupancyCalculator.suggestWorkGroupSize(32, 40000));
        assertEquals(512, KernelOccupancyCalculator.suggestWorkGroupSize(16, 1024));
        assertEquals(256, KernelOccupancyCalculator.suggestWorkGroupSize(48, 20000));
    }

    @Test
    void parseAcceptsOneToThreeNumbers() {
        KernelConfiguration a = KernelOccupancyCalculator.parse("256");
        assertNotNull(a);
        assertEquals(256, a.workGroupSize);
        assertEquals(32, a.registersPerThread);
        assertEquals(0, a.localMemoryPerWorkGroup);
        KernelConfiguration b = KernelOccupancyCalculator.parse(" 128, 64 ,  8192 ");
        assertNotNull(b);
        assertEquals(128, b.workGroupSize);
        assertEquals(64, b.registersPerThread);
        assertEquals(8192, b.localMemoryPerWorkGroup);
        assertNotNull(KernelOccupancyCalculator.parse("64 16 0"));
    }

    @Test
    void parseRejectsInvalidInput() {
        assertNull(KernelOccupancyCalculator.parse(null));
        assertNull(KernelOccupancyCalculator.parse("  "));
        assertNull(KernelOccupancyCalculator.parse("abc"));
        assertNull(KernelOccupancyCalculator.parse("0"));
        assertNull(KernelOccupancyCalculator.parse("256, -1"));
        assertNull(KernelOccupancyCalculator.parse("1,2,3,4"));
    }

    @Test
    void reportIsLocaleIndependentAndListsSuggestions() {
        KernelConfiguration config = new KernelConfiguration(48, 32, 0);
        String report = KernelOccupancyCalculator.report(config, KernelOccupancyCalculator.calculateOccupancy(config));
        assertTrue(report.contains("Theoretical occupancy: 98.4%"), report);
        assertTrue(report.contains("Limiting factor: Work-group size"));
        assertTrue(report.contains("  • "));
    }
}
