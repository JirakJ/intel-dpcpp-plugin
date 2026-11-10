# JetBrains Marketplace Listing — Intel DPC++ Support

---

## 📝 Plugin Information

| Field       | Value                          |
|-------------|--------------------------------|
| **Name**    | Intel DPC++ Support            |
| **Plugin ID** | com.jakubjirak.dpcpp         |
| **Version** | 2025.1.19                      |
| **Category** | Languages & Frameworks         |
| **Tags**    | SYCL, DPC++, Intel oneAPI, heterogeneous computing, GPU, FPGA |

---

## 🎯 Short Description

> Develop for CPUs, GPUs, and FPGAs with a single codebase. SYCL 2020 and DPC++ support for CLion — intelligent code completion, GPU debugging, VTune profiling, and device memory analysis.

*(198 characters)*

---

## 📖 Full Description

### 🚀 One Codebase. Every Device. — Full SYCL 2020 & DPC++ Support for CLion

**The Problem:** Heterogeneous computing development in C++ is painful. SYCL and DPC++ developers lack proper IDE support — no syntax highlighting, no intelligent completion for SYCL APIs, no integrated GPU debugging, and no way to profile kernels without leaving the IDE.

**The Solution:** Intel DPC++ Support brings first-class SYCL 2020 and DPC++ development to CLion. From intelligent code completion for `sycl::queue`, `sycl::buffer`, and USM APIs to integrated GPU debugging via `gdb-oneapi` and Intel VTune Profiler — everything you need to develop for CPUs, GPUs, and FPGAs in one place.

---

### ✨ Key Features

#### 🎨 SYCL 2020 Language Support (Free)
- **Syntax highlighting** for SYCL 2020 and DPC++ extensions
- **File type recognition** for `.dpcpp` and `.dp.cpp` files
- **Code completion** for SYCL APIs: `sycl::queue`, `sycl::buffer`, `sycl::handler`, USM, and more
- **Semantic highlighting** with customizable color settings
- **Documentation on hover** for SYCL APIs

#### 📝 Live Templates (Free)
- 6 built-in templates: `syclqueue`, `syclbuffer`, `syclsubmit`, `syclparallel`, `syclaccess`, `syclusm`
- Accelerate SYCL boilerplate with tab-completion

#### 🔧 CMake & Toolchain Integration (Free)
- Automatic Intel oneAPI toolchain detection
- CMake integration for DPC++ projects
- Project templates for common DPC++ patterns

#### 🖥️ Device Explorer (Free)
- Browse available compute devices (CPUs, GPUs, FPGAs)
- Device details and capabilities at a glance

#### 🐛 GPU Debugging
- **gdb-oneapi** integration for GPU kernel debugging
- GPU thread visualization and inspector
- Kernel breakpoints and conditional breakpoints
- Device memory viewer

#### 📊 Intel VTune Profiler Integration
- 4 analysis types: GPU hotspots, GPU offload, kernel performance metrics, memory bandwidth
- Real-time device monitoring tool window
- Performance visualization within the IDE

#### 🔍 Advanced Code Inspections
- Race condition detection in SYCL kernels
- USM best practice checks
- Kernel occupancy analysis
- Memory bandwidth predictor

#### ♻️ SYCL Refactorings
- **Buffer → USM** migration assistant
- **Extract Kernel** refactoring
- **Optimize Work-Groups** transformation

#### 🏭 FPGA Optimization
- Resource estimation and pragma suggestions
- Pipeline analysis for FPGA targets
- Multi-device orchestrator with intelligent workload distribution

#### 🤖 AI-Powered Optimization
- Optimization suggestions with 70–90% confidence
- Live kernel editor with hot-reload (~100ms)
- Intel DevCloud integration (7 device types: GPU Max, Arc, FPGA, and more)

#### 🤝 Team
- Team collaboration with shared performance baselines
- Regression tracking across builds
- CI/CD automation and performance testing

---

### 💰 Pricing

The Intel DPC++ plugin is **completely free and open source** (Apache License 2.0).
Every feature is available to everyone — no license or subscription needed.

---

### 🎯 Perfect For

- **C++ developers** targeting heterogeneous hardware (CPU + GPU + FPGA)
- **Intel oneAPI users** who want IDE-native tooling
- **HPC researchers** running parallel workloads across devices
- **FPGA developers** needing optimization analysis and resource estimation
- **Academic institutions** teaching parallel and heterogeneous computing (50% discount)

---

### ⚙️ Requirements

| Requirement            | Value                          |
|------------------------|--------------------------------|
| **IDE**                | CLion 2023.2+ (build 232+)    |
| **Java Version**       | 17+                           |
| **Intel oneAPI Toolkit** | 2024.0+ (with DPC++ compiler) |
| **CMake**              | 3.20+                         |
| **OS**                 | Linux, Windows, macOS          |

---

### 🚀 Quick Start

1. **Install** Intel oneAPI Base Toolkit (2024.0+)
2. **Install** the plugin from JetBrains Marketplace
3. **Create** a new DPC++ project using the built-in templates
4. Start coding with **intelligent SYCL code completion** — try typing `syclqueue` + Tab
5. Open the **Device Explorer** to see available compute targets
6. Build and run with the oneAPI CMake toolchain

---

## 📸 Screenshots & Media

| # | Screenshot Description                                                        |
|---|------------------------------------------------------------------------------|
| 1 | **SYCL Syntax Highlighting** — Rich coloring for SYCL 2020 code with semantic annotations |
| 2 | **Code Completion** — Intelligent suggestions for `sycl::queue`, `sycl::buffer`, and USM APIs |
| 3 | **Live Templates** — Tab-expanding `syclsubmit` into full kernel submission boilerplate |
| 4 | **Device Explorer** — Tool window listing available CPUs, GPUs, and FPGAs with capabilities |
| 5 | **GPU Debugging (Pro)** — gdb-oneapi debugger with kernel breakpoint and thread inspector |
| 6 | **VTune Profiling (Pro)** — GPU hotspots analysis results with kernel performance metrics |
| 7 | **FPGA Optimization (Pro)** — Resource estimation panel with pipeline analysis and pragma suggestions |
| 8 | **AI Optimization (Pro)** — Suggestion panel with confidence scores and live kernel hot-reload |

---

## 🏷️ Marketplace Categories

| Type          | Category                         |
|---------------|----------------------------------|
| **Primary**   | Languages & Frameworks           |
| **Secondary** | Profiling & Debugging, Education |

---

## 🔍 Keywords (SEO)

`SYCL`, `DPC++`, `Intel oneAPI`, `heterogeneous computing`, `GPU programming`, `FPGA`, `gdb-oneapi`, `VTune`, `SYCL 2020`, `parallel computing`, `CLion plugin`, `GPU debugging`, `kernel profiling`, `USM`, `unified shared memory`, `compute offload`, `Intel GPU`, `oneAPI toolkit`, `C++ GPU`

---

## 📋 Compatibility

| IDE               | Compatible |
|-------------------|------------|
| CLion             | ✅ (Primary) |
| IntelliJ IDEA     | ⚠️ (C/C++ plugin required) |

**Minimum Version:** CLion 2023.2+ (build 232)

---

## 📞 Vendor Information

| Field        | Value                          |
|--------------|--------------------------------|
| **Vendor**   | Jakub Jirak                    |
| **Email**    | jirak@jakubjirak.com           |
| **Website**  | https://jakubjirak.com         |
| **Support**  | support@jakubjirak.com         |

---

## ✅ Pre-Launch Checklist

- [ ] Plugin icon (SVG, 40×40px) — designed and included
- [ ] Plugin description finalized (< 10,000 characters)
- [ ] Short description finalized (< 255 characters)
- [ ] Screenshots captured (minimum 5, 1280×800px recommended)
- [ ] Plugin signed with JetBrains Marketplace certificate
- [ ] Changelog written for initial release
- [ ] Vendor verified on JetBrains Marketplace
- [ ] Compatibility tested with CLion 2023.2+
- [ ] Intel oneAPI Base Toolkit compatibility verified
- [ ] Support URL / email configured
- [ ] README and documentation published
- [ ] Landing page live at vendor website
- [ ] Social media announcements prepared
