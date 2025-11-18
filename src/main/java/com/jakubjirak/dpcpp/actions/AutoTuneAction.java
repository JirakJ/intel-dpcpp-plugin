package com.jakubjirak.dpcpp.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.jakubjirak.dpcpp.autotuning.AutoTuningFramework;
import org.jetbrains.annotations.NotNull;

public class AutoTuneAction extends AnAction {
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        
        // Auto-tuning configuration
        int[] workGroupSizes = {64, 128, 256, 512, 1024};
        int[] tileSizes = {8, 16, 32};
        
        AutoTuningFramework.TuningConfiguration config = 
            new AutoTuningFramework.TuningConfiguration(
                "vector_add",
                workGroupSizes,
                tileSizes,
                5  // iterations per config
            );
        
        AutoTuningFramework.showAutoTuningResults(project, config);
    }
    
    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(e.getProject() != null);
    }
}
