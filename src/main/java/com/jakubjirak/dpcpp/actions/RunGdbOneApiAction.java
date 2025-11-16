package com.jakubjirak.dpcpp.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class RunGdbOneApiAction extends AnAction {
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Messages.showInfoMessage(
            e.getProject(),
            "gdb-oneapi integration coming in v0.2\n\n" +
            "For now, use terminal:\n" +
            "  source /opt/intel/oneapi/setvars.sh\n" +
            "  gdb-oneapi ./your_program",
            "Intel oneAPI Debugger"
        );
    }
}
