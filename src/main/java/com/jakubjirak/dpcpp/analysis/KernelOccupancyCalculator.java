package com.jakubjirak.dpcpp.analysis;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class KernelOccupancyCalculator {
    
    public static class KernelConfiguration {
        public final int workGroupSize;
        public final int registersPerThread;
        public final int localMemoryPerWorkGroup;
        public final String deviceName;
        
        public KernelConfiguration(int workGroupSize, int registersPerThread, 
                                  int localMemoryPerWorkGroup, String deviceName) {
            this.workGroupSize = workGroupSize;
            this.registersPerThread = registersPerThread;
            this.localMemoryPerWorkGroup = localMemoryPerWorkGroup;
            this.deviceName = deviceName;
        }
    }
    
    public static class OccupancyResult {
        public final double theoreticalOccupancy;
        public final String limitingFactor;
        public final String[] suggestions;
        
        public OccupancyResult(double occupancy, String limitingFactor, String[] suggestions) {
            this.theoreticalOccupancy = occupancy;
            this.limitingFactor = limitingFactor;
            this.suggestions = suggestions;
        }
    }
    
    /**
     * Calculate theoretical occupancy for Intel GPU architectures
     * Based on Intel Data Center GPU Max specifications
     */
    public static OccupancyResult calculateOccupancy(@NotNull KernelConfiguration config) {
        // Intel GPU Max hardware limits (typical values)
        final int MAX_THREADS_PER_EU = 8;  // Hardware threads per EU
        final int MAX_REGISTERS_PER_EU = 128;
        final int MAX_LOCAL_MEMORY_PER_SLICE = 65536; // 64 KB
        final int EUs_PER_SLICE = 16;
        
        // Calculate occupancy based on different limiting factors
        double occupancyByThreads = (double) config.workGroupSize / 
                                    (MAX_THREADS_PER_EU * EUs_PER_SLICE);
        
        double occupancyByRegisters = config.registersPerThread > 0 ? 
                                      (double) MAX_REGISTERS_PER_EU / 
                                      (config.registersPerThread * config.workGroupSize) : 1.0;
        
        double occupancyByLocalMemory = config.localMemoryPerWorkGroup > 0 ?
                                        (double) MAX_LOCAL_MEMORY_PER_SLICE /
                                        config.localMemoryPerWorkGroup : 1.0;
        
        // Find limiting factor
        double actualOccupancy = Math.min(1.0, Math.min(occupancyByThreads, 
                                          Math.min(occupancyByRegisters, occupancyByLocalMemory)));
        
        String limitingFactor;
        String[] suggestions;
        
        if (actualOccupancy == occupancyByThreads) {
            limitingFactor = "Work-group size";
            suggestions = new String[]{
                "Try increasing work-group size to 256 or 512",
                "Current work-group size may be too small for efficient GPU utilization",
                "Consider using nd_range with explicit work-group dimensions"
            };
        } else if (actualOccupancy == occupancyByRegisters) {
            limitingFactor = "Register pressure";
            suggestions = new String[]{
                "Reduce temporary variables in kernel",
                "Split complex kernel into multiple simpler kernels",
                "Use local memory instead of registers for temporary storage"
            };
        } else if (actualOccupancy == occupancyByLocalMemory) {
            limitingFactor = "Local memory usage";
            suggestions = new String[]{
                "Reduce local memory allocation size",
                "Consider tiling strategy with smaller tiles",
                "Use private memory where possible instead of local"
            };
        } else {
            limitingFactor = "Optimal";
            suggestions = new String[]{
                "Kernel configuration is well-optimized",
                "Occupancy is near theoretical maximum",
                "Focus on memory access patterns for further optimization"
            };
        }
        
        return new OccupancyResult(actualOccupancy * 100.0, limitingFactor, suggestions);
    }
    
    public static void showOccupancyAnalysis(@NotNull Project project, 
                                            @NotNull KernelConfiguration config) {
        OccupancyResult result = calculateOccupancy(config);
        
        StringBuilder message = new StringBuilder();
        message.append("Kernel Occupancy Analysis\n\n");
        message.append(String.format("Device: %s\n", config.deviceName));
        message.append(String.format("Work-group size: %d\n", config.workGroupSize));
        message.append(String.format("Registers per thread: %d\n", config.registersPerThread));
        message.append(String.format("Local memory: %d bytes\n\n", config.localMemoryPerWorkGroup));
        
        message.append(String.format("📊 Theoretical Occupancy: %.1f%%\n\n", result.theoreticalOccupancy));
        message.append(String.format("🔍 Limiting Factor: %s\n\n", result.limitingFactor));
        
        message.append("💡 Optimization Suggestions:\n");
        for (String suggestion : result.suggestions) {
            message.append("  • ").append(suggestion).append("\n");
        }
        
        message.append("\nAvailable in Professional Edition");
        
        Messages.showInfoMessage(project, message.toString(), "Kernel Occupancy Calculator");
    }
    
    /**
     * Estimate optimal work-group size for given kernel
     */
    public static int suggestWorkGroupSize(int registersPerThread, int localMemory) {
        // Heuristic for Intel GPUs
        if (registersPerThread > 64) {
            return 128;  // High register pressure - smaller work-groups
        } else if (localMemory > 32768) {
            return 128;  // High local memory - smaller work-groups
        } else if (registersPerThread < 32 && localMemory < 16384) {
            return 512;  // Low resource usage - larger work-groups
        } else {
            return 256;  // Balanced default
        }
    }
}
