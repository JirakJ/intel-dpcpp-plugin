/*
 * Copyright (c) 2026 Jakub Jirák.
 * Licensed under the Apache License, Version 2.0. See LICENSE for details.
 */
package com.jakubjirak.dpcpp;

import com.jakubjirak.dpcpp.highlighting.DpcppAnnotator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EditorSupportTest {

    @Test
    void annotatorFindsSyclIdentifiersAndConstructs() {
        String text = "sycl::queue q; h.parallel_for(r, k); single_task";
        List<DpcppAnnotator.Span> spans = DpcppAnnotator.findSpans(text);
        assertEquals(3, spans.size());
        assertEquals("sycl::queue", text.substring(spans.get(0).start, spans.get(0).end));
        assertFalse(spans.get(0).construct);
        assertEquals("parallel_for", text.substring(spans.get(1).start, spans.get(1).end));
        assertTrue(spans.get(1).construct);
        assertEquals("single_task", text.substring(spans.get(2).start, spans.get(2).end));
    }

    @Test
    void annotatorPrefersLongestConstructAndRespectsWordBoundaries() {
        String text = "parallel_for_work_group my_parallel_for nosycl::x";
        List<DpcppAnnotator.Span> spans = DpcppAnnotator.findSpans(text);
        assertEquals(1, spans.size());
        assertEquals("parallel_for_work_group", text.substring(spans.get(0).start, spans.get(0).end));
    }

    @Test
    void annotatorIgnoresPlainCpp() {
        assertTrue(DpcppAnnotator.findSpans("std::vector<int> v; for (auto x : v) {}").isEmpty());
        assertTrue(DpcppAnnotator.findSpans("").isEmpty());
    }

    @Test
    void pluginXmlRegistersExtensionsForTheClionLanguageId() throws IOException {
        String xml = resource("/META-INF/plugin.xml");
        assertFalse(xml.contains("language=\"C++\""), "CLion's C/C++ language id is ObjectiveC, not C++");
        assertTrue(xml.contains("language=\"ObjectiveC\""));
        assertTrue(xml.contains("name=\"DPC++\""), "fileType name must match DpcppFileType#getName");
        assertTrue(xml.contains("<notificationGroup id=\"Intel DPC++ Support Feedback\""));
    }

    @Test
    void liveTemplatesUseTheRegisteredContext() throws IOException {
        String templates = resource("/liveTemplates/DpcppTemplates.xml");
        for (String name : new String[]{"syclqueue", "syclbuffer", "syclsubmit", "syclparallel", "syclaccess", "syclusm"}) {
            assertTrue(templates.contains("name=\"" + name + "\""), name);
        }
        assertTrue(templates.contains("<option name=\"C++\" value=\"true\""));
    }

    private static String resource(String path) throws IOException {
        try (InputStream in = EditorSupportTest.class.getResourceAsStream(path)) {
            assertNotNull(in, path);
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
