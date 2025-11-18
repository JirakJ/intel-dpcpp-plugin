package com.jakubjirak.dpcpp.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.jakubjirak.dpcpp.ai.AiOptimizationAssistant;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class AiOptimizationAction extends AnAction {
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        
        // Example kernel code for analysis
        String kernelCode = 
            "q.submit([&](handler& h) {\n" +
            "  accessor acc_a(buf_a, h, read_only);\n" +
            "  accessor acc_b(buf_b, h, read_only);\n" +
            "  accessor acc_c(buf_c, h, write_only);\n" +
            "  h.parallel_for(range<1>(N), [=](id<1> i) {\n" +
            "    acc_c[i] = acc_a[i] + acc_b[i];\n" +
            "  });\n" +
            "}).wait();";
        
        AiOptimizationAssistant.KernelCode kernel = 
            new AiOptimizationAssistant.KernelCode(
                kernelCode,
                "vector_add",
                new HashMap<>()
            );
        
        AiOptimizationAssistant.showAiSuggestions(project, kernel);
    }
    
    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(e.getProject() != null);
    }
}
