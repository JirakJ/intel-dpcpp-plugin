package com.jakubjirak.dpcpp.analysis;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class MemoryBandwidthEstimator {
    
    public static class KernelMemoryProfile {
        public final long globalLoads;
        public final long globalStores;
        public final long localLoads;
        public final long localStores;
        public final int elementSize;  // bytes
        public final long numWorkItems;
        public final double executionTimeMs;
        
        public KernelMemoryProfile(long globalLoads, long globalStores,
                                  long localLoads, long localStores,
                                  int elementSize, long numWorkItems,
                                  double executionTimeMs) {
            this.globalLoads = globalLoads;
            this.globalStores = globalStores;
            this.localLoads = localLoads;
            this.localStores = localStores;
            this.elementSize = elementSize;
            this.numWorkItems = numWorkItems;
            this.executionTimeMs = executionTimeMs;
        }
    }
    
    public static class BandwidthAnalysis {
        public final double requiredBandwidthGBps;
        public final double achievedBandwidthGBps;
        public final double utilizationPercent;
        public final String bottleneckType;
        public final String[] recommendations;
        
        public BandwidthAnalysis(double required, double achieved, double utilization,
                               String bottleneck, String[] recommendations) {
            this.requiredBandwidthGBps = required;
            this.achievedBandwidthGBps = achieved;
            this.utilizationPercent = utilization;
            this.bottleneckType = bottleneck;
            this.recommendations = recommendations;
        }
    }
    
    /**
     * Estimate memory bandwidth requirements and identify bottlenecks
     */
    public static BandwidthAnalysis analyzeBandwidth(@NotNull KernelMemoryProfile profile) {
        // Intel GPU Max specifications (typical)
        final double PEAK_BANDWIDTH_GBPS = 600.0;  // HBM2e peak bandwidth
        final double CACHE_BANDWIDTH_GBPS = 1200.0; // L1/L2 cache
        
        // Calculate total bytes transferred
        long totalGlobalBytes = (profile.globalLoads + profile.globalStores) * 
                               profile.elementSize * profile.numWorkItems;
        long totalLocalBytes = (profile.localLoads + profile.localStores) * 
                              profile.elementSize * profile.numWorkItems;
        
        // Convert to GB/s
        double timeInSeconds = profile.executionTimeMs / 1000.0;
        double requiredBandwidth = (totalGlobalBytes / 1e9) / timeInSeconds;
        double achievedBandwidth = Math.min(requiredBandwidth, PEAK_BANDWIDTH_GBPS);
        double utilization = (requiredBandwidth / PEAK_BANDWIDTH_GBPS) * 100.0;
        
        // Determine bottleneck type
        String bottleneck;
        String[] recommendations;
        
        if (utilization > 90.0) {
            bottleneck = "Memory-bound (severe)";
            recommendations = new String[]{
                "Kernel is severely memory-bound",
                "Consider using local memory for frequently accessed data",
                "Implement data tiling to improve cache reuse",
                "Reduce redundant memory accesses",
                "Use sub-groups for coalesced memory access"
            };
        } else if (utilization > 70.0) {
            bottleneck = "Memory-bound (moderate)";
            recommendations = new String[]{
                "Kernel is moderately memory-bound",
                "Optimize memory access patterns for coalescing",
                "Use vectorization where possible (vec<T, N>)",
                "Consider async data transfers to overlap computation"
            };
        } else if (utilization > 40.0) {
            bottleneck = "Balanced (memory/compute)";
            recommendations = new String[]{
                "Kernel has balanced memory and compute",
                "Good candidate for further compute optimization",
                "Profile with VTune to identify specific hotspots",
                "Consider increasing arithmetic intensity"
            };
        } else {
            bottleneck = "Compute-bound";
            recommendations = new String[]{
                "Kernel is compute-bound (good!)",
                "Focus on algorithmic optimizations",
                "Consider using specialized math functions",
                "Ensure vectorization is applied where possible",
                "Check for instruction-level parallelism opportunities"
            };
        }
        
        return new BandwidthAnalysis(requiredBandwidth, achievedBandwidth, 
                                    utilization, bottleneck, recommendations);
    }
    
    public static void showBandwidthAnalysis(@NotNull Project project,
                                            @NotNull KernelMemoryProfile profile) {
        BandwidthAnalysis analysis = analyzeBandwidth(profile);
        
        StringBuilder message = new StringBuilder();
        message.append("Memory Bandwidth Analysis\n\n");
        
        message.append("📊 Bandwidth Metrics:\n");
        message.append(String.format("  Required: %.1f GB/s\n", analysis.requiredBandwidthGBps));
        message.append(String.format("  Achieved: %.1f GB/s\n", analysis.achievedBandwidthGBps));
        message.append(String.format("  Utilization: %.1f%%\n\n", analysis.utilizationPercent));
        
        message.append("📈 Memory Operations:\n");
        message.append(String.format("  Global loads: %d\n", profile.globalLoads));
        message.append(String.format("  Global stores: %d\n", profile.globalStores));
        message.append(String.format("  Local loads: %d\n", profile.localLoads));
        message.append(String.format("  Local stores: %d\n\n", profile.localStores));
        
        message.append(String.format("🔍 Bottleneck: %s\n\n", analysis.bottleneckType));
        
        message.append("💡 Recommendations:\n");
        for (String rec : analysis.recommendations) {
            message.append("  • ").append(rec).append("\n");
        }
        
        
        Messages.showInfoMessage(project, message.toString(), "Memory Bandwidth Estimator");
    }
    
    /**
     * Calculate arithmetic intensity (FLOPs per byte)
     */
    public static double calculateArithmeticIntensity(long numOperations, long numBytesTransferred) {
        return (double) numOperations / numBytesTransferred;
    }
    
    /**
     * Suggest optimal memory access pattern
     */
    public static String suggestAccessPattern(double arithmeticIntensity) {
        if (arithmeticIntensity < 0.5) {
            return "Use local memory tiling and increase data reuse";
        } else if (arithmeticIntensity < 2.0) {
            return "Optimize for coalesced memory access";
        } else if (arithmeticIntensity < 10.0) {
            return "Good balance - focus on compute optimization";
        } else {
            return "Compute-bound - consider loop unrolling and SIMD";
        }
    }
}
