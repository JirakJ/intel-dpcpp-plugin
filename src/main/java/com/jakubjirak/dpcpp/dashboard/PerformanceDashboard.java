package com.jakubjirak.dpcpp.dashboard;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Performance dashboard for visualizing SYCL application metrics
 */
public class PerformanceDashboard {
    
    public static class PerformanceMetrics {
        public final Map<String, Double> kernelExecutionTimes;
        public final Map<String, Double> memoryBandwidth;
        public final Map<String, Double> gpuUtilization;
        public final Map<String, Long> memoryUsage;
        public final double totalExecutionTime;
        
        public PerformanceMetrics(Map<String, Double> executionTimes,
                                 Map<String, Double> bandwidth,
                                 Map<String, Double> utilization,
                                 Map<String, Long> memory,
                                 double totalTime) {
            this.kernelExecutionTimes = executionTimes;
            this.memoryBandwidth = bandwidth;
            this.gpuUtilization = utilization;
            this.memoryUsage = memory;
            this.totalExecutionTime = totalTime;
        }
    }
    
    public static class TimelineEvent {
        public final long timestampMs;
        public final String eventType;
        public final String description;
        public final double durationMs;
        
        public TimelineEvent(long timestamp, String type, String description, double duration) {
            this.timestampMs = timestamp;
            this.eventType = type;
            this.description = description;
            this.durationMs = duration;
        }
    }
    
    /**
     * Generate example performance data
     */
    public static PerformanceMetrics generateExampleMetrics() {
        Map<String, Double> executionTimes = new LinkedHashMap<>();
        executionTimes.put("vector_add", 1.23);
        executionTimes.put("matrix_multiply", 45.67);
        executionTimes.put("reduction", 3.45);
        executionTimes.put("copy", 0.89);
        
        Map<String, Double> bandwidth = new LinkedHashMap<>();
        bandwidth.put("vector_add", 234.5);
        bandwidth.put("matrix_multiply", 456.7);
        bandwidth.put("reduction", 123.4);
        bandwidth.put("copy", 567.8);
        
        Map<String, Double> utilization = new LinkedHashMap<>();
        utilization.put("vector_add", 45.0);
        utilization.put("matrix_multiply", 92.3);
        utilization.put("reduction", 67.8);
        utilization.put("copy", 23.4);
        
        Map<String, Long> memory = new LinkedHashMap<>();
        memory.put("vector_add", 1024L * 1024 * 64);  // 64 MB
        memory.put("matrix_multiply", 1024L * 1024 * 512);  // 512 MB
        memory.put("reduction", 1024L * 1024 * 32);  // 32 MB
        memory.put("copy", 1024L * 1024 * 128);  // 128 MB
        
        return new PerformanceMetrics(executionTimes, bandwidth, utilization, 
                                     memory, 51.24);
    }
    
    public static void showDashboard(@NotNull Project project) {
        PerformanceMetrics metrics = generateExampleMetrics();
        
        StringBuilder message = new StringBuilder();
        message.append("Performance Dashboard\n\n");
        
        message.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        message.append("📊 Kernel Performance Summary\n");
        message.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
        
        message.append(String.format("Total execution: %.2f ms\n\n", metrics.totalExecutionTime));
        
        message.append("⏱️ Execution Times:\n");
        for (Map.Entry<String, Double> entry : metrics.kernelExecutionTimes.entrySet()) {
            double percentage = (entry.getValue() / metrics.totalExecutionTime) * 100;
            String bar = generateProgressBar(percentage);
            message.append(String.format("  %-20s %6.2f ms %s (%.1f%%)\n",
                entry.getKey(), entry.getValue(), bar, percentage));
        }
        
        message.append("\n📈 Memory Bandwidth (GB/s):\n");
        for (Map.Entry<String, Double> entry : metrics.memoryBandwidth.entrySet()) {
            double percentage = (entry.getValue() / 600.0) * 100; // vs 600 GB/s peak
            String bar = generateProgressBar(percentage);
            message.append(String.format("  %-20s %6.1f GB/s %s (%.0f%%)\n",
                entry.getKey(), entry.getValue(), bar, percentage));
        }
        
        message.append("\n🎯 GPU Utilization (%):\n");
        for (Map.Entry<String, Double> entry : metrics.gpuUtilization.entrySet()) {
            String bar = generateProgressBar(entry.getValue());
            String status = entry.getValue() > 80 ? "✓" : 
                          entry.getValue() > 50 ? "⚠" : "⚠️";
            message.append(String.format("  %-20s %5.1f%% %s %s\n",
                entry.getKey(), entry.getValue(), bar, status));
        }
        
        message.append("\n💾 Memory Usage:\n");
        for (Map.Entry<String, Long> entry : metrics.memoryUsage.entrySet()) {
            double mb = entry.getValue() / (1024.0 * 1024.0);
            message.append(String.format("  %-20s %6.0f MB\n", entry.getKey(), mb));
        }
        
        message.append("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        message.append("🔍 Insights & Recommendations\n");
        message.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
        
        // Generate insights
        String hottestKernel = findHottestKernel(metrics.kernelExecutionTimes);
        message.append(String.format("⚠️ Hottest kernel: %s (%.1f%% of total time)\n",
            hottestKernel,
            (metrics.kernelExecutionTimes.get(hottestKernel) / metrics.totalExecutionTime) * 100));
        
        String lowUtilKernel = findLowestUtilization(metrics.gpuUtilization);
        message.append(String.format("💡 Optimization opportunity: %s (%.1f%% GPU utilization)\n",
            lowUtilKernel, metrics.gpuUtilization.get(lowUtilKernel)));
        
        message.append("\n🎯 Next Steps:\n");
        message.append("  1. Focus optimization on " + hottestKernel + "\n");
        message.append("  2. Investigate low utilization in " + lowUtilKernel + "\n");
        message.append("  3. Consider kernel fusion for small kernels\n");
        message.append("  4. Profile with VTune for detailed analysis\n\n");
        
        message.append("Available in Professional Edition");
        
        Messages.showInfoMessage(project, message.toString(), "Performance Dashboard");
    }
    
    private static String generateProgressBar(double percentage) {
        int bars = (int) (percentage / 10.0);
        bars = Math.max(0, Math.min(bars, 10));
        return "█".repeat(bars) + "░".repeat(10 - bars);
    }
    
    private static String findHottestKernel(Map<String, Double> times) {
        return times.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("unknown");
    }
    
    private static String findLowestUtilization(Map<String, Double> utilization) {
        return utilization.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("unknown");
    }
    
    /**
     * Export metrics to various formats
     */
    public static String exportMetrics(PerformanceMetrics metrics, String format) {
        switch (format.toLowerCase()) {
            case "json":
                return exportToJson(metrics);
            case "csv":
                return exportToCsv(metrics);
            case "html":
                return exportToHtml(metrics);
            default:
                return "Unsupported format";
        }
    }
    
    private static String exportToJson(PerformanceMetrics metrics) {
        return "{ \"kernels\": " + metrics.kernelExecutionTimes.toString() + " }";
    }
    
    private static String exportToCsv(PerformanceMetrics metrics) {
        StringBuilder csv = new StringBuilder();
        csv.append("Kernel,Time(ms),Bandwidth(GB/s),Utilization(%)\n");
        for (String kernel : metrics.kernelExecutionTimes.keySet()) {
            csv.append(String.format("%s,%.2f,%.1f,%.1f\n",
                kernel,
                metrics.kernelExecutionTimes.get(kernel),
                metrics.memoryBandwidth.get(kernel),
                metrics.gpuUtilization.get(kernel)
            ));
        }
        return csv.toString();
    }
    
    private static String exportToHtml(PerformanceMetrics metrics) {
        return "<html><body><h1>Performance Dashboard</h1>" +
               "<table border='1'><tr><th>Kernel</th><th>Time</th></tr>" +
               "</table></body></html>";
    }
}
