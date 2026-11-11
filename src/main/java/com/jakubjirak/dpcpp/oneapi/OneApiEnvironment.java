/*
 * Copyright (c) 2026 Jakub Jirák.
 * Licensed under the Apache License, Version 2.0. See LICENSE for details.
 */
package com.jakubjirak.dpcpp.oneapi;

import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.datatransfer.StringSelection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/** IDE-side glue for {@link OneApiTools}: reads the real environment and presents commands. */
public final class OneApiEnvironment {

    public static final String INSTALL_URL =
            "https://www.intel.com/content/www/us/en/developer/tools/oneapi/base-toolkit.html";

    private OneApiEnvironment() {
    }

    /** oneAPI roots to search on this machine. */
    @NotNull
    public static List<Path> roots() {
        return OneApiTools.candidateRoots(System.getenv("ONEAPI_ROOT"), System.getProperty("user.home"), SystemInfo.isWindows);
    }

    @NotNull
    public static Optional<Path> locate(@NotNull OneApiTools.Tool tool) {
        return OneApiTools.locate(tool, roots(), SystemInfo.isWindows);
    }

    /** Result directory for a tool inside the project, falling back to the working directory. */
    @NotNull
    public static Path projectDir(@Nullable Project project, @NotNull String name) {
        String base = project != null ? project.getBasePath() : null;
        return Paths.get(base != null ? base : System.getProperty("user.dir"), name);
    }

    /** Shows {@code command} with an explanation and copies it to the clipboard. */
    public static void showCommand(@Nullable Project project, @NotNull String title,
                                   @NotNull String explanation, @NotNull List<String> command) {
        String line = OneApiTools.toShellLine(command);
        CopyPasteManager.getInstance().setContents(new StringSelection(line));
        Messages.showInfoMessage(project,
                explanation + "\n\nRun in a terminal where setvars.sh has been sourced (copied to clipboard):\n\n" + line,
                title);
    }

    /** Error dialog for a missing tool. */
    public static void showMissing(@Nullable Project project, @NotNull String toolName) {
        Messages.showErrorDialog(project,
                toolName + " was not found in $ONEAPI_ROOT or the default oneAPI install locations.\n\n"
                        + "Install the Intel oneAPI Base Toolkit:\n" + INSTALL_URL,
                toolName + " Not Found");
    }
}
