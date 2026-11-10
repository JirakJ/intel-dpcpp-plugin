package com.jakubjirak.dpcpp.refactoring;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * Refactoring to extract SYCL kernel lambda into a separate functor class
 * 
 * Example transformation:
 * 
 * FROM (inline lambda):
 * q.parallel_for(range<1>(N), [=](id<1> i) {
 *   c[i] = a[i] + b[i];
 * });
 * 
 * TO (functor class):
 * class VectorAddKernel {
 *   accessor<int> a, b, c;
 * public:
 *   VectorAddKernel(accessor<int> a, accessor<int> b, accessor<int> c)
 *     : a(a), b(b), c(c) {}
 *   
 *   void operator()(id<1> i) const {
 *     c[i] = a[i] + b[i];
 *   }
 * };
 * 
 * q.parallel_for(range<1>(N), VectorAddKernel{acc_a, acc_b, acc_c});
 */
public class ExtractKernelRefactoring {
    
    public static class KernelInfo {
        public final String kernelName;
        public final String[] parameters;
        public final String[] parameterTypes;
        public final String kernelBody;
        
        public KernelInfo(String kernelName, String[] parameters, 
                         String[] parameterTypes, String kernelBody) {
            this.kernelName = kernelName;
            this.parameters = parameters;
            this.parameterTypes = parameterTypes;
            this.kernelBody = kernelBody;
        }
    }
    
    public static String generateKernelClass(@NotNull KernelInfo info) {
        StringBuilder code = new StringBuilder();
        
        // Class definition
        code.append(String.format("class %s {\n", info.kernelName));
        
        // Private members
        for (int i = 0; i < info.parameters.length; i++) {
            code.append(String.format("  %s %s;\n", info.parameterTypes[i], info.parameters[i]));
        }
        
        code.append("\npublic:\n");
        
        // Constructor
        code.append(String.format("  %s(", info.kernelName));
        for (int i = 0; i < info.parameters.length; i++) {
            if (i > 0) code.append(", ");
            code.append(String.format("%s %s", info.parameterTypes[i], info.parameters[i]));
        }
        code.append(")\n");
        code.append("    : ");
        for (int i = 0; i < info.parameters.length; i++) {
            if (i > 0) code.append(", ");
            code.append(String.format("%s(%s)", info.parameters[i], info.parameters[i]));
        }
        code.append(" {}\n\n");
        
        // Operator()
        code.append("  void operator()(id<1> idx) const {\n");
        code.append("    ").append(info.kernelBody).append("\n");
        code.append("  }\n");
        code.append("};\n");
        
        return code.toString();
    }
    
    public static void showRefactoringPreview(@NotNull Project project,
                                             @NotNull KernelInfo info) {
        String kernelClass = generateKernelClass(info);
        
        StringBuilder message = new StringBuilder();
        message.append("Extract SYCL Kernel Refactoring\n\n");
        message.append("This refactoring extracts inline kernel lambda into a functor class.\n\n");
        message.append("Generated Code:\n");
        message.append("```cpp\n");
        message.append(kernelClass);
        message.append("\n// Usage:\n");
        message.append(String.format("q.parallel_for(range<1>(N), %s{", info.kernelName));
        for (int i = 0; i < info.parameters.length; i++) {
            if (i > 0) message.append(", ");
            message.append(info.parameters[i]);
        }
        message.append("});\n");
        message.append("```\n\n");
        message.append("Benefits:\n");
        message.append("  • Reusable kernel code\n");
        message.append("  • Easier to test in isolation\n");
        message.append("  • Better for complex kernels\n");
        message.append("  • Improved readability\n\n");
        
        Messages.showInfoMessage(project, message.toString(), "Extract Kernel Refactoring");
    }
}
