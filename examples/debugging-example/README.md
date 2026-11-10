# GPU Debugging Example

This example demonstrates GPU debugging capabilities of the Intel DPC++ CLion Plugin.

## Bug Description

The code contains an intentional **off-by-one error** in the vector addition kernel:

```cpp
acc_c[idx] = acc_a[idx] + acc_b[idx + 1];  // BUG!
```

Should be:
```cpp
acc_c[idx] = acc_a[idx] + acc_b[idx];
```

## Prerequisites

- Intel oneAPI Base Toolkit 2024.0+
- CLion with Intel DPC++ Plugin
- GPU with SYCL support

## Build & Run

```bash
# Source oneAPI environment
source /opt/intel/oneapi/setvars.sh

# Build
cmake -B build -DCMAKE_BUILD_TYPE=Debug
cmake --build build

# Run (will show errors)
./build/debugging-example
```

Expected output:
```
Running on: Intel(R) Data Center GPU Max 1550
❌ FAILED - Errors detected:
  c[0] = 2 (expected 0)
  c[1] = 5 (expected 3)
  c[2] = 8 (expected 6)
  ...
```

## Debugging Exercise

### Step 1: Open in CLion
1. Open this directory as a CMake project
2. CLion detects CMakeLists.txt
3. Wait for CMake configuration

### Step 2: Set Breakpoint
1. Open `main.dpcpp`
2. Find line inside `parallel_for` lambda
3. Click gutter to set breakpoint (red dot)

### Step 3: Launch GPU Debugger
1. **Tools → DPC++ → Debug with gdb-oneapi**
2. Select `debugging-example` executable
3. Click "Debug"

### Step 4: Inspect GPU Threads
When breakpoint hits:
- **Variables View**: Shows `idx`, `acc_a[idx]`, `acc_b[idx]`
- **GPU Thread Inspector**: Click to see all 1024 GPU threads
- **Console**: Use gdb commands:
  ```gdb
  (gdb) info threads         # List all GPU threads
  (gdb) thread 10           # Switch to thread 10
  (gdb) print idx           # Show index
  (gdb) print acc_b[idx]    # Correct value
  (gdb) print acc_b[idx+1]  # Wrong value (off-by-one)
  ```

### Step 5: Find the Bug
Notice the discrepancy:
- `acc_a[idx]` = correct
- `acc_b[idx]` = correct
- `acc_b[idx + 1]` = **wrong!**

### Step 6: Fix the Bug
Change line 38:
```cpp
// Before (buggy)
acc_c[idx] = acc_a[idx] + acc_b[idx + 1];

// After (fixed)
acc_c[idx] = acc_a[idx] + acc_b[idx];
```

### Step 7: Verify Fix
```bash
cmake --build build
./build/debugging-example
```

Output:
```
Running on: Intel(R) Data Center GPU Max 1550
✅ PASSED - All results correct!
```

## Advanced Techniques

### Conditional Breakpoint
Right-click breakpoint → Edit Breakpoint → Condition:
```cpp
idx == 512
```

Now only thread 512 stops.

### GPU Memory Inspector
**Tools → DPC++ → GPU Memory Inspector**

View device memory allocation:
- Buffer addresses
- USM allocations
- Memory layout

### Watch Expressions
Add watch for specific threads:
```gdb
watch -l acc_c[512]  # Break when acc_c[512] changes
```

## Learning Objectives

✅ Set breakpoints in SYCL kernels  
✅ Inspect GPU thread state  
✅ Compare expected vs actual values  
✅ Use conditional breakpoints  
✅ Navigate between GPU threads  
✅ Fix data race and off-by-one bugs  

## Next Steps

- [VTune Profiling Example](../profiling-example)
- [GPU Debugging Tutorial](../../docs/tutorials/gpu-debugging.md)

---

