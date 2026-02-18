# Intel DPC++ Support

## Develop for CPUs, GPUs, and FPGAs with One Codebase

> Full SYCL 2020 and DPC++ development support for JetBrains CLion — from code completion to GPU debugging and VTune profiling.

---

## The Problem

Heterogeneous computing is becoming essential for performance, but the tooling hasn't kept up. Developers writing DPC++/SYCL code are stuck with command-line workflows, no IDE support, and manual compiler configuration. Setting up the oneAPI toolchain in CLion requires manual CMake tweaking, and debugging GPU kernels means dropping to the terminal.

---

## Key Features

### SYCL 2020 Syntax Highlighting
Full keyword highlighting for SYCL parallel constructs, device selectors, buffers, accessors, and all SYCL namespace entities. DPC++-specific extensions are supported too.

### Smart Code Completion
Context-aware autocomplete for sycl::queue, sycl::buffer, accessors, parallel_for, and all SYCL APIs. Type `sycl::` and see every available entity with documentation.

### oneAPI Toolchain Detection
The plugin auto-detects your Intel oneAPI installation and configures CMake for the DPC++ compiler. No manual `CMAKE_CXX_COMPILER` setup needed.

### Live Templates
Six built-in code snippets for common DPC++ patterns: `syclqueue`, `syclbuffer`, `syclsubmit`, `syclparallel`, `syclaccess`, and `syclusm`. Type the abbreviation and press Tab.

### GPU Debugging with gdb-oneapi
Debug SYCL kernels directly in CLion. Set breakpoints inside kernels, inspect GPU thread state, view device memory, and step through kernel execution.

### VTune Profiler Integration
Profile your DPC++ code with Intel VTune Profiler. Run GPU hotspot analysis, GPU offload tracking, and memory bandwidth profiling from the IDE.

### Device Explorer
Browse all available compute devices (CPU, GPU, FPGA) in a dedicated tool window with real-time monitoring of device properties and status.

### FPGA Optimization
Get resource estimation, pragma suggestions, and pipeline analysis for FPGA compilation targets. Optimize your kernels for Intel FPGA devices.

---

## How It Works

1. **Install Intel oneAPI** — Download and install the Intel oneAPI Base Toolkit with the DPC++ compiler.
2. **Install the plugin** — Search for "Intel DPC++" in the JetBrains Marketplace and install it in CLion.
3. **Configure toolchain** — Go to Tools → DPC++ → Configure oneAPI Toolchain. The plugin auto-detects your installation.
4. **Start coding** — Create `.dpcpp` or `.dp.cpp` files with full IDE support, or use live templates to generate boilerplate quickly.

---

## Pricing

### Free (Open Source) — $0
- SYCL 2020 syntax highlighting
- File type support (.dpcpp, .dp.cpp)
- Code completion for SYCL APIs
- Live templates for common patterns
- CMake integration with oneAPI detection
- Documentation on hover
- Project templates

### Professional Edition — Contact for Pricing
- GPU debugging with gdb-oneapi
- VTune Profiler integration
- Device Explorer tool window
- Advanced code inspections
- Kernel occupancy calculator
- Intel Advisor integration (roofline analysis)
- SYCL refactorings (Buffer→USM, Extract Kernel)
- FPGA optimization analyzer
- Multi-device orchestrator
- AI optimization assistant
- Live kernel editing with hot-reload
- Intel DevCloud integration

### Enterprise — Contact for Pricing
- Language Server Protocol (LSP) integration
- Team collaboration features
- SSO and license management
- CI/CD integration
- Admin dashboard

*Academic/Non-profit: 50% discount available.*

---

## Testimonials

> *"The oneAPI toolchain detection alone saved us hours of CMake configuration. The VTune integration is the cherry on top."*
> — **HPC Researcher, University Lab**

> *"Being able to debug SYCL kernels directly in CLion changed our development workflow completely."*
> — **Software Engineer, Semiconductor Company**

> *"The FPGA optimization hints helped us cut our synthesis time and improve resource utilization significantly."*
> — **FPGA Developer, Research Institute**

---

## FAQ

**Q: Which JetBrains IDEs are supported?**
A: The plugin is designed for CLion 2023.2 or later.

**Q: Do I need Intel hardware?**
A: The free features work on any platform. GPU debugging and profiling require Intel GPUs or compatible devices with the oneAPI toolkit installed.

**Q: Is the free version truly open source?**
A: Yes, the core plugin is licensed under Apache 2.0. Premium features require a Professional or Enterprise license.

**Q: Does it support FPGA development?**
A: Yes. The Professional Edition includes FPGA optimization analysis, resource estimation, and pragma suggestions for Intel FPGA targets.

**Q: Can I use it with Intel DevCloud?**
A: Yes. The Professional Edition includes Intel DevCloud integration for cloud compilation and testing on 7 device types.

---

## Get Started

Install Intel DPC++ Support and bring the full power of the oneAPI ecosystem into your CLion IDE.

[Install from Marketplace](https://plugins.jetbrains.com) | [GitHub](https://github.com/jakubjirak/dpcpp-clion-plugin) | [Intel oneAPI](https://www.intel.com/content/www/us/en/developer/tools/oneapi/overview.html)
