/*
 * Copyright (c) 2026 Jakub Jirák. All rights reserved.
 * Licensed under the terms of the JetBrains Marketplace EULA.
 */
package com.jakubjirak.dpcpp.feedback;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;

@Service(Service.Level.APP)
@State(name = "com.jakubjirak.dpcpp.FeedbackState", storages = @Storage("com-jakubjirak-dpcpp-feedback.xml"))
public final class FeedbackState implements PersistentStateComponent<FeedbackState.State> {
    public static class State {
        public long firstActivation = 0L;
        public boolean feedbackShown = false;
        public boolean dismissed = false;
    }

    private State myState = new State();

    @Override
    public State getState() { return myState; }

    @Override
    public void loadState(State state) { myState = state; }

    public static FeedbackState getInstance() {
        return ApplicationManager.getApplication().getService(FeedbackState.class);
    }
}
