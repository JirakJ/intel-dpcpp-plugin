package com.jakubjirak.dpcpp.refactoring;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * Refactoring to convert basic parallel_for to nd_range with optimized work-groups
 * 
 * Example transformation:
 * 
 * FROM (basic range):
 * q.parallel_for(range<1>(N), [=](id<1> i) {
 *   c[i] = a[i] + b[i];
 * });
 * 
 * TO (nd_range with work-groups):
 * const size_t work_group_size = 256;
 * q.parallel_for(
 *   nd_range<1>(range<1>(N), range<1>(work_group_size)),
 *   [=](nd_item<1> item) {
 *     size_t i = item.get_global_id(0);
 *     if (i < N) {
 *       c[i] = a[i] + b[i];
 *     }
 *   }
 * );
 */
public class OptimizeWorkGroupRefactoring {
    
    public static class WorkGroupConfig {
        public final int dimension;
        public final int suggestedWorkGroupSize;
        public final String rationale;
        
        public WorkGroupConfig(int dimension, int suggestedSize, String rationale) {
            this.dimension = dimension;
            this.suggestedWorkGroupSize = suggestedSize;
            this.rationale = rationale;
        }
    }
    
    public static WorkGroupConfig analyzeAndSuggest(int dimension, int problemSize) {
        // Intel GPU optimization heuristics
        int suggestedSize;
        String rationale;
        
        if (dimension == 1) {
            if (problemSize < 1024) {
                suggestedSize = 128;
                rationale = "Small problem - use 128 for low overhead";
            } else if (problemSize < 100000) {
                suggestedSize = 256;
                rationale = "Medium problem - use 256 for good balance";
            } else {
                suggestedSize = 512;
                rationale = "Large problem - use 512 for maximum occupancy";
            }
        } else if (dimension == 2) {
            suggestedSize = 16; // 16x16 = 256 threads
            rationale = "2D problem - use 16x16 tiles (256 threads total)";
        } else {
            suggestedSize = 8; // 8x8x8 = 512 threads
            rationale = "3D problem - use 8x8x8 blocks (512 threads total)";
        }
        
        return new WorkGroupConfig(dimension, suggestedSize, rationale);
    }
    
    public static String generateOptimizedCode(@NotNull WorkGroupConfig config, 
                                              String originalKernelBody) {
        StringBuilder code = new StringBuilder();
        
        if (config.dimension == 1) {
            code.append(String.format("const size_t work_group_size = %d;\n", 
                                     config.suggestedWorkGroupSize));
            code.append("q.parallel_for(\n");
            code.append("  nd_range<1>(range<1>(N), range<1>(work_group_size)),\n");
            code.append("  [=](nd_item<1> item) {\n");
            code.append("    size_t i = item.get_global_id(0);\n");
            code.append("    if (i < N) {\n");
            code.append("      ").append(originalKernelBody).append("\n");
            code.append("    }\n");
            code.append("  }\n");
            code.append(");\n");
        } else if (config.dimension == 2) {
            code.append(String.format("const size_t tile_size = %d;\n", 
                                     config.suggestedWorkGroupSize));
            code.append("q.parallel_for(\n");
            code.append("  nd_range<2>(\n");
            code.append("    range<2>(M, N),\n");
            code.append("    range<2>(tile_size, tile_size)\n");
            code.append("  ),\n");
            code.append("  [=](nd_item<2> item) {\n");
            code.append("    size_t i = item.get_global_id(0);\n");
            code.append("    size_t j = item.get_global_id(1);\n");
            code.append("    if (i < M && j < N) {\n");
            code.append("      ").append(originalKernelBody).append("\n");
            code.append("    }\n");
            code.append("  }\n");
            code.append(");\n");
        }
        
        return code.toString();
    }
    
    public static void showRefactoringPreview(@NotNull Project project,
                                             @NotNull WorkGroupConfig config,
                                             String originalBody) {
        String optimizedCode = generateOptimizedCode(config, originalBody);
        
        StringBuilder message = new StringBuilder();
        message.append("Optimize Work-Group Size Refactoring\n\n");
        message.append(String.format("Rationale: %s\n\n", config.rationale));
        message.append("Optimized Code:\n");
        message.append("```cpp\n");
        message.append(optimizedCode);
        message.append("```\n\n");
        message.append("Expected Benefits:\n");
        message.append("  • Better GPU occupancy\n");
        message.append("  • Improved memory coalescing\n");
        message.append("  • Reduced kernel launch overhead\n");
        message.append(String.format("  • Estimated speedup: 1.5-3x\n\n"));
        message.append("Note: Actual speedup depends on memory access patterns\n");
        message.append("and computational intensity.\n\n");
        message.append("Available in Professional Edition");
        
        Messages.showInfoMessage(project, message.toString(), "Optimize Work-Group Size");
    }
}
