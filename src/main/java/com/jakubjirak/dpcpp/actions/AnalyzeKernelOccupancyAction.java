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
import com.jakubjirak.dpcpp.analysis.KernelOccupancyCalculator;
import org.jetbrains.annotations.NotNull;

/** Asks for a kernel's resource usage and shows the occupancy estimate. */
public class AnalyzeKernelOccupancyAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        String input = Messages.showInputDialog(
                project,
                "Work-group size, registers per thread, local memory bytes (e.g. 256, 32, 16384):",
                "Kernel Occupancy Calculator",
                Messages.getQuestionIcon(),
                "256, 32, 16384",
                null);
        if (input == null) {
            return;
        }
        KernelOccupancyCalculator.KernelConfiguration config = KernelOccupancyCalculator.parse(input);
        if (config == null) {
            Messages.showErrorDialog(project,
                    "Enter a positive work-group size, optionally followed by registers per thread and local memory bytes.",
                    "Invalid Input");
            return;
        }
        Messages.showInfoMessage(project,
                KernelOccupancyCalculator.report(config, KernelOccupancyCalculator.calculateOccupancy(config)),
                "Kernel Occupancy Calculator");
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
