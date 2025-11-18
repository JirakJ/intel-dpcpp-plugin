package com.jakubjirak.dpcpp.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.jakubjirak.dpcpp.profiler.VTuneProfilerIntegration;
import org.jetbrains.annotations.NotNull;

public class ProfileWithVTuneAction extends AnAction {
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        
        if (!VTuneProfilerIntegration.isAvailable()) {
            Messages.showErrorDialog(
                project,
                "Intel VTune Profiler not found.\n\n" +
                "Please install Intel oneAPI Base Toolkit:\n" +
                "https://www.intel.com/content/www/us/en/developer/tools/oneapi/base-toolkit.html",
                "VTune Profiler Not Found"
            );
            return;
        }
        
        String[] options = {"GPU Hotspots", "GPU Offload", "Kernel Analysis", "Memory Analysis"};
        int choice = Messages.showChooseDialog(
            project,
            "Select VTune analysis type:",
            "Profile with VTune",
            Messages.getQuestionIcon(),
            options,
            options[0]
        );
        
        switch (choice) {
            case 0:
                Messages.showInfoMessage(project, 
                    "GPU Hotspots analysis will identify performance bottlenecks\n" +
                    "in your SYCL kernels and show detailed metrics.\n\n" +
                    "Configure executable in Run Configuration first.",
                    "GPU Hotspots Analysis");
                break;
            case 1:
                Messages.showInfoMessage(project,
                    "GPU Offload analysis tracks host-device data transfers\n" +
                    "and identifies optimization opportunities.\n\n" +
                    "Configure executable in Run Configuration first.",
                    "GPU Offload Analysis");
                break;
            case 2:
                VTuneProfilerIntegration.showKernelAnalysis(project, "selected_kernel");
                break;
            case 3:
                VTuneProfilerIntegration.showMemoryAnalysis(project);
                break;
        }
    }
    
    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(e.getProject() != null);
    }
}
