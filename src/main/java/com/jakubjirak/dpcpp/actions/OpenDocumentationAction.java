package com.jakubjirak.dpcpp.actions;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class OpenDocumentationAction extends AnAction {
    
    private static final String DPCPP_DOCS_URL = 
        "https://www.intel.com/content/www/us/en/docs/dpcpp-cpp-compiler/developer-guide-reference/current/overview.html";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        BrowserUtil.browse(DPCPP_DOCS_URL);
    }
}
