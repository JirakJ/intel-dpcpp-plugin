package com.jakubjirak.dpcpp.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class NewDpcppProjectAction extends AnAction {
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Messages.showInfoMessage(
            e.getProject(),
            "Use File > New > Project and select 'C++ Executable' template.\n\n" +
            "Then configure CMakeLists.txt with:\n" +
            "  set(CMAKE_CXX_COMPILER icpx)\n" +
            "  add_compile_options(-fsycl)\n" +
            "  add_link_options(-fsycl)",
            "Create DPC++ Project"
        );
    }
}
