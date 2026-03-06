# Changelog

All notable changes to the Intel DPC++ CLion Plugin will be documented in this file.

## [2025.1.10] - 2025-11-16 ✅ PUBLISHED

### Added - Core Features (Free)
- SYCL 2020 syntax highlighting for keywords and constructs
- File type recognition for `.dpcpp` and `.dp.cpp` files
- Code completion for SYCL APIs (sycl::queue, sycl::buffer, parallel_for, etc.)
- Live templates for common DPC++ patterns (syclmain, syclqueue, syclparallel, etc.)
- CMake integration with oneAPI compiler detection
- Documentation provider with hover tooltips for SYCL APIs
- Actions menu: Configure Toolchain, Code Samples, Documentation
- Plugin icons (SVG format) for DPC++, toolchain, and samples
- Basic annotator for semantic highlighting of SYCL constructs
- Color settings page for customizing DPC++ syntax colors

### Added - Advanced Features
- **GPU Debugging** - gdb-oneapi debugger integration with GPU thread visualization
- **Performance Profiling** - Intel VTune Profiler integration (GPU hotspots, offload analysis)
- **GPU Memory Inspector** - Real-time memory allocation tracking and leak detection
- **Device Explorer** - Multi-device monitoring and capability inspection tool window
- **Advanced Code Analysis** - SYCL performance inspections and optimization hints
- **Kernel Occupancy Calculator** - GPU occupancy prediction and limiting factor analysis
- **Intel Advisor Integration** - Roofline analysis and vectorization recommendations
- **FPGA Optimization Analyzer** - FPGA-specific optimization and resource estimation
- **Multi-Device Orchestrator** - Intelligent workload distribution across devices
- **Auto-Tune Parameters** - Automatic work-group size and ND-range optimization
- **AI Optimization Assistant** - ML-powered code optimization suggestions
- **Live Kernel Editor** - Hot-reload kernels without application restart
- **Intel DevCloud Integration** - Cloud-based compilation and testing on real hardware
- **Performance Dashboard** - Visual metrics and historical performance tracking
- **Team Collaboration** - Code review tools and performance tracking
- **CI/CD Integration** - Automated performance testing and regression detection
- **LSP Support** - Advanced language server features for navigation and refactoring

### Technical
- Plugin successfully published to JetBrains Marketplace
- Release date: 20251116
- Built with IntelliJ Platform SDK 2023.2.5
- Supports CLion 2023.2+
- Compatible with Intel oneAPI 2024.0+

## [Unreleased]

### Planned for v2025.2.x
- Enhanced FPGA emulation support
- Improved AI optimization models
- Better DevCloud integration with job queue management
- Advanced refactoring tools (extract kernel, optimize memory access)
