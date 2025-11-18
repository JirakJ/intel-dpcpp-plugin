package com.jakubjirak.dpcpp.collaboration;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Team Performance Tracker
 * Tracks and shares performance metrics across team members.
 */
public class TeamPerformanceTracker {
    private static final Logger LOG = Logger.getInstance(TeamPerformanceTracker.class);
    
    private final Project project;
    private final Map<String, KernelPerformance> baselineMetrics;
    private final Map<String, List<PerformanceEntry>> history;
    
    public TeamPerformanceTracker(@NotNull Project project) {
        this.project = project;
        this.baselineMetrics = new ConcurrentHashMap<>();
        this.history = new ConcurrentHashMap<>();
    }
    
    public void recordPerformance(String kernelName, String developer, 
                                  double executionTimeMs, String commitHash) {
        PerformanceEntry entry = new PerformanceEntry(
            kernelName, developer, executionTimeMs, LocalDateTime.now(), commitHash
        );
        history.computeIfAbsent(kernelName, k -> new ArrayList<>()).add(entry);
        
        KernelPerformance baseline = baselineMetrics.get(kernelName);
        if (baseline != null) {
            double slowdown = (executionTimeMs - baseline.executionTimeMs) / baseline.executionTimeMs;
            if (slowdown > 0.05) {
                notifyRegression(kernelName, developer, slowdown * 100);
            }
        } else {
            baselineMetrics.put(kernelName, 
                new KernelPerformance(kernelName, executionTimeMs, developer, commitHash));
        }
    }
    
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("Team Performance Report\n");
        report.append("=======================\n\n");
        
        for (Map.Entry<String, KernelPerformance> entry : baselineMetrics.entrySet()) {
            KernelPerformance perf = entry.getValue();
            report.append(String.format("%s: %.3f ms (by %s)\n", 
                entry.getKey(), perf.executionTimeMs, perf.developer));
        }
        
        return report.toString();
    }
    
    private void notifyRegression(String kernelName, String developer, double percentSlower) {
        LOG.warn(String.format("Performance regression in %s by %s: %.1f%% slower",
                              kernelName, developer, percentSlower));
    }
    
    public static class KernelPerformance {
        public final String kernelName;
        public final double executionTimeMs;
        public final String developer;
        public final String commitHash;
        
        public KernelPerformance(String kernelName, double executionTimeMs, 
                                String developer, String commitHash) {
            this.kernelName = kernelName;
            this.executionTimeMs = executionTimeMs;
            this.developer = developer;
            this.commitHash = commitHash;
        }
    }
    
    public static class PerformanceEntry {
        public final String kernelName;
        public final String developer;
        public final double executionTimeMs;
        public final LocalDateTime timestamp;
        public final String commitHash;
        
        public PerformanceEntry(String kernelName, String developer, 
                               double executionTimeMs, LocalDateTime timestamp, 
                               String commitHash) {
            this.kernelName = kernelName;
            this.developer = developer;
            this.executionTimeMs = executionTimeMs;
            this.timestamp = timestamp;
            this.commitHash = commitHash;
        }
    }
}
