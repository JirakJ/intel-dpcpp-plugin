package com.jakubjirak.dpcpp.advisor;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Integration with Intel Advisor for roofline analysis and optimization recommendations
 */
public class AdvisorIntegration {
    
    private static final String ADVISOR_PATH = "/opt/intel/oneapi/advisor/latest/bin64/advisor";
    
    public static boolean isAvailable() {
        File advisor = new File(ADVISOR_PATH);
        return advisor.exists() && advisor.canExecute();
    }
    
    /**
     * Run roofline analysis on SYCL application
     */
    public static GeneralCommandLine createRooflineAnalysis(@NotNull Project project,
                                                            @NotNull String executablePath)
            throws ExecutionException {
        if (!isAvailable()) {
            throw new ExecutionException("Intel Advisor not found. Install Intel oneAPI Base Toolkit.");
        }
        
        Path resultDir = Paths.get(project.getBasePath(), ".advisor");
        
        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine.setExePath(ADVISOR_PATH);
        commandLine.addParameter("--collect=roofline");
        commandLine.addParameter("--project-dir=" + resultDir.toString());
        commandLine.addParameter("--");
        commandLine.addParameter(executablePath);
        
        return commandLine;
    }
    
    /**
     * Run offload modeling analysis
     */
    public static GeneralCommandLine createOffloadAnalysis(@NotNull Project project,
                                                          @NotNull String executablePath)
            throws ExecutionException {
        if (!isAvailable()) {
            throw new ExecutionException("Intel Advisor not found.");
        }
        
        Path resultDir = Paths.get(project.getBasePath(), ".advisor");
        
        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine.setExePath(ADVISOR_PATH);
        commandLine.addParameter("--collect=offload");
        commandLine.addParameter("--project-dir=" + resultDir.toString());
        commandLine.addParameter("--");
        commandLine.addParameter(executablePath);
        
        return commandLine;
    }
    
    /**
     * Show roofline chart and analysis
     */
    public static void showRooflineAnalysis(@NotNull Project project) {
        StringBuilder message = new StringBuilder();
        message.append("Intel Advisor - Roofline Analysis\n\n");
        
        message.append("📊 Roofline Model:\n");
        message.append("Visualizes performance bottlenecks by plotting:\n");
        message.append("  • Arithmetic Intensity (FLOPs/Byte)\n");
        message.append("  • Performance (GFLOPs/s)\n");
        message.append("  • Memory vs Compute bounds\n\n");
        
        message.append("🎯 Analysis Types:\n");
        message.append("  • Survey: Identify optimization opportunities\n");
        message.append("  • Roofline: Memory/compute characterization\n");
        message.append("  • Dependencies: Find parallelization blockers\n");
        message.append("  • Offload: GPU offload effectiveness\n\n");
        
        message.append("📈 Metrics Provided:\n");
        message.append("  • GFLOP/s (actual vs peak)\n");
        message.append("  • Memory bandwidth (actual vs peak)\n");
        message.append("  • Arithmetic intensity\n");
        message.append("  • Vectorization efficiency\n");
        message.append("  • Cache utilization\n\n");
        
        message.append("💡 Optimization Workflow:\n");
        message.append("  1. Collect roofline data\n");
        message.append("  2. Identify bottleneck (memory vs compute)\n");
        message.append("  3. Apply targeted optimizations\n");
        message.append("  4. Re-measure and verify improvement\n\n");
        
        message.append("Available in Professional Edition");
        
        Messages.showInfoMessage(project, message.toString(), "Intel Advisor Roofline");
    }
    
    /**
     * Show offload modeling recommendations
     */
    public static void showOffloadRecommendations(@NotNull Project project) {
        StringBuilder message = new StringBuilder();
        message.append("Intel Advisor - Offload Modeling\n\n");
        
        message.append("🎯 Analysis Focus:\n");
        message.append("  • Identify code suitable for GPU offload\n");
        message.append("  • Estimate speedup potential\n");
        message.append("  • Detect parallelization opportunities\n");
        message.append("  • Find data transfer bottlenecks\n\n");
        
        message.append("📊 Metrics:\n");
        message.append("  • Potential speedup from GPU offload\n");
        message.append("  • Data transfer overhead\n");
        message.append("  • Kernel launch overhead\n");
        message.append("  • Host-device synchronization cost\n\n");
        
        message.append("💡 Recommendations Include:\n");
        message.append("  • Which loops to offload to GPU\n");
        message.append("  • Optimal work distribution strategy\n");
        message.append("  • Memory transfer optimization hints\n");
        message.append("  • Kernel fusion opportunities\n\n");
        
        message.append("Available in Professional Edition");
        
        Messages.showInfoMessage(project, message.toString(), "Intel Advisor Offload");
    }
    
    /**
     * Analyze threading efficiency
     */
    public static void showThreadingAnalysis(@NotNull Project project) {
        StringBuilder message = new StringBuilder();
        message.append("Intel Advisor - Threading Analysis\n\n");
        
        message.append("🔍 Threading Checks:\n");
        message.append("  • Data race detection\n");
        message.append("  • Deadlock detection\n");
        message.append("  • Load imbalance analysis\n");
        message.append("  • Lock contention\n\n");
        
        message.append("📈 Scalability Metrics:\n");
        message.append("  • Speedup vs thread count\n");
        message.append("  • CPU utilization\n");
        message.append("  • Wait time analysis\n");
        message.append("  • Critical path identification\n\n");
        
        message.append("💡 Optimization Hints:\n");
        message.append("  • Reduce synchronization overhead\n");
        message.append("  • Balance work distribution\n");
        message.append("  • Minimize false sharing\n");
        message.append("  • Optimize parallel regions\n\n");
        
        message.append("Available in Professional Edition");
        
        Messages.showInfoMessage(project, message.toString(), "Intel Advisor Threading");
    }
}
