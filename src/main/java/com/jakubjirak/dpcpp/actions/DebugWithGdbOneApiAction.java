/*
 * Copyright (c) 2026 Jakub Jirák.
 * Licensed under the Apache License, Version 2.0. See LICENSE for details.
 */
package com.jakubjirak.dpcpp.actions;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.jakubjirak.dpcpp.oneapi.OneApiEnvironment;
import com.jakubjirak.dpcpp.oneapi.OneApiTools;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Optional;

/** Shows (and copies) the gdb-oneapi command for debugging SYCL kernels on the GPU. */
public class DebugWithGdbOneApiAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Optional<Path> gdb = OneApiEnvironment.locate(OneApiTools.Tool.GDB_ONEAPI);
        if (gdb.isEmpty()) {
            OneApiEnvironment.showMissing(project, "gdb-oneapi");
            return;
        }
        OneApiEnvironment.showCommand(project, "Debug with gdb-oneapi",
                "gdb-oneapi can set breakpoints inside SYCL kernels and inspect GPU threads. "
                        + "Build with -g -O0 for best results.",
                OneApiTools.gdbCommand(gdb.get().toString(), "<your-executable>"));
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
