# Intel DPC++ Support — Screenshot Plan

## Screenshots

### 1. SYCL Syntax Highlighting
**What to capture:** A DPC++ source file open in CLion showing SYCL 2020 keywords highlighted (sycl::queue, sycl::buffer, parallel_for, handler). Include colorful differentiation between SYCL-specific and standard C++ tokens.

### 2. Code Completion Popup
**What to capture:** The code completion dropdown after typing `sycl::` showing available namespace entities (queue, buffer, handler, range, id) with type hints and documentation preview.

### 3. Live Templates in Action
**What to capture:** A before/after of typing `syclsubmit` and pressing Tab, showing the expanded template with a queue submit block and handler lambda. Include the Tab cursor positions.

### 4. oneAPI Toolchain Configuration
**What to capture:** The Tools → DPC++ → Configure oneAPI Toolchain dialog showing auto-detected oneAPI installation path, compiler version, and CMake configuration status.

### 5. Device Explorer Tool Window
**What to capture:** The DPC++ Devices tool window on the right side showing available compute devices — CPU (Intel Xeon), GPU (Intel Arc), and FPGA — with their properties and status.

### 6. GPU Debugging Session
**What to capture:** A gdb-oneapi debugging session in CLion with a breakpoint inside a SYCL kernel. Show the debug panel with GPU thread state, variable values, and the kernel source code.

### 7. VTune Profiler Results
**What to capture:** The VTune profiling results view showing GPU hotspot analysis with kernel execution times, host-device transfer overhead, and memory bandwidth utilization charts.

### 8. FPGA Optimization Report
**What to capture:** The FPGA optimization analyzer output showing resource estimation (ALMs, RAM blocks, DSP blocks), pipeline analysis, and optimization suggestions with pragma recommendations.

---

## Video Demo Outline (50 seconds)

| Time | Scene | Description |
|------|-------|-------------|
| 0:00–0:07 | Intro | Show "Intel DPC++ Support" plugin name. Open CLion with a DPC++ project. |
| 0:07–0:15 | Syntax and completion | Type SYCL code, show syntax highlighting and code completion popup |
| 0:15–0:22 | Live template | Type `syclparallel` + Tab, show the expanded parallel_for template |
| 0:22–0:30 | Toolchain setup | Quick flash of the oneAPI toolchain configuration dialog |
| 0:30–0:37 | Device explorer | Open the DPC++ Devices tool window, show detected CPU and GPU devices |
| 0:37–0:45 | Debugging | Set breakpoint in a kernel, run gdb-oneapi, inspect GPU thread variables |
| 0:45–0:50 | Outro | Show pricing info and links to install from Marketplace |
