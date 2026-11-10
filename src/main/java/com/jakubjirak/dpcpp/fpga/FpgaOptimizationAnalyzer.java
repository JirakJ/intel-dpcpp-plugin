package com.jakubjirak.dpcpp.fpga;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * FPGA-specific optimization analysis and recommendations
 * for Intel oneAPI DPC++ targeting FPGA devices
 */
public class FpgaOptimizationAnalyzer {
    
    public static class FpgaKernelProfile {
        public final String kernelName;
        public final int loopDepth;
        public final int pipelineDepth;
        public final int unrollFactor;
        public final boolean hasMemoryDependency;
        public final int estimatedLatency;
        
        public FpgaKernelProfile(String kernelName, int loopDepth, int pipelineDepth,
                                int unrollFactor, boolean hasMemoryDependency, 
                                int estimatedLatency) {
            this.kernelName = kernelName;
            this.loopDepth = loopDepth;
            this.pipelineDepth = pipelineDepth;
            this.unrollFactor = unrollFactor;
            this.hasMemoryDependency = hasMemoryDependency;
            this.estimatedLatency = estimatedLatency;
        }
    }
    
    public static class FpgaOptimizationReport {
        public final int estimatedFmax;
        public final int estimatedLogicUtilization;
        public final int estimatedMemoryUtilization;
        public final String[] optimizationHints;
        public final String[] pragmaSuggestions;
        
        public FpgaOptimizationReport(int fmax, int logicUtil, int memUtil,
                                     String[] hints, String[] pragmas) {
            this.estimatedFmax = fmax;
            this.estimatedLogicUtilization = logicUtil;
            this.estimatedMemoryUtilization = memUtil;
            this.optimizationHints = hints;
            this.pragmaSuggestions = pragmas;
        }
    }
    
    /**
     * Analyze FPGA kernel and provide optimization recommendations
     */
    public static FpgaOptimizationReport analyzeFpgaKernel(@NotNull FpgaKernelProfile profile) {
        int estimatedFmax = 300; // MHz baseline
        int logicUtilization = 50; // percentage
        int memoryUtilization = 30; // percentage
        
        // Adjust based on kernel characteristics
        if (profile.loopDepth > 3) {
            estimatedFmax -= 50;
            logicUtilization += 15;
        }
        
        if (profile.unrollFactor > 4) {
            logicUtilization += profile.unrollFactor * 5;
        }
        
        if (profile.hasMemoryDependency) {
            estimatedFmax -= 100;
        }
        
        // Generate optimization hints
        String[] hints = generateOptimizationHints(profile);
        String[] pragmas = generatePragmaSuggestions(profile);
        
        return new FpgaOptimizationReport(estimatedFmax, logicUtilization, 
                                         memoryUtilization, hints, pragmas);
    }
    
    private static String[] generateOptimizationHints(@NotNull FpgaKernelProfile profile) {
        java.util.List<String> hints = new java.util.ArrayList<>();
        
        if (profile.loopDepth > 2) {
            hints.add("Consider loop flattening to reduce loop depth");
        }
        
        if (profile.unrollFactor == 1) {
            hints.add("Add loop unrolling for better throughput");
        }
        
        if (profile.hasMemoryDependency) {
            hints.add("Remove memory dependencies or use memory buffers");
        }
        
        if (profile.pipelineDepth < 10) {
            hints.add("Increase pipeline depth with [[intel::initiation_interval(1)]]");
        }
        
        hints.add("Use streaming interfaces for continuous data flow");
        hints.add("Consider data reordering for better memory access");
        
        return hints.toArray(new String[0]);
    }
    
    private static String[] generatePragmaSuggestions(@NotNull FpgaKernelProfile profile) {
        java.util.List<String> pragmas = new java.util.ArrayList<>();
        
        // Pipeline pragma
        pragmas.add("[[intel::initiation_interval(1)]]");
        
        // Loop unrolling
        if (profile.unrollFactor <= 2) {
            pragmas.add("[[intel::loop_coalesce(2)]]");
            pragmas.add("#pragma unroll 4");
        }
        
        // Memory optimizations
        pragmas.add("[[intel::kernel_args_restrict]]");
        pragmas.add("[[intel::num_simd_work_items(4)]]");
        
        // Floating point optimizations
        pragmas.add("[[intel::fp_contract(on)]]");
        
        return pragmas.toArray(new String[0]);
    }
    
    public static void showFpgaOptimizationReport(@NotNull Project project,
                                                 @NotNull FpgaKernelProfile profile) {
        FpgaOptimizationReport report = analyzeFpgaKernel(profile);
        
        StringBuilder message = new StringBuilder();
        message.append("FPGA Optimization Analysis\n\n");
        message.append(String.format("Kernel: %s\n\n", profile.kernelName));
        
        message.append("📊 Estimated Metrics:\n");
        message.append(String.format("  Fmax: ~%d MHz\n", report.estimatedFmax));
        message.append(String.format("  Logic utilization: ~%d%%\n", report.estimatedLogicUtilization));
        message.append(String.format("  Memory utilization: ~%d%%\n\n", report.estimatedMemoryUtilization));
        
        message.append("💡 Optimization Hints:\n");
        for (String hint : report.optimizationHints) {
            message.append("  • ").append(hint).append("\n");
        }
        
        message.append("\n🔧 Suggested Pragmas:\n");
        for (String pragma : report.pragmaSuggestions) {
            message.append("  ").append(pragma).append("\n");
        }
        
        message.append("\n⚠️ FPGA-Specific Considerations:\n");
        message.append("  • Synthesis time: 2-8 hours\n");
        message.append("  • Emulation mode for fast iteration\n");
        message.append("  • Use fpga_simulator for quick testing\n");
        message.append("  • Profile with dynamic profiler\n\n");
        
        
        Messages.showInfoMessage(project, message.toString(), "FPGA Optimization Analyzer");
    }
    
    /**
     * Estimate FPGA resource usage
     */
    public static String estimateResourceUsage(int loopUnroll, int pipelineDepth) {
        int alus = loopUnroll * 100;
        int registers = pipelineDepth * 500;
        int memory = loopUnroll * 1024;
        
        return String.format(
            "Estimated Resources:\n" +
            "  ALUs: ~%d\n" +
            "  Registers: ~%d\n" +
            "  Memory (bytes): ~%d",
            alus, registers, memory
        );
    }
}
