/*
 * Copyright (c) 2026 Jakub Jirák.
 * Licensed under the Apache License, Version 2.0. See LICENSE for details.
 */
package com.jakubjirak.dpcpp.feedback;

import com.intellij.ide.BrowserUtil;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Non-intrusive feedback prompt shown once after 7 days of plugin usage.
 */
public class FeedbackNotifier implements ProjectActivity {

    /** Must match the {@code notificationGroup} id registered in plugin.xml. */
    public static final String NOTIFICATION_GROUP_ID = "Intel DPC++ Support Feedback";
    static final long DAYS_BEFORE_PROMPT = 7;

    /**
     * Decides whether the one-off feedback balloon should be shown at {@code now}.
     * The first call only records the activation time.
     */
    static boolean shouldShowFeedback(@NotNull FeedbackState.State state, long now) {
        if (state.firstActivation == 0L) {
            state.firstActivation = now;
            return false;
        }
        if (state.feedbackShown || state.dismissed) {
            return false;
        }
        return java.util.concurrent.TimeUnit.MILLISECONDS.toDays(now - state.firstActivation) >= DAYS_BEFORE_PROMPT;
    }

    @Nullable
    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        FeedbackState feedbackState = FeedbackState.getInstance();
        FeedbackState.State state = feedbackState.getState();
        if (!shouldShowFeedback(state, System.currentTimeMillis())) return Unit.INSTANCE;
        state.feedbackShown = true;

        var notification = NotificationGroupManager.getInstance()
            .getNotificationGroup(NOTIFICATION_GROUP_ID)
            .createNotification(
                "Intel DPC++ Support",
                "You've been using this plugin for over a week — thank you! \uD83C\uDF89\nWe'd love your feedback to help us improve.",
                NotificationType.INFORMATION
            )
            .addAction(NotificationAction.createSimpleExpiring("\uD83D\uDCE7 Send Feedback", () ->
                BrowserUtil.browse("mailto:feedback@jakubjirak.com?subject=Feedback: Intel%20DPC++%20Support")
            ))
            .addAction(NotificationAction.createSimpleExpiring("\u2B50 Rate on Marketplace", () ->
                BrowserUtil.browse("https://plugins.jetbrains.com/plugin/29045/reviews")
            ))
            .addAction(NotificationAction.createSimpleExpiring("Later", () ->
                state.feedbackShown = false
            ))
            .addAction(NotificationAction.createSimpleExpiring("Don't show again", () ->
                state.dismissed = true
            ));

        notification.notify(project);
        return Unit.INSTANCE;
    }
}
