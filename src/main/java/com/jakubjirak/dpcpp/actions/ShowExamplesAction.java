package com.jakubjirak.dpcpp.actions;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class ShowExamplesAction extends AnAction {
    
    private static final String ONEAPI_SAMPLES_URL = 
        "https://github.com/oneapi-src/oneAPI-samples";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        BrowserUtil.browse(ONEAPI_SAMPLES_URL);
    }
}
