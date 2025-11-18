package com.jakubjirak.dpcpp.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.jakubjirak.dpcpp.multidevice.MultiDeviceOrchestrator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class MultiDeviceAction extends AnAction {
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        
        // Example devices
        List<MultiDeviceOrchestrator.DeviceInfo> devices = Arrays.asList(
            new MultiDeviceOrchestrator.DeviceInfo(
                "Intel(R) Data Center GPU Max 1550",
                "Level Zero GPU",
                128,
                51539607552L,  // 48 GB
                1600
            ),
            new MultiDeviceOrchestrator.DeviceInfo(
                "Intel(R) Xeon(R) CPU",
                "OpenCL CPU",
                32,
                134217728000L, // 128 GB
                3500
            ),
            new MultiDeviceOrchestrator.DeviceInfo(
                "Intel(R) Arc A770",
                "Level Zero GPU",
                32,
                17179869184L,  // 16 GB
                2100
            )
        );
        
        long totalWorkload = 10000000; // 10M items
        
        MultiDeviceOrchestrator.showOrchestrationReport(project, devices, totalWorkload);
    }
    
    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(e.getProject() != null);
    }
}
