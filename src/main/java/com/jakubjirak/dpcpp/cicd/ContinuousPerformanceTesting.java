package com.jakubjirak.dpcpp.cicd;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Continuous Performance Testing for CI/CD
 * Automatically runs benchmarks and detects regressions in CI pipeline.
 */
public class ContinuousPerformanceTesting {
    private static final Logger LOG = Logger.getInstance(ContinuousPerformanceTesting.class);
    
    private final Project project;
    private final Map<String, Double> performanceBaselines;
    private static final double REGRESSION_THRESHOLD = 0.05; // 5%
    
    public ContinuousPerformanceTesting(@NotNull Project project) {
        this.project = project;
        this.performanceBaselines = new HashMap<>();
    }
    
    public TestResult runBenchmarks() {
        LOG.info("Running performance benchmarks");
        
        TestResult result = new TestResult();
        
        // Simulate benchmark execution
        result.addKernel("vector_add", 1.23, 1.20);
        result.addKernel("matrix_multiply", 45.67, 48.12);
        result.addKernel("reduction", 3.45, 3.42);
        
        return result;
    }
    
    public boolean detectRegression(TestResult result) {
        for (KernelResult kernel : result.kernelResults) {
            Double baseline = performanceBaselines.get(kernel.name);
            if (baseline != null) {
                double change = (kernel.currentTime - baseline) / baseline;
                if (change > REGRESSION_THRESHOLD) {
                    LOG.warn(String.format("Regression detected in %s: %.1f%% slower", 
                        kernel.name, change * 100));
                    return true;
                }
            } else {
                performanceBaselines.put(kernel.name, kernel.currentTime);
            }
        }
        return false;
    }
    
    public String generateCIReport(TestResult result) {
        StringBuilder report = new StringBuilder();
        report.append("Performance Test Report\n");
        report.append("======================\n\n");
        
        for (KernelResult kernel : result.kernelResults) {
            report.append(String.format("%s:\n", kernel.name));
            report.append(String.format("  Current: %.3f ms\n", kernel.currentTime));
            report.append(String.format("  Baseline: %.3f ms\n", kernel.baselineTime));
            
            double change = (kernel.currentTime - kernel.baselineTime) / kernel.baselineTime * 100;
            String status = change > REGRESSION_THRESHOLD * 100 ? "❌ REGRESSION" : "✅ PASS";
            report.append(String.format("  Change: %.1f%% %s\n\n", change, status));
        }
        
        return report.toString();
    }
    
    public static class TestResult {
        public final List<KernelResult> kernelResults = new ArrayList<>();
        
        public void addKernel(String name, double currentTime, double baselineTime) {
            kernelResults.add(new KernelResult(name, currentTime, baselineTime));
        }
    }
    
    public static class KernelResult {
        public final String name;
        public final double currentTime;
        public final double baselineTime;
        
        public KernelResult(String name, double currentTime, double baselineTime) {
            this.name = name;
            this.currentTime = currentTime;
            this.baselineTime = baselineTime;
        }
    }
}
