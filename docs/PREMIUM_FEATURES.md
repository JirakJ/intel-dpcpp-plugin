# Premium Features Guide

## Overview

The Intel DPC++ CLion Plugin offers premium features for professional heterogeneous computing development. These advanced capabilities enhance debugging, profiling, and code analysis for SYCL/DPC++ applications.

## 🔍 GPU Debugging with gdb-oneapi

### Features
- **Breakpoints in Kernels** - Set breakpoints directly in SYCL parallel_for kernels
- **GPU Thread Inspector** - Visualize active GPU threads and work-group hierarchy
- **Device Memory Viewer** - Inspect GPU memory allocations (USM, buffers)
- **Thread State Tracking** - Monitor thread execution status (running/suspended)
- **Conditional Breakpoints** - Break on specific GPU thread conditions

### Requirements
- Intel oneAPI Base Toolkit 2024.0+
- gdb-oneapi debugger (included in oneAPI)

### Usage
1. **Tools → DPC++ → Debug with gdb-oneapi (Premium)**
2. Select executable in Run/Debug Configuration
3. Set breakpoints in .dpcpp files
4. Launch debug session

### Example Session
```bash
# Source oneAPI environment
source /opt/intel/oneapi/setvars.sh

# Launch CLion with oneAPI tools
clion
```

In CLion:
- Open DPC++ project
- Set breakpoint inside `parallel_for` lambda
- Run "Debug with gdb-oneapi"
- Use GPU Thread Inspector to view all threads

---

## ⚡ Performance Profiling with VTune

### Analysis Types

#### 1. GPU Hotspots
Identifies performance bottlenecks in SYCL kernels:
- Kernel execution time
- GPU utilization percentage
- Memory bandwidth usage
- Compute unit occupancy

#### 2. GPU Offload Analysis
Tracks host-device interactions:
- Data transfer overhead
- Queue submission latency
- Synchronization points
- Host-device balance

#### 3. Kernel Analysis
Deep dive into individual kernels:
- Work-group efficiency
- Memory access patterns
- Cache hit rates
- Instruction mix

#### 4. Memory Analysis
Memory performance metrics:
- Host-to-device transfer rates
- Device-to-host transfer rates
- USM allocation patterns
- Memory bandwidth utilization

### Requirements
- Intel VTune Profiler (oneAPI Base Toolkit)
- GPU drivers with performance counters enabled

### Usage
1. **Tools → DPC++ → Profile with VTune (Premium)**
2. Select analysis type
3. Configure executable path
4. Run analysis
5. View results in `.vtune` directory

### Command Line Integration
```bash
# GPU Hotspots
vtune -collect gpu-hotspots -result-dir ./vtune_results -- ./my_dpcpp_app

# GPU Offload
vtune -collect gpu-offload -result-dir ./vtune_results -- ./my_dpcpp_app

# Open GUI
vtune-gui ./vtune_results
```

---

## 📊 Device Explorer Tool Window

Real-time device monitoring and capability inspection.

### Features
- **Platform Detection** - Automatically detect Level Zero, OpenCL, CUDA backends
- **Device Enumeration** - List all available SYCL devices (CPU, GPU, FPGA)
- **Capability Inspection**:
  - Compute unit count
  - Max work-group size
  - Memory hierarchy (global, local, constant)
  - Supported SYCL features
- **Live Monitoring**:
  - Real-time GPU utilization
  - Memory usage statistics
  - Temperature (if available)
- **Device Selection** - Choose target device for debugging/profiling

### Access
**View → Tool Windows → DPC++ Devices**

### Device Information Display
```
Intel(R) Data Center GPU Max 1550
├── Platform: Level Zero
├── Compute Units: 128
├── Max Clock: 1600 MHz
├── Global Memory: 48 GB HBM2e
├── Local Memory: 64 KB per work-group
├── Max Work-Group Size: 1024
├── Supported Features:
│   ✓ USM Shared Allocations
│   ✓ USM Device Allocations
│   ✓ Sub-groups (8, 16, 32)
│   ✓ Atomics (64-bit)
└── Current Utilization: 12%
```

---

## 🔬 Advanced Code Analysis

### SYCL Code Quality Inspection

Detects common SYCL programming issues:

#### Race Condition Detection
```cpp
// ⚠️ WARNING: Potential race condition
q.parallel_for(range<1>(N), [=](id<1> i) {
    data[i] = data[i-1] + 1;  // Dependency between iterations
});
```

#### Inefficient Memory Patterns
```cpp
// ⚠️ WARNING: Inefficient memory access
q.parallel_for(range<2>(M, N), [=](id<2> idx) {
    // Column-major access on row-major data
    result[idx[1]][idx[0]] = data[idx[1]][idx[0]] * 2;
});
```

#### USM Best Practices
```cpp
// ⚠️ WARNING: Missing explicit synchronization
auto data = malloc_device<int>(N, q);
q.parallel_for(range<1>(N), [=](id<1> i) {
    data[i] = i;
});
// Missing q.wait() before accessing data on host
```

### SYCL Performance Optimization

Suggests performance improvements:

#### Synchronization Overhead
```cpp
// ⚠️ WEAK WARNING: Unnecessary synchronization
for (int i = 0; i < iterations; i++) {
    q.submit([&](handler& h) {
        // kernel
    }).wait();  // Synchronous submission in loop
}
// SUGGESTION: Use async submission and wait once after loop
```

#### Sub-optimal Work-Group Size
```cpp
// ⚠️ WEAK WARNING: Small work-group size
q.parallel_for(nd_range<1>(N, 32), [=](nd_item<1> item) {
    // GPU can handle larger work-groups (e.g., 256)
});
// SUGGESTION: Consider work-group size 256 or 512
```

#### Buffer vs USM Selection
```cpp
// 💡 INFO: Consider USM for better performance
buffer<int> buf(data, range<1>(N));
q.submit([&](handler& h) {
    accessor acc(buf, h);
    // accessor overhead for simple operations
});
// SUGGESTION: USM may be more efficient for this pattern
```

---

## 🎯 Kernel Analysis (Coming in v0.3)

### Occupancy Prediction
Estimates GPU utilization for kernel configurations:
- Theoretical occupancy percentage
- Limiting factors (registers, local memory, work-group size)
- Optimization suggestions

### Memory Bandwidth Estimation
Predicts memory bottlenecks:
- Required bandwidth calculation
- Available bandwidth comparison
- Memory-bound vs compute-bound classification

---

## 💼 Enterprise Features

### Team License
- Unlimited seats for organization
- Priority support (24-hour response)
- Custom training sessions
- Quarterly updates

### Enterprise Support
- Dedicated Slack channel
- Custom feature development
- On-site consulting available
- Architecture review sessions

---

## 📦 Installation

### Requirements
1. **Intel oneAPI Base Toolkit 2024.0+**
   ```bash
   # Download from:
   # https://www.intel.com/content/www/us/en/developer/tools/oneapi/base-toolkit.html
   ```

2. **CLion 2023.2+**

3. **Plugin Installation**
   - Settings → Plugins
   - Search "Intel DPC++ Support"
   - Install and restart

### Activation
Premium features require Professional Edition license:
1. Help → Register → Intel DPC++ Professional
2. Enter license key
3. Restart CLion

---

## 🎓 Training Resources

### Tutorials
- [GPU Debugging Walkthrough](./tutorials/gpu-debugging.md)
- [VTune Profiling Guide](./tutorials/vtune-profiling.md)
- [Performance Optimization Workflow](./tutorials/optimization-workflow.md)

### Example Projects
```bash
cd examples/
├── debugging-example/      # Breakpoint and memory inspection demo
├── profiling-example/      # VTune analysis workflow
└── optimization-example/   # Before/after optimization case study
```

---

## 🐛 Troubleshooting

### gdb-oneapi Not Found
**Solution**: Source oneAPI environment
```bash
source /opt/intel/oneapi/setvars.sh
# Or add to ~/.bashrc or ~/.zshrc
```

### VTune Permission Denied
**Solution**: Enable performance counters
```bash
# Temporary (until reboot)
sudo sysctl -w kernel.perf_event_paranoid=0

# Permanent
echo "kernel.perf_event_paranoid = 0" | sudo tee -a /etc/sysctl.conf
```

### Device Explorer Shows No Devices
**Solution**: Verify GPU drivers and oneAPI installation
```bash
# Check SYCL devices
sycl-ls

# Verify Level Zero
ls /dev/dri/render*

# Check OpenCL
clinfo
```

---

## 📞 Support

### Community
- GitHub Issues: https://github.com/jakubjirak/dpcpp-clion-plugin/issues
- Discussions: https://github.com/jakubjirak/dpcpp-clion-plugin/discussions

### Professional Support
- Email: jakub.jirak@pm.me
- Response time: 24 hours (enterprise customers)
- Slack: Premium/Enterprise customers only

---

## 💰 Pricing

| Edition | Price | Features |
|---------|-------|----------|
| **Community** | Free | Basic syntax, completion, templates |
| **Professional** | $99/year | All premium features, priority support |
| **Enterprise** | $499/year | Unlimited seats, custom training |

**Volume discounts**: 10+ licenses: 20% off | 50+ licenses: 35% off

**Academic/Non-profit**: 50% discount with valid .edu email

---

## 🔄 Roadmap

### v0.3.0 (Q2 2025)
- Kernel occupancy calculator
- Memory bandwidth predictor
- Intel Advisor integration

### v0.4.0 (Q3 2025)
- FPGA-specific tooling
- Multi-device debugging
- Custom metrics dashboard

### v1.0.0 (Q4 2025)
- Full language server protocol
- AI-powered optimization hints
- Cross-platform profiling

---

Built with ❤️ for heterogeneous computing professionals
