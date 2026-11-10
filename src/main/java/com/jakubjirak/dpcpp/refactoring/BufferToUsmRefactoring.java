package com.jakubjirak.dpcpp.refactoring;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * Refactoring to convert SYCL buffer/accessor code to USM (Unified Shared Memory)
 * 
 * Example transformation:
 * 
 * FROM (buffer/accessor):
 * {
 *   buffer<int> buf(data, range<1>(N));
 *   q.submit([&](handler& h) {
 *     accessor acc(buf, h);
 *     h.parallel_for(range<1>(N), [=](id<1> i) {
 *       acc[i] = i;
 *     });
 *   });
 * }
 * 
 * TO (USM):
 * int* data_usm = malloc_device<int>(N, q);
 * q.memcpy(data_usm, data, N * sizeof(int)).wait();
 * q.parallel_for(range<1>(N), [=](id<1> i) {
 *   data_usm[i] = i;
 * }).wait();
 * q.memcpy(data, data_usm, N * sizeof(int)).wait();
 * free(data_usm, q);
 */
public class BufferToUsmRefactoring {
    
    public static class RefactoringContext {
        public final String bufferVariable;
        public final String dataType;
        public final String size;
        public final String originalData;
        
        public RefactoringContext(String bufferVariable, String dataType, 
                                 String size, String originalData) {
            this.bufferVariable = bufferVariable;
            this.dataType = dataType;
            this.size = size;
            this.originalData = originalData;
        }
    }
    
    public static String generateUsmCode(@NotNull RefactoringContext context) {
        StringBuilder code = new StringBuilder();
        
        // Allocate USM memory
        code.append(String.format("// USM allocation\n"));
        code.append(String.format("%s* %s_usm = malloc_device<%s>(%s, q);\n",
            context.dataType, context.bufferVariable, context.dataType, context.size));
        
        // Copy host to device
        code.append(String.format("q.memcpy(%s_usm, %s, %s * sizeof(%s)).wait();\n\n",
            context.bufferVariable, context.originalData, context.size, context.dataType));
        
        // Use in kernel (simplified - actual refactoring would preserve kernel code)
        code.append("// Use data_usm directly in kernel\n");
        code.append("q.parallel_for(range<1>(N), [=](id<1> i) {\n");
        code.append(String.format("  %s_usm[i] = /* computation */;\n", context.bufferVariable));
        code.append("}).wait();\n\n");
        
        // Copy device to host
        code.append(String.format("q.memcpy(%s, %s_usm, %s * sizeof(%s)).wait();\n",
            context.originalData, context.bufferVariable, context.size, context.dataType));
        
        // Free USM memory
        code.append(String.format("free(%s_usm, q);\n", context.bufferVariable));
        
        return code.toString();
    }
    
    public static void showRefactoringPreview(@NotNull Project project,
                                             @NotNull RefactoringContext context) {
        String usmCode = generateUsmCode(context);
        
        StringBuilder message = new StringBuilder();
        message.append("Buffer to USM Refactoring\n\n");
        message.append("This refactoring converts SYCL buffer/accessor code to USM.\n\n");
        message.append("Generated USM Code:\n");
        message.append("```cpp\n");
        message.append(usmCode);
        message.append("```\n\n");
        message.append("Benefits:\n");
        message.append("  • Simpler code (no accessors)\n");
        message.append("  • Explicit memory management\n");
        message.append("  • Better for pointer-based algorithms\n");
        message.append("  • Easier to debug\n\n");
        message.append("Trade-offs:\n");
        message.append("  • Manual memory management required\n");
        message.append("  • Explicit synchronization needed\n");
        message.append("  • No automatic dependency tracking\n\n");
        
        Messages.showInfoMessage(project, message.toString(), "Buffer to USM Refactoring");
    }
}
