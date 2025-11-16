package com.jakubjirak.dpcpp.actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigureToolchainAction extends AnAction {
    
    private static final String[] ONEAPI_PATHS = {
        "/opt/intel/oneapi",
        System.getProperty("user.home") + "/intel/oneapi",
        "C:\\Program Files (x86)\\Intel\\oneAPI",
        "/usr/local/intel/oneapi"
    };

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        List<String> foundPaths = new ArrayList<>();
        
        for (String path : ONEAPI_PATHS) {
            File oneapiDir = new File(path);
            if (oneapiDir.exists() && oneapiDir.isDirectory()) {
                File compilerDir = new File(oneapiDir, "compiler/latest/bin");
                if (compilerDir.exists()) {
                    File icpx = new File(compilerDir, isWindows() ? "icpx.exe" : "icpx");
                    if (icpx.exists()) {
                        foundPaths.add(path);
                    }
                }
            }
        }
        
        if (foundPaths.isEmpty()) {
            Notifications.Bus.notify(new Notification(
                "DPC++ Notifications",
                "Intel oneAPI Not Found",
                "Could not detect Intel oneAPI installation.\n" +
                "Please install from: https://www.intel.com/content/www/us/en/developer/tools/oneapi/base-toolkit-download.html",
                NotificationType.WARNING
            ), e.getProject());
        } else {
            StringBuilder message = new StringBuilder("Found Intel oneAPI installations:\n\n");
            for (String path : foundPaths) {
                message.append("• ").append(path).append("\n");
            }
            message.append("\nConfigure in Settings > Build > Toolchains");
            
            Notifications.Bus.notify(new Notification(
                "DPC++ Notifications",
                "Intel oneAPI Detected",
                message.toString(),
                NotificationType.INFORMATION
            ), e.getProject());
        }
    }
    
    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
