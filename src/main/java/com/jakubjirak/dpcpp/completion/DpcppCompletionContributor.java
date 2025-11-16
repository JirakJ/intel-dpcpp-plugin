package com.jakubjirak.dpcpp.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class DpcppCompletionContributor extends CompletionContributor {
    
    private static final String[][] SYCL_COMPLETIONS = {
        // Queue operations
        {"sycl::queue", "SYCL queue for submitting commands"},
        {"sycl::gpu_selector{}", "Select GPU device"},
        {"sycl::cpu_selector{}", "Select CPU device"},
        {"sycl::default_selector{}", "Select default device"},
        {".submit", "Submit command group to queue"},
        {".wait()", "Wait for queue operations to complete"},
        {".wait_and_throw()", "Wait and throw exceptions"},
        
        // Buffer and accessor
        {"sycl::buffer<", "SYCL buffer for data management"},
        {".get_access<", "Get accessor from buffer"},
        {"sycl::access::mode::read", "Read-only access"},
        {"sycl::access::mode::write", "Write-only access"},
        {"sycl::access::mode::read_write", "Read-write access"},
        
        // Parallel constructs
        {"parallel_for", "Execute kernel in parallel"},
        {"single_task", "Execute single kernel instance"},
        {"sycl::range<1>", "1D range for iteration space"},
        {"sycl::range<2>", "2D range for iteration space"},
        {"sycl::range<3>", "3D range for iteration space"},
        {"sycl::nd_range<", "N-dimensional range with work-groups"},
        
        // Handler operations
        {"sycl::handler", "Command group handler"},
        
        // USM allocations
        {"sycl::malloc_device<", "Allocate device memory"},
        {"sycl::malloc_host<", "Allocate host memory"},
        {"sycl::malloc_shared<", "Allocate shared memory"},
        {"sycl::free", "Free USM allocation"},
        
        // Device info
        {"get_info<sycl::info::device::name>()", "Get device name"},
        {"get_info<sycl::info::device::vendor>()", "Get device vendor"},
        {"get_info<sycl::info::device::max_compute_units>()", "Get max compute units"},
    };

    public DpcppCompletionContributor() {
        extend(CompletionType.BASIC, 
            PlatformPatterns.psiElement(),
            new CompletionProvider<CompletionParameters>() {
                @Override
                protected void addCompletions(@NotNull CompletionParameters parameters,
                                            @NotNull ProcessingContext context,
                                            @NotNull CompletionResultSet result) {
                    for (String[] completion : SYCL_COMPLETIONS) {
                        result.addElement(
                            LookupElementBuilder.create(completion[0])
                                .withTypeText("DPC++/SYCL")
                                .withTailText(" - " + completion[1], true)
                                .bold()
                        );
                    }
                }
            }
        );
    }
}
