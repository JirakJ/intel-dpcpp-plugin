/*
 * Copyright (c) 2026 Jakub Jirák.
 * Licensed under the Apache License, Version 2.0. See LICENSE for details.
 */
package com.jakubjirak.dpcpp.analysis;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * Back-of-the-envelope occupancy model for Intel Xe GPUs.
 * <p>
 * It is a first-order estimate, not a measurement: it considers three limits per
 * Xe-core / sub-slice — hardware thread slots, register file and shared local memory —
 * and reports the tightest one. Use VTune GPU Hotspots for real numbers.
 */
public final class KernelOccupancyCalculator {

    /** Hardware threads per EU (Xe-HPC). */
    static final int THREADS_PER_EU = 8;
    /** EUs per Xe-core / sub-slice. */
    static final int EUS_PER_SUBSLICE = 16;
    /** SIMD width a work-group is compiled for (work-items per hardware thread). */
    static final int SIMD_WIDTH = 16;
    /** General registers per hardware thread in the default GRF mode. */
    static final int REGISTERS_PER_THREAD = 128;
    /** Shared local memory per Xe-core in bytes. */
    static final int SLM_PER_SUBSLICE = 128 * 1024;

    private KernelOccupancyCalculator() {
    }

    /** Kernel resource usage entered by the user. */
    public static final class KernelConfiguration {
        public final int workGroupSize;
        public final int registersPerThread;
        public final int localMemoryPerWorkGroup;

        public KernelConfiguration(int workGroupSize, int registersPerThread, int localMemoryPerWorkGroup) {
            if (workGroupSize <= 0) {
                throw new IllegalArgumentException("Work-group size must be positive");
            }
            if (registersPerThread < 0 || localMemoryPerWorkGroup < 0) {
                throw new IllegalArgumentException("Registers and local memory must not be negative");
            }
            this.workGroupSize = workGroupSize;
            this.registersPerThread = registersPerThread;
            this.localMemoryPerWorkGroup = localMemoryPerWorkGroup;
        }
    }

    /** Estimated occupancy in percent, the limiting factor and suggestions. */
    public static final class OccupancyResult {
        public final double theoreticalOccupancy;
        public final String limitingFactor;
        public final String[] suggestions;

        OccupancyResult(double occupancy, String limitingFactor, String[] suggestions) {
            this.theoreticalOccupancy = occupancy;
            this.limitingFactor = limitingFactor;
            this.suggestions = suggestions;
        }
    }

    /** Computes the estimate for {@code config}. */
    @NotNull
    public static OccupancyResult calculateOccupancy(@NotNull KernelConfiguration config) {
        int slots = THREADS_PER_EU * EUS_PER_SUBSLICE;
        int threadsPerGroup = (config.workGroupSize + SIMD_WIDTH - 1) / SIMD_WIDTH;
        if (threadsPerGroup > slots) {
            return new OccupancyResult(0.0, "Work-group size", new String[]{
                "Work-group needs " + threadsPerGroup + " hardware threads but a sub-slice has " + slots,
                "Reduce the work-group size to at most " + slots * SIMD_WIDTH
            });
        }
        int groupsByThreads = slots / threadsPerGroup;
        int groupsBySlm = config.localMemoryPerWorkGroup == 0
                ? Integer.MAX_VALUE : SLM_PER_SUBSLICE / config.localMemoryPerWorkGroup;
        double registerFactor = config.registersPerThread <= REGISTERS_PER_THREAD
                ? 1.0 : (double) REGISTERS_PER_THREAD / config.registersPerThread;

        int groups = Math.min(groupsByThreads, groupsBySlm);
        double threadOccupancy = (double) groups * threadsPerGroup / slots;
        double occupancy = Math.min(1.0, threadOccupancy * registerFactor) * 100.0;

        if (groupsBySlm == 0) {
            return new OccupancyResult(0.0, "Local memory usage", new String[]{
                "One work-group needs more shared local memory than a sub-slice provides ("
                        + SLM_PER_SUBSLICE / 1024 + " KB)",
                "Tile the problem so each work-group uses less local memory"
            });
        }
        if (registerFactor < 1.0) {
            return new OccupancyResult(occupancy, "Register pressure", new String[]{
                "More than " + REGISTERS_PER_THREAD + " registers per thread spill to memory",
                "Reduce live temporaries or split the kernel",
                "Consider the large GRF mode (-ftarget-register-alloc-mode=pvc:large)"
            });
        }
        if (groupsBySlm < groupsByThreads) {
            return new OccupancyResult(occupancy, "Local memory usage", new String[]{
                "Only " + groupsBySlm + " work-group(s) fit per sub-slice because of local memory",
                "Reduce local memory per work-group or use smaller tiles"
            });
        }
        if (occupancy < 100.0) {
            return new OccupancyResult(occupancy, "Work-group size", new String[]{
                "Hardware thread slots are left idle; pick a work-group size whose thread count divides " + slots,
                "Suggested work-group size: " + suggestWorkGroupSize(config.registersPerThread, config.localMemoryPerWorkGroup)
            });
        }
        return new OccupancyResult(occupancy, "None", new String[]{
            "All hardware thread slots can be filled",
            "Focus on memory access patterns next (VTune GPU Hotspots)"
        });
    }

    /** Heuristic work-group size for the given resource usage. */
    public static int suggestWorkGroupSize(int registersPerThread, int localMemory) {
        if (registersPerThread > 64 || localMemory > 32768) {
            return 128;
        }
        if (registersPerThread < 32 && localMemory < 16384) {
            return 512;
        }
        return 256;
    }

    /**
     * Parses user input of the form {@code "workGroupSize[, registersPerThread[, localMemoryBytes]]"};
     * omitted values default to 32 registers and 0 bytes. Returns {@code null} for invalid input.
     */
    @Nullable
    public static KernelConfiguration parse(@Nullable String input) {
        if (input == null || input.isBlank()) {
            return null;
        }
        String[] parts = input.split("[,\\s]+");
        int[] values = {0, 32, 0};
        int index = 0;
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (index >= values.length) {
                return null;
            }
            try {
                values[index++] = Integer.parseInt(part);
            } catch (NumberFormatException ex) {
                return null;
            }
        }
        try {
            return new KernelConfiguration(values[0], values[1], values[2]);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    /** Multi-line report for display. */
    @NotNull
    public static String report(@NotNull KernelConfiguration config, @NotNull OccupancyResult result) {
        StringBuilder message = new StringBuilder();
        message.append("Estimate for an Intel Xe-HPC sub-slice (first-order model, not a measurement)\n\n");
        message.append(String.format(Locale.ROOT, "Work-group size: %d%n", config.workGroupSize));
        message.append(String.format(Locale.ROOT, "Registers per thread: %d%n", config.registersPerThread));
        message.append(String.format(Locale.ROOT, "Local memory per work-group: %d bytes%n%n", config.localMemoryPerWorkGroup));
        message.append(String.format(Locale.ROOT, "Theoretical occupancy: %.1f%%%n", result.theoreticalOccupancy));
        message.append("Limiting factor: ").append(result.limitingFactor).append("\n\nSuggestions:\n");
        for (String suggestion : result.suggestions) {
            message.append("  • ").append(suggestion).append('\n');
        }
        return message.toString();
    }
}
