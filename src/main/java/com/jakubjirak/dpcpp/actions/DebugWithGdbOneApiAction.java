package com.jakubjirak.dpcpp.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.jakubjirak.dpcpp.debugger.GdbOneApiDebuggerSupport;
import org.jetbrains.annotations.NotNull;

public class DebugWithGdbOneApiAction extends AnAction {
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        
        if (!GdbOneApiDebuggerSupport.isAvailable()) {
            Messages.showErrorDialog(
                project,
                "gdb-oneapi debugger not found.\n\n" +
                "Please install Intel oneAPI Base Toolkit:\n" +
                "https://www.intel.com/content/www/us/en/developer/tools/oneapi/base-toolkit.html\n\n" +
                "After installation, source the environment:\n" +
                "source /opt/intel/oneapi/setvars.sh",
                "gdb-oneapi Not Found"
            );
            return;
        }
        
        Messages.showInfoMessage(
            project,
            "GPU Debugging with gdb-oneapi\n\n" +
            "Debugging features:\n" +
            "• Set breakpoints in SYCL kernels\n" +
            "• Inspect GPU thread state\n" +
            "• View device memory\n" +
            "• Step through kernel execution\n" +
            "• Conditional breakpoints on GPU\n\n" +
            "Configure debug configuration in Run/Debug Configurations first.",
            "GPU Debugging"
        );
    }
    
    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setEnabledAndVisible(e.getProject() != null);
    }
}
