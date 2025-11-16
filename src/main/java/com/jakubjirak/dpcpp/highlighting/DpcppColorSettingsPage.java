package com.jakubjirak.dpcpp.highlighting;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class DpcppColorSettingsPage implements ColorSettingsPage {

    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
        new AttributesDescriptor("SYCL Keyword", DpcppSyntaxHighlighter.SYCL_KEYWORD),
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return IconLoader.getIcon("/icons/dpcpp.svg", DpcppColorSettingsPage.class);
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new DpcppSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return """
            #include <sycl/sycl.hpp>
            
            int main() {
                sycl::queue q{sycl::gpu_selector{}};
                
                const int N = 1024;
                sycl::buffer<int> buf(N);
                
                q.submit([&](sycl::handler& h) {
                    auto acc = buf.get_access<sycl::access::mode::write>(h);
                    h.parallel_for(sycl::range<1>(N), [=](sycl::id<1> idx) {
                        acc[idx] = idx[0] * 2;
                    });
                }).wait();
                
                return 0;
            }
            """;
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Intel DPC++";
    }
}
