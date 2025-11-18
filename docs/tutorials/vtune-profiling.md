# VTune Profiling Guide

## Introduction

Learn how to use Intel VTune Profiler integration to optimize SYCL/DPC++ application performance.

## Prerequisites

- Intel VTune Profiler (oneAPI Base Toolkit)
- CLion with Intel DPC++ plugin (Professional Edition)
- Performance counters enabled

## Setup

### Enable Performance Counters

```bash
# Temporary
sudo sysctl -w kernel.perf_event_paranoid=0

# Permanent
echo "kernel.perf_event_paranoid = 0" | sudo tee -a /etc/sysctl.conf
```

### Source oneAPI Environment

```bash
source /opt/intel/oneapi/setvars.sh
```

## Analysis Types

### 1. GPU Hotspots

Identifies performance bottlenecks in kernels.

**Usage in CLion**:
1. **Tools → DPC++ → Profile with VTune (Premium)**
2. Select "GPU Hotspots"
3. Configure executable
4. Run analysis

**Metrics**:
- Kernel execution time
- GPU utilization (%)
- EU (Execution Unit) occupancy
- Memory bandwidth usage

**Example Output**:
```
Function                     | GPU Time | % of Total
-----------------------------|----------|------------
vector_add_kernel            | 45.2 ms  | 62.3%
matrix_multiply_kernel       | 18.7 ms  | 25.8%
reduction_kernel             | 8.6 ms   | 11.9%
```

### 2. GPU Offload Analysis

Tracks host-device data transfers and queue operations.

**Metrics**:
- Host-to-device transfer time
- Device-to-host transfer time
- Queue submission overhead
- Kernel launch latency
- Synchronization points

**Optimization Tips**:
- Batch transfers to reduce overhead
- Use async operations
- Overlap compute with transfers

### 3. Memory Analysis

Analyzes memory access patterns and bandwidth utilization.

**Metrics**:
- Memory bandwidth achieved vs. peak
- Cache hit rates
- Uncoalesced memory accesses
- Bank conflicts in local memory

### 4. Roofline Analysis

Determines if application is memory-bound or compute-bound.

**Interpretation**:
- Below roofline: memory-bound
- At roofline: well-optimized
- Left side: needs more work per byte

## Example Workflow

### Step 1: Baseline Performance

```cpp
// Original implementation
q.parallel_for(range<1>(N), [=](id<1> i) {
    c[i] = a[i] + b[i];
});
```

Run GPU Hotspots analysis:
- Execution time: 125 ms
- GPU utilization: 35%
- Bandwidth: 45 GB/s (peak: 600 GB/s)

**Conclusion**: Memory-bound, low utilization

### Step 2: Optimize Work-Group Size

```cpp
// Optimized with explicit work-group size
q.parallel_for(nd_range<1>(N, 256), [=](nd_item<1> item) {
    auto i = item.get_global_id(0);
    c[i] = a[i] + b[i];
});
```

Run again:
- Execution time: 85 ms (1.5x faster)
- GPU utilization: 68%
- Bandwidth: 180 GB/s

### Step 3: Use Local Memory

```cpp
// Use local memory for data reuse
q.parallel_for(nd_range<1>(N, 256), [=](nd_item<1> item) {
    auto local_mem = sycl::local_accessor<int, 1>(256, item);
    // ... use local_mem for caching
});
```

Final results:
- Execution time: 42 ms (3x faster than baseline)
- GPU utilization: 92%
- Bandwidth: 520 GB/s

## Command Line VTune

```bash
# Collect GPU hotspots
vtune -collect gpu-hotspots -result-dir ./vtune_results -- ./app

# Collect GPU offload
vtune -collect gpu-offload -result-dir ./vtune_results -- ./app

# Generate report
vtune -report summary -result-dir ./vtune_results

# Open GUI
vtune-gui ./vtune_results
```

## Integration with CLion

Results automatically imported into:
- **Performance Tab**: Timeline view
- **Bottom-Up View**: Function call tree
- **Top-Down View**: Caller-callee relationships

## Best Practices

1. **Profile Release Builds**: Use `-O2` or `-O3`
2. **Multiple Runs**: Average 3-5 runs for consistency
3. **Isolate Kernels**: Profile individual kernels separately
4. **Compare Versions**: Keep baseline for comparison
5. **Focus on Hotspots**: Optimize top 20% of time first

## Troubleshooting

### "Driver doesn't support metrics collection"
Update GPU drivers to latest version.

### "Permission denied"
Enable performance counters (see Setup section).

### "No GPU activity detected"
Verify GPU selector and device availability.

## Next Steps

- [Performance Optimization Workflow](./optimization-workflow.md)
- [Advanced SYCL Patterns](./advanced-patterns.md)
