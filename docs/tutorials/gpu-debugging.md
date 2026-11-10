# GPU Debugging with gdb-oneapi

## Introduction

This tutorial demonstrates GPU-level debugging of SYCL/DPC++ applications using the integrated gdb-oneapi debugger.

## Prerequisites

- Intel oneAPI Base Toolkit 2024.0+
- CLion with Intel DPC++ plugin
- Example project from `examples/debugging-example`

## Setup

### 1. Source oneAPI Environment

```bash
source /opt/intel/oneapi/setvars.sh
```

### 2. Build with Debug Symbols

```cmake
# CMakeLists.txt
set(CMAKE_BUILD_TYPE Debug)
set(CMAKE_CXX_FLAGS_DEBUG "-g -O0")
```

```bash
cmake -B build -DCMAKE_BUILD_TYPE=Debug
cmake --build build
```

## Tutorial: Debugging a Vector Addition Kernel

### Example Code

```cpp
#include <sycl/sycl.hpp>
#include <vector>

int main() {
    const size_t N = 1024;
    std::vector<int> a(N), b(N), c(N);
    
    // Initialize data
    for (size_t i = 0; i < N; i++) {
        a[i] = i;
        b[i] = i * 2;
    }
    
    sycl::queue q{sycl::gpu_selector_v};
    
    {
        sycl::buffer<int> buf_a(a.data(), sycl::range<1>(N));
        sycl::buffer<int> buf_b(b.data(), sycl::range<1>(N));
        sycl::buffer<int> buf_c(c.data(), sycl::range<1>(N));
        
        q.submit([&](sycl::handler& h) {
            auto acc_a = buf_a.get_access<sycl::access::mode::read>(h);
            auto acc_b = buf_b.get_access<sycl::access::mode::read>(h);
            auto acc_c = buf_c.get_access<sycl::access::mode::write>(h);
            
            h.parallel_for(sycl::range<1>(N), [=](sycl::id<1> idx) {
                // BUG: Intentional off-by-one error
                if (idx[0] < N - 1) {
                    acc_c[idx] = acc_a[idx] + acc_b[idx + 1];  // Wrong!
                }
            });
        });
    }
    
    // Verify (will show errors)
    for (size_t i = 0; i < 10; i++) {
        printf("c[%zu] = %d (expected %d)\n", i, c[i], a[i] + b[i]);
    }
    
    return 0;
}
```

### Debugging Steps

#### Step 1: Set Breakpoint in Kernel

In CLion:
1. Open the `.dpcpp` file
2. Click line gutter inside `parallel_for` lambda
3. Red dot appears: breakpoint is set

#### Step 2: Launch Debug Session

1. **Tools → DPC++ → Debug with gdb-oneapi**
2. Select the executable in dialog
3. Click "Debug"

The IDE stops at the kernel breakpoint.

#### Step 3: Inspect GPU Thread State

When stopped at breakpoint:

```gdb
# In gdb-oneapi console
(gdb) info threads
  Thread 1 (process 12345) at main.cpp:15
  Thread 2 (GPU thread 0:0:0) at kernel.dpcpp:18
  Thread 3 (GPU thread 0:0:1) at kernel.dpcpp:18
  ...
  Thread 258 (GPU thread 0:7:255) at kernel.dpcpp:18
```

**GPU Thread Inspector**:
- Click "Show GPU Threads" button
- Visual hierarchy display

#### Step 4: Inspect Variables

Select specific GPU thread and print variables to find the off-by-one bug.

## Advanced Techniques

### Debugging Race Conditions
Use Thread Sanitizer for automatic race detection.

### Debugging Work-Group Local Memory
View local memory allocation per work-group.

### Debugging Multiple Devices
Select target device in Device Explorer.

## Command Line Workflow

```bash
# Build with debug symbols
icpx -g -O0 -fsycl kernel.cpp -o kernel

# Launch gdb-oneapi
gdb-oneapi ./kernel

# Set breakpoint
(gdb) break kernel.cpp:18
(gdb) run
```

## Tips & Tricks

- Debug builds are 10-100x slower on GPU
- Use conditional breakpoints to limit stops
- Use `thread apply all <cmd>` for batch operations

## Troubleshooting

### "Cannot access GPU threads"
```bash
sudo modprobe i915 enable_guc=3
```

### "Symbol not found"
Rebuild with `-g` flag.

## Next Steps

- [VTune Profiling Guide](./vtune-profiling.md)
- [Performance Optimization Workflow](./optimization-workflow.md)
