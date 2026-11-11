/*
 * Copyright (c) 2026 Jakub Jirák.
 * Licensed under the Apache License, Version 2.0. See LICENSE for details.
 */
package com.jakubjirak.dpcpp.documentation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DpcppDocumentationProviderTest {

    @Test
    void documentsQueueWithSpecLink() {
        String doc = DpcppDocumentationProvider.docFor("sycl::queue q{sycl::gpu_selector_v};");
        assertTrue(doc.startsWith("<b>sycl::queue</b>"));
        assertTrue(doc.contains("registry.khronos.org"));
    }

    @Test
    void documentsParallelFor() {
        assertTrue(DpcppDocumentationProvider.docFor("h.parallel_for(range, k)").startsWith("<b>parallel_for</b>"));
    }

    @Test
    void unknownTextHasNoDocumentation() {
        assertNull(DpcppDocumentationProvider.docFor("int main() {}"));
        assertNull(DpcppDocumentationProvider.docFor(null));
        assertNull(new DpcppDocumentationProvider().generateDoc(null, null));
    }
}
