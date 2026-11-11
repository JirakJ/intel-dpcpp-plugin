/*
 * Copyright (c) 2026 Jakub Jirák.
 * Licensed under the Apache License, Version 2.0. See LICENSE for details.
 */
package com.jakubjirak.dpcpp.completion;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DpcppCompletionContributorTest {

    @Test
    void everyEntryHasLookupStringAndDescription() {
        for (String[] entry : DpcppCompletionContributor.SYCL_COMPLETIONS) {
            assertEquals(2, entry.length);
            assertFalse(entry[0].isBlank());
            assertFalse(entry[1].isBlank());
        }
    }

    @Test
    void lookupStringsAreUniqueAndCoverCoreApis() {
        Set<String> seen = new HashSet<>();
        for (String[] entry : DpcppCompletionContributor.SYCL_COMPLETIONS) {
            assertTrue(seen.add(entry[0]), "duplicate " + entry[0]);
        }
        assertTrue(seen.contains("sycl::queue"));
        assertTrue(seen.contains("parallel_for"));
        assertTrue(seen.contains("sycl::malloc_device<"));
    }
}
