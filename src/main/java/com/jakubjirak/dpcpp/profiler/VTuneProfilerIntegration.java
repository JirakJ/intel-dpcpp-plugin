package com.jakubjirak.dpcpp.profiler;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VTuneProfilerIntegration {
    
    private static final String VTUNE_PATH = "/opt/intel/oneapi/vtune/latest/bin64/vtune";
    
    public static boolean isAvailable() {
        File vtune = new File(VTUNE_PATH);
        return vtune.exists() && vtune.canExecute();
    }
    
    public static GeneralCommandLine createGpuHotspotAnalysis(@NotNull Project project,
                                                               @NotNull String executablePath)
            throws ExecutionException {
        if (!isAvailable()) {
            throw new ExecutionException("VTune Profiler not found. Install Intel oneAPI Base Toolkit.");
        }
        
        Path resultDir = Paths.get(project.getBasePath(), ".vtune");
        
        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine.setExePath(VTUNE_PATH);
        commandLine.addParameter("-collect");
        commandLine.addParameter("gpu-hotspots");
        commandLine.addParameter("-result-dir");
        commandLine.addParameter(resultDir.toString());
        commandLine.addParameter("--");
        commandLine.addParameter(executablePath);
        
        return commandLine;
    }
    
    public static GeneralCommandLine createGpuOffloadAnalysis(@NotNull Project project,
                                                              @NotNull String executablePath)
            throws ExecutionException {
        if (!isAvailable()) {
            throw new ExecutionException("VTune Profiler not found.");
        }
        
        Path resultDir = Paths.get(project.getBasePath(), ".vtune");
        
        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine.setExePath(VTUNE_PATH);
        commandLine.addParameter("-collect");
        commandLine.addParameter("gpu-offload");
        commandLine.addParameter("-result-dir");
        commandLine.addParameter(resultDir.toString());
        commandLine.addParameter("--");
        commandLine.addParameter(executablePath);
        
        return commandLine;
    }
    
    public static void showKernelAnalysis(@NotNull Project project, @NotNull String kernelName) {
        Messages.showInfoMessage(
            project,
            String.format("Kernel Performance Analysis: %s\n\n" +
                "Features:\n" +
                "• GPU utilization timeline\n" +
                "• Memory bandwidth analysis\n" +
                "• Occupancy metrics\n" +
                "• Bottleneck identification", kernelName),
            "VTune Kernel Analysis"
        );
    }
    
    public static void showMemoryAnalysis(@NotNull Project project) {
        Messages.showInfoMessage(
            project,
            "Memory Performance Analysis\n\n" +
            "Features:\n" +
            "• Host-device transfer tracking\n" +
            "• USM allocation patterns\n" +
            "• Cache efficiency metrics\n" +
            "• Memory bandwidth utilization",
            "VTune Memory Analysis"
        );
    }
}
