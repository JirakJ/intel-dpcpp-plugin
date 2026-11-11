/*
 * Copyright (c) 2026 Jakub Jirák.
 * Licensed under the Apache License, Version 2.0. See LICENSE for details.
 */
package com.jakubjirak.dpcpp.highlighting;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Highlights SYCL identifiers ({@code sycl::xxx}) and kernel-launch constructs
 * ({@code parallel_for}, {@code single_task}, ...) in C++ files.
 * <p>
 * Only leaf PSI elements are annotated, and only the matched text range — annotating
 * composite elements would colour whole statements or the entire file.
 */
public class DpcppAnnotator implements Annotator {

    private static final Pattern SYCL_NAMESPACE_PATTERN = Pattern.compile("\\bsycl::\\w+");
    private static final Pattern PARALLEL_CONSTRUCT_PATTERN = Pattern.compile(
            "\\b(parallel_for_work_group|parallel_for|single_task)\\b");

    /** A highlighted span relative to the element start. */
    public static final class Span {
        public final int start;
        public final int end;
        public final boolean construct;

        Span(int start, int end, boolean construct) {
            this.start = start;
            this.end = end;
            this.construct = construct;
        }
    }

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element.getFirstChild() != null) {
            return;
        }
        int offset = element.getTextRange().getStartOffset();
        for (Span span : findSpans(element.getText())) {
            TextAttributesKey key = span.construct
                    ? DefaultLanguageHighlighterColors.KEYWORD
                    : DefaultLanguageHighlighterColors.INSTANCE_FIELD;
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(new TextRange(offset + span.start, offset + span.end))
                    .textAttributes(key)
                    .create();
        }
    }

    /** Finds SYCL identifiers and kernel-launch constructs in {@code text}. */
    @NotNull
    public static List<Span> findSpans(@NotNull String text) {
        List<Span> spans = new ArrayList<>();
        Matcher sycl = SYCL_NAMESPACE_PATTERN.matcher(text);
        while (sycl.find()) {
            spans.add(new Span(sycl.start(), sycl.end(), false));
        }
        Matcher construct = PARALLEL_CONSTRUCT_PATTERN.matcher(text);
        while (construct.find()) {
            spans.add(new Span(construct.start(), construct.end(), true));
        }
        return spans;
    }
}
