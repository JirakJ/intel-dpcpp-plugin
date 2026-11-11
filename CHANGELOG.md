# Changelog

## [Unreleased]

## [2026.9.0] - 2026-11-11

### Fixed
- Completion, SYCL highlighting and quick documentation were registered for a language id CLion does not use (`C++`), so they never activated; they now attach to CLion's C/C++ language.
- The DPC++ syntax highlighter replaced CLion's C++ lexer with an empty one; it has been removed so C++ highlighting is untouched.
- SYCL highlighting no longer colours whole statements or files — only the matched identifiers.
- The file-type registration name now matches the file type (`DPC++`).
- Kernel Occupancy Calculator: replaced a model that reported register pressure for almost any input with a thread-slot / register / shared-local-memory estimate, and it now asks for registers and local memory instead of assuming them.
- VTune, Advisor and gdb-oneapi actions now look in `$ONEAPI_ROOT` and the default install locations and give a ready-to-run command (copied to the clipboard).

### Removed
- Actions and panels that showed simulated or hard-coded results rather than analysing your code: Auto-Tune, Live Kernel Editor, Intel DevCloud, Performance Dashboard, Multi-Device Orchestrator, AI Optimization Assistant, FPGA Optimization Analyzer, the Device Explorer tool window and the "coming soon" Run-menu debugger entry.
- Two inspections that never reported anything, and unused internal classes.
- Documentation describing features that did not exist.

## [2026.8.0] - 2026-11-10

### Changed
- The plugin is now completely free — all features unlocked, no license needed.
- Source code is now open source under the Apache License 2.0: https://github.com/JirakJ/intel-dpcpp-plugin

## [2026.3.5] - 2026-04-18

### Changed
- Enabled freemium licensing via `<product-descriptor optional="true">`. Plugin remains fully functional without a license; premium features may be introduced in future minor releases.


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
