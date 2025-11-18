package com.jakubjirak.dpcpp.multidevice;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Multi-device orchestration and workload distribution for SYCL applications
 */
public class MultiDeviceOrchestrator {
    
    public static class DeviceInfo {
        public final String name;
        public final String platform;
        public final int computeUnits;
        public final long globalMemory;
        public final int maxClockMhz;
        public final double performanceScore;
        
        public DeviceInfo(String name, String platform, int computeUnits,
                         long globalMemory, int maxClockMhz) {
            this.name = name;
            this.platform = platform;
            this.computeUnits = computeUnits;
            this.globalMemory = globalMemory;
            this.maxClockMhz = maxClockMhz;
            this.performanceScore = calculatePerformanceScore();
        }
        
        private double calculatePerformanceScore() {
            // Simplified performance score calculation
            return (computeUnits * maxClockMhz * globalMemory) / 1e12;
        }
    }
    
    public static class WorkloadDistribution {
        public final Map<String, Double> deviceWorkloads;
        public final String strategy;
        public final double estimatedSpeedup;
        
        public WorkloadDistribution(Map<String, Double> workloads, 
                                   String strategy, double speedup) {
            this.deviceWorkloads = workloads;
            this.strategy = strategy;
            this.estimatedSpeedup = speedup;
        }
    }
    
    /**
     * Calculate optimal workload distribution across devices
     */
    public static WorkloadDistribution calculateDistribution(
            @NotNull List<DeviceInfo> devices, long totalWorkload) {
        
        if (devices.isEmpty()) {
            throw new IllegalArgumentException("No devices available");
        }
        
        // Calculate total performance
        double totalPerformance = devices.stream()
            .mapToDouble(d -> d.performanceScore)
            .sum();
        
        // Distribute workload proportionally
        Map<String, Double> distribution = new LinkedHashMap<>();
        for (DeviceInfo device : devices) {
            double proportion = device.performanceScore / totalPerformance;
            double deviceWorkload = totalWorkload * proportion;
            distribution.put(device.name, deviceWorkload);
        }
        
        // Estimate speedup (simplified)
        double speedup = devices.size() * 0.85; // 85% efficiency
        
        String strategy = determineStrategy(devices);
        
        return new WorkloadDistribution(distribution, strategy, speedup);
    }
    
    private static String determineStrategy(List<DeviceInfo> devices) {
        boolean hasGPU = devices.stream().anyMatch(d -> d.platform.contains("GPU"));
        boolean hasCPU = devices.stream().anyMatch(d -> d.platform.contains("CPU"));
        boolean hasFPGA = devices.stream().anyMatch(d -> d.platform.contains("FPGA"));
        
        if (hasGPU && hasCPU && hasFPGA) {
            return "Heterogeneous (GPU+CPU+FPGA)";
        } else if (hasGPU && hasCPU) {
            return "Heterogeneous (GPU+CPU)";
        } else if (devices.size() > 1 && hasGPU) {
            return "Multi-GPU";
        } else if (devices.size() > 1) {
            return "Multi-device homogeneous";
        } else {
            return "Single device";
        }
    }
    
    public static void showOrchestrationReport(@NotNull Project project,
                                              @NotNull List<DeviceInfo> devices,
                                              long totalWorkload) {
        WorkloadDistribution distribution = calculateDistribution(devices, totalWorkload);
        
        StringBuilder message = new StringBuilder();
        message.append("Multi-Device Orchestration\n\n");
        
        message.append("🖥️ Available Devices:\n");
        for (DeviceInfo device : devices) {
            message.append(String.format("  %s (%s)\n", device.name, device.platform));
            message.append(String.format("    Compute Units: %d\n", device.computeUnits));
            message.append(String.format("    Memory: %d GB\n", device.globalMemory / (1024*1024*1024)));
            message.append(String.format("    Performance Score: %.2f\n", device.performanceScore));
        }
        
        message.append("\n📊 Workload Distribution:\n");
        message.append(String.format("  Strategy: %s\n", distribution.strategy));
        message.append(String.format("  Total workload: %,d items\n\n", totalWorkload));
        
        for (Map.Entry<String, Double> entry : distribution.deviceWorkloads.entrySet()) {
            double percentage = (entry.getValue() / totalWorkload) * 100;
            message.append(String.format("  %s: %,d items (%.1f%%)\n", 
                entry.getKey(), entry.getValue().longValue(), percentage));
        }
        
        message.append(String.format("\n⚡ Estimated Speedup: %.2fx\n\n", 
            distribution.estimatedSpeedup));
        
        message.append("💡 Orchestration Patterns:\n");
        message.append("  • Data parallelism: Split data across devices\n");
        message.append("  • Task parallelism: Different tasks on different devices\n");
        message.append("  • Pipeline: Chain operations across devices\n");
        message.append("  • Load balancing: Dynamic work distribution\n\n");
        
        message.append("🔧 Implementation:\n");
        message.append("```cpp\n");
        message.append("// Create queues for each device\n");
        message.append("std::vector<sycl::queue> queues;\n");
        message.append("for (auto& dev : devices) {\n");
        message.append("  queues.emplace_back(dev);\n");
        message.append("}\n\n");
        message.append("// Submit work to multiple devices\n");
        message.append("for (size_t i = 0; i < queues.size(); i++) {\n");
        message.append("  queues[i].submit([&](handler& h) {\n");
        message.append("    // Process chunk for device i\n");
        message.append("  });\n");
        message.append("}\n");
        message.append("```\n\n");
        
        message.append("Available in Professional Edition");
        
        Messages.showInfoMessage(project, message.toString(), "Multi-Device Orchestrator");
    }
    
    /**
     * Generate code for multi-device execution
     */
    public static String generateMultiDeviceCode(List<DeviceInfo> devices) {
        StringBuilder code = new StringBuilder();
        
        code.append("// Multi-device orchestration\n");
        code.append("auto devices = sycl::device::get_devices();\n");
        code.append("std::vector<sycl::queue> queues;\n\n");
        
        code.append("// Create queue for each device\n");
        code.append("for (auto& dev : devices) {\n");
        code.append("  queues.emplace_back(dev);\n");
        code.append("}\n\n");
        
        code.append("// Distribute workload\n");
        code.append("size_t chunk_size = N / queues.size();\n");
        code.append("for (size_t i = 0; i < queues.size(); i++) {\n");
        code.append("  size_t start = i * chunk_size;\n");
        code.append("  size_t end = (i == queues.size() - 1) ? N : start + chunk_size;\n\n");
        
        code.append("  queues[i].submit([&](sycl::handler& h) {\n");
        code.append("    h.parallel_for(sycl::range<1>(end - start), [=](sycl::id<1> idx) {\n");
        code.append("      // Process data[start + idx]\n");
        code.append("    });\n");
        code.append("  });\n");
        code.append("}\n\n");
        
        code.append("// Wait for all devices\n");
        code.append("for (auto& q : queues) q.wait();\n");
        
        return code.toString();
    }
}
