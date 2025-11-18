package com.jakubjirak.dpcpp.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.jakubjirak.dpcpp.fpga.FpgaOptimizationAnalyzer;
import org.jetbrains.annotations.NotNull;

public class FpgaOptimizationAction extends AnAction {
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        
        // Example FPGA kernel profile
        FpgaOptimizationAnalyzer.FpgaKernelProfile profile = 
            new FpgaOptimizationAnalyzer.FpgaKernelProfile(
                "matrix_multiply",
                3,      // loop depth
                16,     // pipeline depth
                2,      // unroll factor
                false,  // has memory dependency
                100     // estimated latency cycles
            );
        
        FpgaOptimizationAnalyzer.showFpgaOptimizationReport(project, profile);
    }
    
    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(e.getProject() != null);
    }
}
