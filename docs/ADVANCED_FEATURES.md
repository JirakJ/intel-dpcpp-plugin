# Advanced Premium Features (v0.3+)

## Overview

This guide covers advanced premium features added in version 0.3 of the Intel DPC++ CLion Plugin.

---

## 🎯 Kernel Occupancy Calculator

### Purpose
Predicts theoretical GPU occupancy based on kernel resource usage and identifies limiting factors.

### How It Works

The occupancy calculator analyzes:
- **Work-group size** - Number of threads per work-group
- **Register usage** - Registers allocated per thread
- **Local memory** - Shared memory per work-group

Based on Intel GPU architecture limits, it calculates theoretical occupancy and identifies bottlenecks.

### Usage

**Tools → DPC++ → Kernel Occupancy Calculator (Premium)**

1. Enter work-group size (e.g., 256)
2. Review occupancy analysis
3. Apply suggested optimizations

### Example Analysis

```
Kernel Occupancy Analysis

Device: Intel(R) Data Center GPU Max 1550
Work-group size: 256
Registers per thread: 32
Local memory: 16384 bytes

📊 Theoretical Occupancy: 75.0%

🔍 Limiting Factor: Work-group size

💡 Optimization Suggestions:
  • Try increasing work-group size to 256 or 512
  • Current work-group size may be too small
  • Consider using nd_range with explicit dimensions
```

### Interpretation

| Occupancy | Meaning | Action |
|-----------|---------|--------|
| < 50% | Poor utilization | Increase work-group size or reduce resource usage |
| 50-80% | Moderate | Optimize limiting factor |
| > 80% | Good | Focus on memory access patterns |

### Limiting Factors

#### Work-Group Size
**Problem**: Too few threads to saturate GPU
**Solution**: Increase to 256, 512, or device maximum

#### Register Pressure
**Problem**: Too many temporary variables
**Solutions**:
- Reduce local variables
- Split kernel into multiple passes
- Use local memory instead

#### Local Memory
**Problem**: Excessive shared memory allocation
**Solutions**:
- Reduce tile/block size
- Use smaller data types
- Split into multiple kernels

---

## 📊 Memory Bandwidth Estimator

### Purpose
Analyzes memory access patterns and estimates bandwidth utilization to identify memory bottlenecks.

### Metrics Analyzed

- **Global memory loads/stores** - Device memory accesses
- **Local memory loads/stores** - Shared memory accesses
- **Required bandwidth** - Calculated from memory operations
- **Achieved bandwidth** - Limited by hardware peak
- **Utilization %** - Percentage of peak bandwidth

### Usage

Typically invoked automatically during VTune profiling, or manually:

```java
KernelMemoryProfile profile = new KernelMemoryProfile(
    globalLoads,
    globalStores,
    localLoads,
    localStores,
    elementSize,
    numWorkItems,
    executionTimeMs
);

MemoryBandwidthEstimator.showBandwidthAnalysis(project, profile);
```

### Example Analysis

```
Memory Bandwidth Analysis

📊 Bandwidth Metrics:
  Required: 180.5 GB/s
  Achieved: 180.5 GB/s
  Utilization: 30.1%

📈 Memory Operations:
  Global loads: 1048576
  Global stores: 1048576
  Local loads: 0
  Local stores: 0

🔍 Bottleneck: Balanced (memory/compute)

💡 Recommendations:
  • Kernel has balanced memory and compute
  • Good candidate for further compute optimization
  • Profile with VTune to identify specific hotspots
  • Consider increasing arithmetic intensity
```

### Bottleneck Types

#### Memory-Bound (Severe)
- **Utilization**: > 90%
- **Characteristics**: Spends most time waiting for memory
- **Solutions**:
  - Use local memory for frequently accessed data
  - Implement tiling
  - Reduce redundant accesses
  - Improve coalescing

#### Memory-Bound (Moderate)
- **Utilization**: 70-90%
- **Characteristics**: Significant memory overhead
- **Solutions**:
  - Optimize access patterns
  - Use vectorization
  - Async transfers

#### Balanced
- **Utilization**: 40-70%
- **Characteristics**: Neither memory nor compute dominant
- **Action**: Profile further to identify specific hotspots

#### Compute-Bound
- **Utilization**: < 40%
- **Characteristics**: Low memory pressure (good!)
- **Action**: Focus on algorithmic optimizations

### Arithmetic Intensity

**Definition**: FLOPs per byte transferred

```
Arithmetic Intensity = (Number of Operations) / (Bytes Transferred)
```

| Intensity | Characterization | Optimization Focus |
|-----------|------------------|-------------------|
| < 0.5 | Memory-bound | Increase data reuse, tiling |
| 0.5-2.0 | Mixed | Coalesce memory access |
| 2.0-10.0 | Balanced | Compute optimization |
| > 10.0 | Compute-bound | SIMD, loop unrolling |

---

## 🚀 Intel Advisor Integration

### Purpose
Advanced performance analysis with roofline modeling and optimization recommendations.

### Analysis Types

#### 1. Roofline Analysis

**What It Shows**: Performance ceiling based on arithmetic intensity

**Tools → DPC++ → Intel Advisor Analysis → Roofline Analysis**

**Roofline Chart**:
```
GFLOPs/s
   |
   |          Compute Bound
   |         /
   |        /  (at roofline)
   |-------/
   |      /
   |     / Memory Bound
   |    /  (below roofline)
   |___________________________
        Arithmetic Intensity (FLOPs/Byte)
```

**Interpretation**:
- **Above line**: Impossible (exceeds hardware limits)
- **At line**: Optimal performance
- **Below line**: Room for optimization

**Left side** (low intensity): Memory-bound → Focus on memory optimization
**Right side** (high intensity): Compute-bound → Focus on compute optimization

#### 2. Offload Modeling

**What It Shows**: Predicts speedup from GPU offload

**Metrics**:
- Potential speedup estimate
- Data transfer overhead
- Kernel launch overhead
- Synchronization cost

**Recommendations**:
- Which loops to offload
- Optimal work distribution
- Memory transfer strategies
- Kernel fusion opportunities

#### 3. Threading Analysis

**What It Shows**: Parallelization efficiency

**Checks**:
- Data races
- Deadlocks
- Load imbalance
- Lock contention

**Scalability Metrics**:
- Speedup vs thread count
- CPU utilization
- Wait time
- Critical path

### Command-Line Usage

```bash
# Roofline analysis
advisor --collect=roofline --project-dir=.advisor -- ./app

# Offload modeling
advisor --collect=offload --project-dir=.advisor -- ./app

# View results
advisor-gui .advisor
```

### Integration with Plugin

**Tools → DPC++ → Intel Advisor Analysis (Premium)**

Select analysis type:
1. Roofline Analysis
2. Offload Modeling
3. Threading Analysis

Results automatically imported into IDE for visualization.

---

## 🔧 SYCL Refactorings

### 1. Buffer to USM Refactoring

**Purpose**: Convert buffer/accessor code to Unified Shared Memory

**Before**:
```cpp
{
  buffer<int> buf(data, range<1>(N));
  q.submit([&](handler& h) {
    accessor acc(buf, h);
    h.parallel_for(range<1>(N), [=](id<1> i) {
      acc[i] = i;
    });
  });
}
```

**After**:
```cpp
int* data_usm = malloc_device<int>(N, q);
q.memcpy(data_usm, data, N * sizeof(int)).wait();
q.parallel_for(range<1>(N), [=](id<1> i) {
  data_usm[i] = i;
}).wait();
q.memcpy(data, data_usm, N * sizeof(int)).wait();
free(data_usm, q);
```

**Benefits**:
- Simpler code (no accessors)
- Explicit memory management
- Pointer-based algorithms easier
- Better for debugging

**Trade-offs**:
- Manual memory management
- Explicit synchronization
- No automatic dependency tracking

### 2. Extract Kernel Refactoring

**Purpose**: Extract inline lambda into reusable functor class

**Before**:
```cpp
q.parallel_for(range<1>(N), [=](id<1> i) {
  c[i] = a[i] + b[i];
});
```

**After**:
```cpp
class VectorAddKernel {
  accessor<int> a, b, c;
public:
  VectorAddKernel(accessor<int> a, accessor<int> b, accessor<int> c)
    : a(a), b(b), c(c) {}
  
  void operator()(id<1> i) const {
    c[i] = a[i] + b[i];
  }
};

q.parallel_for(range<1>(N), VectorAddKernel{acc_a, acc_b, acc_c});
```

**Benefits**:
- Reusable kernel code
- Easier to test
- Better for complex kernels
- Improved readability

### 3. Optimize Work-Group Size Refactoring

**Purpose**: Convert basic parallel_for to nd_range with optimal work-groups

**Before**:
```cpp
q.parallel_for(range<1>(N), [=](id<1> i) {
  c[i] = a[i] + b[i];
});
```

**After**:
```cpp
const size_t work_group_size = 256;
q.parallel_for(
  nd_range<1>(range<1>(N), range<1>(work_group_size)),
  [=](nd_item<1> item) {
    size_t i = item.get_global_id(0);
    if (i < N) {
      c[i] = a[i] + b[i];
    }
  }
);
```

**Expected Speedup**: 1.5-3x (depends on memory patterns)

**Benefits**:
- Better GPU occupancy
- Improved memory coalescing
- Reduced kernel launch overhead

### Work-Group Size Heuristics

| Problem Size | Suggested Size | Rationale |
|-------------|----------------|-----------|
| < 1K | 128 | Low overhead |
| 1K-100K | 256 | Balanced |
| > 100K | 512 | Maximum occupancy |
| 2D | 16x16 | 256 threads total |
| 3D | 8x8x8 | 512 threads total |

---

## 📈 Performance Optimization Workflow

### Step 1: Profile Baseline
```bash
# VTune GPU hotspots
vtune -collect gpu-hotspots -- ./app
```

**Identify**:
- Slowest kernels
- GPU utilization %
- Memory bandwidth %

### Step 2: Analyze Occupancy
**Tools → DPC++ → Kernel Occupancy Calculator**

**Optimize**:
- Adjust work-group size
- Reduce register pressure
- Optimize local memory usage

### Step 3: Analyze Memory
Use Memory Bandwidth Estimator to identify:
- Memory-bound vs compute-bound
- Arithmetic intensity
- Access pattern issues

### Step 4: Apply Roofline Analysis
**Tools → DPC++ → Intel Advisor → Roofline**

**Determine**:
- Current position on roofline
- Memory or compute optimization needed
- Target performance ceiling

### Step 5: Refactor Code
Apply appropriate refactorings:
- Buffer to USM (if beneficial)
- Extract complex kernels
- Optimize work-group sizes

### Step 6: Re-measure
Run profiling again and compare:
- Speedup achieved
- Occupancy improvement
- Bandwidth utilization

---

## 🎓 Best Practices

### Occupancy Optimization
1. **Start with 256** threads per work-group (good default)
2. **Measure actual performance** - theory ≠ practice
3. **Balance resources** - don't optimize one at expense of others
4. **Profile on target hardware** - different GPUs behave differently

### Memory Optimization
1. **Coalesce accesses** - consecutive threads access consecutive memory
2. **Use local memory** for frequently reused data
3. **Minimize transfers** between host and device
4. **Overlap computation** with data transfers

### Roofline Optimization
1. **Know your position** - memory or compute bound?
2. **Move right** (increase arithmetic intensity) for memory-bound
3. **Move up** (optimize compute) for compute-bound
4. **Aim for roofline** but don't over-optimize

### Refactoring Guidelines
1. **Profile first** - measure before refactoring
2. **One change at a time** - isolate improvements
3. **Validate correctness** - refactoring shouldn't change results
4. **Re-profile** - verify improvement

---

## 💡 Pro Tips

### Occupancy Calculator
- Run on multiple work-group sizes (128, 256, 512, 1024)
- Compare results to find optimal
- Consider register spilling at large sizes

### Bandwidth Estimator
- Low utilization + fast execution = good (compute-bound)
- High utilization + slow execution = bad (memory-bound)
- Focus optimization where it matters most

### Intel Advisor
- Roofline identifies optimization category
- Offload modeling predicts GPU benefit
- Use together for comprehensive analysis

### Refactorings
- Buffer to USM: Best for pointer-heavy algorithms
- Extract Kernel: Best for reusable/complex kernels
- Optimize Work-Group: Apply to all kernels systematically

---

**Available in Professional Edition ($99/year)**

[View Licensing & Pricing →](LICENSING.md)
