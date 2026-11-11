package com.jakubjirak.dpcpp.documentation;

import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public class DpcppDocumentationProvider extends AbstractDocumentationProvider {
    
    @Nullable
    @Override
    public String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
        return element == null ? null : docFor(element.getText());
    }

    /** Documentation for SYCL constructs mentioned in {@code text}, or {@code null}. */
    @Nullable
    static String docFor(@Nullable String text) {
        if (text == null) {
            return null;
        }
        
        if (text.contains("sycl::queue")) {
            return "<b>sycl::queue</b><br/>" +
                   "Encapsulates a single SYCL queue for submitting commands to a device.<br/><br/>" +
                   "<a href='https://registry.khronos.org/SYCL/specs/sycl-2020/html/sycl-2020.html#sec:interface.queue.class'>SYCL 2020 Specification</a>";
        }
        
        if (text.contains("parallel_for")) {
            return "<b>parallel_for</b><br/>" +
                   "Execute a kernel in parallel across a range of work-items.<br/><br/>" +
                   "Usage: <code>h.parallel_for(range, [=](id idx) { ... });</code>";
        }
        
        return null;
    }

    @Nullable
    @Override
    public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
        return generateDoc(element, originalElement);
    }
}
