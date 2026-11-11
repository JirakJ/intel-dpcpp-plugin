/*
 * Copyright (c) 2026 Jakub Jirák.
 * Licensed under the Apache License, Version 2.0. See LICENSE for details.
 */
package com.jakubjirak.dpcpp.actions;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.jakubjirak.dpcpp.oneapi.OneApiEnvironment;
import com.jakubjirak.dpcpp.oneapi.OneApiTools;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Optional;

/** Builds the Intel Advisor command line for roofline or offload-modeling collection. */
public class AdvisorRooflineAction extends AnAction {

    static final String[] ANALYSES = {"roofline", "offload"};

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Optional<Path> advisor = OneApiEnvironment.locate(OneApiTools.Tool.ADVISOR);
        if (advisor.isEmpty()) {
            OneApiEnvironment.showMissing(project, "Intel Advisor");
            return;
        }
        int choice = Messages.showChooseDialog(project, "Select the Intel Advisor collection:", "Intel Advisor",
                Messages.getQuestionIcon(), ANALYSES, ANALYSES[0]);
        if (choice < 0) {
            return;
        }
        OneApiEnvironment.showCommand(project, "Intel Advisor " + ANALYSES[choice],
                "Collect Advisor '" + ANALYSES[choice] + "' data for your SYCL application; "
                        + "open the project directory in the Advisor GUI to inspect the results.",
                OneApiTools.advisorCommand(advisor.get().toString(), ANALYSES[choice],
                        OneApiEnvironment.projectDir(project, ".advisor"), "<your-executable>"));
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(e.getProject() != null);
    }
}
