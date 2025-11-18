package com.jakubjirak.dpcpp.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.jakubjirak.dpcpp.advisor.AdvisorIntegration;
import org.jetbrains.annotations.NotNull;

public class AdvisorRooflineAction extends AnAction {
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        
        if (!AdvisorIntegration.isAvailable()) {
            Messages.showErrorDialog(
                project,
                "Intel Advisor not found.\n\n" +
                "Please install Intel oneAPI Base Toolkit:\n" +
                "https://www.intel.com/content/www/us/en/developer/tools/oneapi/base-toolkit.html",
                "Intel Advisor Not Found"
            );
            return;
        }
        
        String[] options = {"Roofline Analysis", "Offload Modeling", "Threading Analysis"};
        int choice = Messages.showChooseDialog(
            project,
            "Select Intel Advisor analysis type:",
            "Intel Advisor",
            Messages.getQuestionIcon(),
            options,
            options[0]
        );
        
        switch (choice) {
            case 0:
                AdvisorIntegration.showRooflineAnalysis(project);
                break;
            case 1:
                AdvisorIntegration.showOffloadRecommendations(project);
                break;
            case 2:
                AdvisorIntegration.showThreadingAnalysis(project);
                break;
        }
    }
    
    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(e.getProject() != null);
    }
}
