package com.jakubjirak.dpcpp.highlighting;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class DpcppAnnotator implements Annotator {
    
    private static final Pattern SYCL_NAMESPACE_PATTERN = Pattern.compile("sycl::\\w+");
    private static final Pattern PARALLEL_CONSTRUCT_PATTERN = Pattern.compile(
        "(parallel_for|single_task|parallel_for_work_group)"
    );

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        String text = element.getText();
        
        if (SYCL_NAMESPACE_PATTERN.matcher(text).find()) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element)
                .textAttributes(DefaultLanguageHighlighterColors.INSTANCE_FIELD)
                .create();
        }
        
        if (PARALLEL_CONSTRUCT_PATTERN.matcher(text).find()) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element)
                .textAttributes(DefaultLanguageHighlighterColors.KEYWORD)
                .create();
        }
    }
}
