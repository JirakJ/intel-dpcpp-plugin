/*
 * Copyright (c) 2026 Jakub Jirák.
 * Licensed under the Apache License, Version 2.0. See LICENSE for details.
 */
package com.jakubjirak.dpcpp.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.SystemInfo;
import com.jakubjirak.dpcpp.oneapi.OneApiEnvironment;
import com.jakubjirak.dpcpp.oneapi.OneApiTools;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;

/** Detects Intel oneAPI installations that contain the DPC++ compiler and reports them. */
public class ConfigureToolchainAction extends AnAction {

    static final String NOTIFICATION_GROUP = "DPC++ Notifications";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        List<Path> found = OneApiTools.installations(OneApiEnvironment.roots(), SystemInfo.isWindows);
        Notification notification = found.isEmpty()
                ? new Notification(NOTIFICATION_GROUP, "Intel oneAPI not found",
                        "No DPC++ compiler (icpx) found in $ONEAPI_ROOT or the default install locations. "
                                + "Install from " + OneApiEnvironment.INSTALL_URL,
                        NotificationType.WARNING)
                : new Notification(NOTIFICATION_GROUP, "Intel oneAPI detected", describe(found),
                        NotificationType.INFORMATION);
        Notifications.Bus.notify(notification, e.getProject());
    }

    /** Human-readable summary of detected installations. */
    static String describe(List<Path> installations) {
        StringBuilder message = new StringBuilder("Found Intel oneAPI installations:\n");
        for (Path path : installations) {
            message.append("• ").append(path).append('\n');
        }
        message.append("Configure the toolchain under Settings > Build, Execution, Deployment > Toolchains "
                + "(C++ compiler: icpx, add -fsycl in CMakeLists.txt).");
        return message.toString();
    }
}
