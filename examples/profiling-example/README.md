# VTune Profiling Example

Performance optimization demonstration using Intel VTune Profiler integration.

## Overview

This example compares two matrix multiplication implementations:

### Version 1: Unoptimized (Baseline)
- Simple `parallel_for` with `range`
- No work-group optimization
- Non-coalesced memory access
- **Expected performance**: Slow, low GPU utilization

### Version 2: Optimized
- Uses `nd_range` with explicit work-groups (16x16 tiles)
- Local memory for data reuse
- Coalesced memory access patterns
- **Expected performance**: 3-10x faster

## Prerequisites

- Intel oneAPI Base Toolkit 2024.0+ with VTune Profiler
- CLion with Intel DPC++ Plugin
- GPU with SYCL support

## Build & Run

```bash
# Source oneAPI environment
source /opt/intel/oneapi/setvars.sh

# Build with optimizations
cmake -B build -DCMAKE_BUILD_TYPE=Release
cmake --build build

# Run
./build/profiling-example
```

Expected output:
```
Running on: Intel(R) Data Center GPU Max 1550

🐌 Version 1: Unoptimized (baseline)
   Time: 245.3 ms

🚀 Version 2: Optimized (work-groups + local memory)
   Time: 42.7 ms

📊 Results:
   Speedup: 5.74x
   Performance gain: 474.0%

✅ Results verified: Both versions produce same output
```

## Profiling Exercise

### Method 1: CLion Integration

#### Step 1: Profile Version 1
1. **Tools → DPC++ → Profile with VTune**
2. Select "GPU Hotspots"
3. Run analysis
4. Wait for results

#### Step 2: Analyze Bottlenecks
VTune shows:
- **GPU Utilization**: ~30-40% (low!)
- **Memory Bandwidth**: 45-60 GB/s (far below peak)
- **EU Occupancy**: Low due to small work-groups
- **Hotspot**: `matrixMultiply_v1` kernel

**Diagnosis**: Memory-bound, poor work-group utilization

#### Step 3: Profile Version 2
1. Modify `main()` to run only v2
2. Re-run VTune analysis
3. Compare metrics

VTune shows:
- **GPU Utilization**: ~85-95% (excellent!)
- **Memory Bandwidth**: 450-550 GB/s (near peak)
- **EU Occupancy**: High due to optimized work-groups
- **Speedup**: 3-10x depending on GPU

#### Step 4: Memory Analysis
1. Tools → DPC++ → Profile with VTune → "GPU Offload"
2. Analyze data transfer patterns:
   - Host-to-device: A and B matrices
   - Device-to-host: C matrix
   - Cache efficiency improved in v2

### Method 2: Command-Line VTune

```bash
# Profile GPU hotspots
vtune -collect gpu-hotspots \
      -result-dir vtune_results_v1 \
      -- ./build/profiling-example

# Generate summary report
vtune -report summary -result-dir vtune_results_v1

# Example output:
# GPU Computing: 245.3ms
# Top Hotspot: matrixMultiply_v1 (62.3% of GPU time)
# Memory Bandwidth: 58 GB/s (9.7% of peak)
```

#### Open GUI for detailed analysis
```bash
vtune-gui vtune_results_v1
```

### Method 3: Compare Both Versions

Create script to compare:

```bash
#!/bin/bash

# Profile v1
vtune -collect gpu-hotspots -result-dir vtune_v1 -- ./build/profiling-example

# Profile v2 (modify main.cpp to run only v2)
# ... rebuild ...
vtune -collect gpu-hotspots -result-dir vtune_v2 -- ./build/profiling-example

# Compare
vtune -report comparison -result-dir vtune_v1 -compare-with vtune_v2
```

## Key Metrics to Observe

### GPU Utilization
- **v1**: 30-40% (suboptimal)
- **v2**: 85-95% (excellent)
- **Improvement**: More work-items executing in parallel

### Memory Bandwidth
- **v1**: 45-60 GB/s (< 10% of peak)
- **v2**: 450-550 GB/s (> 75% of peak)
- **Improvement**: Coalesced memory access + local memory caching

### EU (Execution Unit) Occupancy
- **v1**: Low - small effective work-groups
- **v2**: High - explicit 16x16 work-groups saturate GPU
- **Improvement**: Better hardware utilization

### Kernel Execution Time
- **v1**: 245 ms
- **v2**: 42 ms
- **Speedup**: 5.8x

## Optimization Techniques Demonstrated

### 1. Work-Group Size Tuning
```cpp
// Before: implicit work-group
h.parallel_for(range<2>(M, N), ...);

// After: explicit 16x16 work-groups
h.parallel_for(nd_range<2>(range<2>(M, N), range<2>(16, 16)), ...);
```

**Why it helps**: Matches GPU architecture (16 or 32 SIMD width)

### 2. Local Memory (Shared Memory)
```cpp
local_accessor<float, 2> tile_a(range<2>(16, 16), h);
local_accessor<float, 2> tile_b(range<2>(16, 16), h);
```

**Why it helps**: Reduces global memory accesses by caching tiles

### 3. Coalesced Memory Access
```cpp
// Load contiguous memory for better coalescing
tile_a[local_row][local_col] = acc_a[row][t + local_col];
```

**Why it helps**: GPU memory systems optimized for sequential access

### 4. Synchronization
```cpp
item.barrier(access::fence_space::local_space);
```

**Why it helps**: Ensures all threads in work-group see consistent local memory

## Learning Objectives

✅ Profile GPU applications with VTune  
✅ Identify memory bandwidth bottlenecks  
✅ Optimize work-group sizes  
✅ Use local memory for performance  
✅ Measure speedup and utilization  
✅ Interpret VTune profiling reports  

## Advanced: Roofline Analysis

VTune can generate Roofline plots:

```bash
vtune -collect gpu-hotspots -knob enable-roofline=true \
      -result-dir vtune_roofline \
      -- ./build/profiling-example
```

**Roofline interpretation**:
- **Below line**: Memory-bound (optimize memory access)
- **At line**: Compute-bound (optimize compute)
- **Left side**: Low arithmetic intensity (need more compute per byte)

## Troubleshooting

### "VTune Profiler not found"
```bash
which vtune
# If empty, source oneAPI:
source /opt/intel/oneapi/setvars.sh
```

### "Permission denied" on performance counters
```bash
sudo sysctl -w kernel.perf_event_paranoid=0
```

### Low speedup
- Ensure Release build (`-O2` or `-O3`)
- Verify GPU is actually used (not CPU fallback)
- Check matrix size is large enough (1024x1024 minimum)

## Next Steps

- [GPU Debugging Example](../debugging-example)
- [VTune Profiling Tutorial](../../docs/tutorials/vtune-profiling.md)
- [Performance Optimization Workflow](../../docs/tutorials/optimization-workflow.md)

---

