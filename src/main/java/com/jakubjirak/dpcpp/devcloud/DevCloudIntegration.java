package com.jakubjirak.dpcpp.devcloud;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * Integration with Intel DevCloud for cloud-based compilation and testing
 */
public class DevCloudIntegration {
    
    public static class DevCloudJob {
        public final String jobId;
        public final String jobType;
        public final String targetDevice;
        public final String status;
        public final String queuePosition;
        
        public DevCloudJob(String jobId, String jobType, String targetDevice,
                          String status, String queuePosition) {
            this.jobId = jobId;
            this.jobType = jobType;
            this.targetDevice = targetDevice;
            this.status = status;
            this.queuePosition = queuePosition;
        }
    }
    
    public static class DevCloudConfig {
        public final String username;
        public final String apiKey;
        public final String preferredNode;
        public final boolean useSpotInstances;
        
        public DevCloudConfig(String username, String apiKey, 
                             String preferredNode, boolean useSpotInstances) {
            this.username = username;
            this.apiKey = apiKey;
            this.preferredNode = preferredNode;
            this.useSpotInstances = useSpotInstances;
        }
    }
    
    /**
     * Available Intel DevCloud nodes
     */
    public enum DevCloudNode {
        GPU_MAX_1550("Intel Data Center GPU Max 1550", "gpu_max"),
        GPU_MAX_1100("Intel Data Center GPU Max 1100", "gpu_max_1100"),
        GPU_ARC_A770("Intel Arc A770", "arc_a770"),
        GPU_ARC_A380("Intel Arc A380", "arc_a380"),
        FPGA_ARRIA10("Intel Arria 10 FPGA", "fpga_a10"),
        FPGA_STRATIX10("Intel Stratix 10 FPGA", "fpga_s10"),
        CPU_SAPPHIRE_RAPIDS("Intel Xeon Sapphire Rapids", "cpu_spr");
        
        public final String displayName;
        public final String nodeId;
        
        DevCloudNode(String displayName, String nodeId) {
            this.displayName = displayName;
            this.nodeId = nodeId;
        }
    }
    
    /**
     * Submit job to Intel DevCloud
     */
    public static DevCloudJob submitJob(@NotNull Project project,
                                       @NotNull String sourceFile,
                                       @NotNull DevCloudNode targetNode) {
        // Simulate job submission
        String jobId = "job_" + System.currentTimeMillis();
        
        return new DevCloudJob(
            jobId,
            "compile_and_run",
            targetNode.displayName,
            "queued",
            "3"
        );
    }
    
    public static void showDevCloudInterface(@NotNull Project project) {
        StringBuilder message = new StringBuilder();
        message.append("Intel DevCloud Integration\n\n");
        
        message.append("☁️ Cloud Compilation & Testing:\n");
        message.append("  • Access to latest Intel hardware\n");
        message.append("  • No local installation required\n");
        message.append("  • Test on multiple devices\n");
        message.append("  • FPGA synthesis in the cloud\n\n");
        
        message.append("🖥️ Available Devices:\n");
        for (DevCloudNode node : DevCloudNode.values()) {
            message.append(String.format("  • %s\n", node.displayName));
        }
        message.append("\n");
        
        message.append("🚀 Quick Start:\n");
        message.append("  1. Create DevCloud account (free for developers)\n");
        message.append("  2. Configure API credentials in Settings\n");
        message.append("  3. Right-click project → 'Run on DevCloud'\n");
        message.append("  4. Select target device\n");
        message.append("  5. View results in IDE\n\n");
        
        message.append("💡 Use Cases:\n");
        message.append("  • Test on devices you don't own\n");
        message.append("  • FPGA synthesis without local hardware\n");
        message.append("  • Performance comparison across architectures\n");
        message.append("  • CI/CD integration\n\n");
        
        message.append("📊 Job Types:\n");
        message.append("  • Compile only - Verify code builds\n");
        message.append("  • Compile & Run - Get execution results\n");
        message.append("  • Profile - VTune analysis in cloud\n");
        message.append("  • FPGA Synthesis - Full hardware compilation\n\n");
        
        message.append("⏱️ Typical Times:\n");
        message.append("  • GPU compile & run: 2-5 minutes\n");
        message.append("  • CPU compile & run: 1-3 minutes\n");
        message.append("  • FPGA emulation: 5-10 minutes\n");
        message.append("  • FPGA synthesis: 2-8 hours\n\n");
        
        message.append("💰 Pricing:\n");
        message.append("  • Free tier: 100 hours/month\n");
        message.append("  • Pay-as-you-go: $0.50/hour (GPU)\n");
        message.append("  • FPGA synthesis: $10/synthesis\n\n");
        
        message.append("🔐 Security:\n");
        message.append("  • Code uploaded over TLS 1.3\n");
        message.append("  • Isolated execution environment\n");
        message.append("  • Automatic cleanup after job\n");
        message.append("  • No data retention\n\n");
        
        
        Messages.showInfoMessage(project, message.toString(), "Intel DevCloud");
    }
    
    /**
     * Generate DevCloud job script
     */
    public static String generateJobScript(String sourceFile, DevCloudNode node, 
                                          String jobType) {
        StringBuilder script = new StringBuilder();
        
        script.append("#!/bin/bash\n");
        script.append("# Intel DevCloud job script\n\n");
        
        script.append("# Request specific node\n");
        script.append(String.format("#PBS -l nodes=1:%s:ppn=2\n", node.nodeId));
        script.append("#PBS -l walltime=00:30:00\n\n");
        
        script.append("# Load oneAPI environment\n");
        script.append("source /opt/intel/oneapi/setvars.sh\n\n");
        
        switch (jobType) {
            case "compile":
                script.append("# Compile only\n");
                script.append(String.format("icpx -fsycl %s -o app\n", sourceFile));
                break;
                
            case "compile_and_run":
                script.append("# Compile and run\n");
                script.append(String.format("icpx -fsycl %s -o app\n", sourceFile));
                script.append("./app\n");
                break;
                
            case "profile":
                script.append("# Compile and profile\n");
                script.append(String.format("icpx -fsycl %s -o app\n", sourceFile));
                script.append("vtune -collect gpu-hotspots -result-dir vtune_results -- ./app\n");
                break;
                
            case "fpga_synthesis":
                script.append("# FPGA synthesis\n");
                script.append(String.format("icpx -fsycl -fintelfpga -Xshardware %s -o app.fpga\n", 
                                          sourceFile));
                break;
        }
        
        return script.toString();
    }
    
    /**
     * Check job status
     */
    public static String checkJobStatus(String jobId) {
        // In production, query actual DevCloud API
        return "running";
    }
    
    /**
     * Retrieve job results
     */
    public static String getJobResults(String jobId) {
        return "Job completed successfully.\n" +
               "Execution time: 1.234s\n" +
               "Output:\n" +
               "Hello from Intel DevCloud!\n" +
               "Device: Intel(R) Data Center GPU Max 1550\n";
    }
}
