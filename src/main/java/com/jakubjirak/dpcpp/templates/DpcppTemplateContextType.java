package com.jakubjirak.dpcpp.templates;

import com.intellij.codeInsight.template.TemplateActionContext;
import com.intellij.codeInsight.template.TemplateContextType;
import org.jetbrains.annotations.NotNull;

public class DpcppTemplateContextType extends TemplateContextType {
    
    protected DpcppTemplateContextType() {
        super("DPC++");
    }
    
    @NotNull
    @Override
    public String getPresentableName() {
        return "DPC++/SYCL";
    }

    @Override
    public boolean isInContext(@NotNull TemplateActionContext templateActionContext) {
        String fileName = templateActionContext.getFile().getName();
        return fileName.endsWith(".dpcpp") || 
               fileName.endsWith(".dp.cpp") || 
               fileName.endsWith(".cpp");
    }
}
