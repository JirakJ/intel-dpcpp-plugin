/*
 * Copyright (c) 2026 Jakub Jirák.
 * Licensed under the Apache License, Version 2.0. See LICENSE for details.
 */
package com.jakubjirak.dpcpp.feedback;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeedbackNotifierTest {

    private static final long DAY = TimeUnit.DAYS.toMillis(1);
    private static final long START = 1_700_000_000_000L;

    private static FeedbackState.State state(long first, boolean shown, boolean dismissed) {
        FeedbackState.State s = new FeedbackState.State();
        s.firstActivation = first;
        s.feedbackShown = shown;
        s.dismissed = dismissed;
        return s;
    }

    @Test
    void firstRunOnlyRecordsActivation() {
        FeedbackState.State s = state(0, false, false);
        assertFalse(FeedbackNotifier.shouldShowFeedback(s, START));
        assertEquals(START, s.firstActivation);
    }

    @Test
    void promptAfterSevenDays() {
        FeedbackState.State s = state(START, false, false);
        assertFalse(FeedbackNotifier.shouldShowFeedback(s, START + 7 * DAY - 1));
        assertTrue(FeedbackNotifier.shouldShowFeedback(s, START + 7 * DAY));
    }

    @Test
    void neverAfterShownOrDismissed() {
        assertFalse(FeedbackNotifier.shouldShowFeedback(state(START, true, false), START + 30 * DAY));
        assertFalse(FeedbackNotifier.shouldShowFeedback(state(START, false, true), START + 30 * DAY));
    }

    @Test
    void stateComponentRoundTrips() {
        FeedbackState component = new FeedbackState();
        FeedbackState.State loaded = state(5, true, true);
        component.loadState(loaded);
        assertSame(loaded, component.getState());
    }
}
