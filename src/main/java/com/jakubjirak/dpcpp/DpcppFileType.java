package com.jakubjirak.dpcpp;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import com.jetbrains.cidr.lang.OCLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DpcppFileType extends LanguageFileType {
    public static final DpcppFileType INSTANCE = new DpcppFileType();

    private DpcppFileType() {
        super(OCLanguage.getInstance());
    }

    @NotNull
    @Override
    public String getName() {
        return "DPC++";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Intel DPC++ (Data Parallel C++) source file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "dpcpp";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return IconLoader.getIcon("/icons/dpcpp.svg", DpcppFileType.class);
    }
}
