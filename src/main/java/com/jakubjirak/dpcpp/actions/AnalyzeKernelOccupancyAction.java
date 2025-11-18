package com.jakubjirak.dpcpp.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.jakubjirak.dpcpp.analysis.KernelOccupancyCalculator;
import org.jetbrains.annotations.NotNull;

public class AnalyzeKernelOccupancyAction extends AnAction {
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        
        // Show dialog to input kernel parameters
        String workGroupSizeStr = Messages.showInputDialog(
            project,
            "Enter work-group size (e.g., 256):",
            "Kernel Occupancy Calculator",
            Messages.getQuestionIcon(),
            "256",
            null
        );
        
        if (workGroupSizeStr == null) return;
        
        try {
            int workGroupSize = Integer.parseInt(workGroupSizeStr);
            
            // Example configuration - in production, parse from actual kernel code
            KernelOccupancyCalculator.KernelConfiguration config = 
                new KernelOccupancyCalculator.KernelConfiguration(
                    workGroupSize,
                    32,  // registers per thread (estimated)
                    16384, // local memory (16 KB)
                    "Intel(R) Data Center GPU Max 1550"
                );
            
            KernelOccupancyCalculator.showOccupancyAnalysis(project, config);
            
        } catch (NumberFormatException ex) {
            Messages.showErrorDialog(
                project,
                "Invalid work-group size. Please enter a number.",
                "Input Error"
            );
        }
    }
    
    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(e.getProject() != null);
    }
}
