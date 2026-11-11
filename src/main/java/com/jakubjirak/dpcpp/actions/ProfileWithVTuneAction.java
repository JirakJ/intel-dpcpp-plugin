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

/** Builds the VTune Profiler command line for a GPU analysis of the user's executable. */
public class ProfileWithVTuneAction extends AnAction {

    static final String[] ANALYSES = {"gpu-hotspots", "gpu-offload", "memory-access"};

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Optional<Path> vtune = OneApiEnvironment.locate(OneApiTools.Tool.VTUNE);
        if (vtune.isEmpty()) {
            OneApiEnvironment.showMissing(project, "Intel VTune Profiler");
            return;
        }
        int choice = Messages.showChooseDialog(project, "Select the VTune analysis type:", "Profile with VTune",
                Messages.getQuestionIcon(), ANALYSES, ANALYSES[0]);
        if (choice < 0) {
            return;
        }
        OneApiEnvironment.showCommand(project, "VTune " + ANALYSES[choice],
                "Profile your SYCL application with the VTune '" + ANALYSES[choice] + "' analysis; "
                        + "open the result directory in VTune GUI or with 'vtune -report summary'.",
                OneApiTools.vtuneCommand(vtune.get().toString(), ANALYSES[choice],
                        OneApiEnvironment.projectDir(project, ".vtune"), "<your-executable>"));
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
