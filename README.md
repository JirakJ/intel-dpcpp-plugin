# Intel DPC++ CLion Plugin

**Comprehensive Intel oneAPI DPC++/SYCL development support for JetBrains CLion IDE.**

[![Version](https://img.shields.io/badge/version-0.1.x-blue.svg)](https://github.com/jakubjirak/dpcpp-clion-plugin)
[![License](https://img.shields.io/badge/license-Apache%202.0-green.svg)](LICENSE)
[![oneAPI](https://img.shields.io/badge/Intel-oneAPI-0071C5.svg)](https://www.intel.com/content/www/us/en/developer/tools/oneapi/overview.html)

## Features

- ✅ **SYCL 2020 Syntax Highlighting** - Keywords, parallel constructs, device selectors
- ✅ **File Type Support** - `.dpcpp`, `.dp.cpp` file recognition  
- ✅ **Code Completion** - SYCL API autocomplete (sycl::queue, sycl::buffer, etc.)
- ✅ **Live Templates** - Quick code snippets for common DPC++ patterns
- ✅ **CMake Integration** - oneAPI compiler detection
- ✅ **Documentation on Hover** - Inline API documentation
- ✅ **Project Templates** - Quick-start DPC++ project creation
- ✅ **GPU Debugging** - gdb-oneapi integration with GPU thread visualization
- ✅ **Performance Profiling** - Intel VTune Profiler integration
- ✅ **Device Explorer** - Real-time device monitoring tool window
- ✅ **Advanced Code Analysis** - SYCL quality and performance inspections
- ✅ **Kernel Analysis** - Occupancy calculator & bandwidth estimator
- ✅ **Intel Advisor** - Roofline analysis and offload modeling
- ✅ **FPGA Optimization** - Resource estimation, pragma suggestions, pipeline analysis
- ✅ **Multi-Device Orchestrator** - Intelligent workload distribution across devices
- ✅ **AI Optimization** - Pattern-based suggestions
- ✅ **Live Editing** - Hot-reload kernels without restart
- ✅ **Performance Dashboard** - Real-time performance visualization & insights

## Requirements

- **Intel oneAPI Base Toolkit** 2024.0+ (with DPC++ compiler)
- **CLion** 2023.2 or later
- **CMake** 3.20+
- **Operating Systems**: Linux, Windows, macOS

## Installation

### From JetBrains Marketplace
1. Open CLion
2. Go to `Settings/Preferences` → `Plugins`
3. Search for "Intel DPC++"
4. Click `Install`
5. Restart CLion

### Manual Installation
1. Download the latest `.zip` from [Releases](https://github.com/jakubjirak/dpcpp-clion-plugin/releases)
2. `Settings/Preferences` → `Plugins` → `⚙️` → `Install Plugin from Disk`
3. Select the downloaded `.zip` file
4. Restart CLion

## Quick Start

### 1. Install Intel oneAPI
```bash
# Linux/macOS
wget https://registrationcenter-download.intel.com/akdlm/IRC_NAS/...
sudo sh ./l_BaseKit_...sh

# Windows - Download installer from Intel website
```

### 2. Configure Toolchain
```
Tools → DPC++ → Configure oneAPI Toolchain
```
Plugin will auto-detect oneAPI installation.

### 3. Create DPC++ Project

**Option A: Manual CMake Configuration**
```cmake
cmake_minimum_required(VERSION 3.20)
project(MyDpcppProject LANGUAGES CXX)

set(CMAKE_CXX_COMPILER icpx)
set(CMAKE_CXX_STANDARD 17)

add_executable(main main.cpp)
target_compile_options(main PRIVATE -fsycl)
target_link_options(main PRIVATE -fsycl)
```

**Option B: Use Live Templates**
Type `syclmain` and press `Tab` for instant DPC++ skeleton:
```cpp
#include <sycl/sycl.hpp>
#include <iostream>

int main() {
    try {
        sycl::queue q{sycl::default_selector{}};
        std::cout << "Running on: " 
                  << q.get_device().get_info<sycl::info::device::name>()
                  << std::endl;
        
        // Your DPC++ code here
        
    } catch (sycl::exception const& e) {
        std::cerr << "SYCL exception: " << e.what() << std::endl;
        return 1;
    }
    return 0;
}
```

### 4. Build and Run
```bash
source /opt/intel/oneapi/setvars.sh  # Linux/macOS
cmake -B build -G Ninja
cmake --build build
./build/main
```

## Live Templates

Speed up development with built-in code snippets:

| Abbreviation | Description | Expands To |
|-------------|-------------|------------|
| `syclqueue` | Create SYCL queue | `sycl::queue q{sycl::default_selector{}};` |
| `syclbuffer` | Create SYCL buffer | `sycl::buffer<int> buf(N);` |
| `syclsubmit` | Submit command group | `q.submit([&](sycl::handler& h) { ... });` |
| `syclparallel` | Parallel for kernel | `h.parallel_for(range<1>(N), [=](id<1> idx) { ... });` |
| `syclaccess` | Get buffer accessor | `auto acc = buf.get_access<access::mode::read_write>(h);` |
| `syclusm` | USM allocation | `auto ptr = malloc_device<int>(N, q);` |

## Code Completion

IntelliSense for SYCL APIs:
- Type `sycl::` → See all SYCL namespace entities
- Type `.submit` → Auto-complete queue operations
- Type `parallel_for` → Get parameter hints

## GPU Debugging

With Professional Edition, debug SYCL kernels directly in CLion:

**Tools → DPC++ → Debug with gdb-oneapi**

Features:
- Set breakpoints in kernels
- Inspect GPU thread state
- View device memory
- Step through kernel execution

📖 **[GPU Debugging Tutorial →](docs/tutorials/gpu-debugging.md)**

### Command-line (Community)
```bash
source /opt/intel/oneapi/setvars.sh
gdb-oneapi ./build/main

# GPU debugging commands
(gdb) info sycl devices      # List GPUs
(gdb) info sycl kernels      # List loaded kernels
(gdb) thread 2:4            # Switch to thread 2, SIMD lane 4
```

## Performance Profiling

Optimize with Intel VTune Profiler integration:

**Tools → DPC++ → Profile with VTune**

Analysis types:
- GPU Hotspots - Find performance bottlenecks
- GPU Offload - Track host-device transfers
- Memory Analysis - Bandwidth utilization

📖 **[VTune Profiling Guide →](docs/tutorials/vtune-profiling.md)**

## Documentation

### Plugin Documentation
- 📖 [Premium Features Guide](docs/PREMIUM_FEATURES.md)
- 🏗️ [Architecture](docs/ARCHITECTURE.md)
- 🤝 [Contributing Guide](CONTRIBUTING.md)

### Intel Resources
- [Intel DPC++ Developer Guide](https://www.intel.com/content/www/us/en/docs/dpcpp-cpp-compiler/developer-guide-reference/current/overview.html)
- [SYCL 2020 Specification](https://registry.khronos.org/SYCL/specs/sycl-2020/html/sycl-2020.html)
- [oneAPI Code Samples](https://github.com/oneapi-src/oneAPI-samples)

### Support
- [GitHub Issues](https://github.com/jakubjirak/dpcpp-clion-plugin/issues) - Bug reports
- [GitHub Discussions](https://github.com/jakubjirak/dpcpp-clion-plugin/discussions) - Questions
- Email: jakub.jirak@pm.me

## Contributing

Contributions welcome! This is an **Apache 2.0 open-source project**.

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## Development

### Build Plugin
```bash
./gradlew buildPlugin
# Output: build/distributions/dpcpp-clion-plugin-*.zip
```

### Run Plugin in Test IDE
```bash
./gradlew runIde
```

### Run Tests
```bash
./gradlew test
```

## Roadmap

### Phase 1 (v0.1.x - Current) ✅
- Basic syntax highlighting
- File type support
- CMake integration
- Code completion
- Live templates

### Phase 2 (v0.2.x) ✅
- gdb-oneapi debugger integration
- GPU thread visualization
- VTune Profiler integration
- Device Explorer tool window
- Advanced code inspections

### Phase 3 (v0.3.x) ✅
- Kernel occupancy analysis
- Memory bandwidth predictor
- Intel Advisor integration
- SYCL refactorings (3 types)

### Phase 4 (v0.4.x) ✅
- FPGA optimization analyzer
- Multi-device orchestrator
- Auto-tuning framework

### Phase 5 (v0.5.x) ✅
- AI-powered optimization assistant
- Live kernel editing with hot-reload
- Intel DevCloud integration
- Performance dashboard

### Phase 6 (v1.0.0 - Current) ✅
- Language Server Protocol (LSP)
- Team collaboration features
- Enterprise license management
- CI/CD integration
- Advanced analytics

### Future (v1.1+)
- Enhanced AI models
- More device support
- Performance optimization suggestions
- Team collaboration features
- Enterprise support options

## Support

- **Issues**: [GitHub Issues](https://github.com/jakubjirak/dpcpp-clion-plugin/issues)
- **Discussions**: [GitHub Discussions](https://github.com/jakubjirak/dpcpp-clion-plugin/discussions)
- **Intel Forums**: [oneAPI Community](https://community.intel.com/t5/Intel-oneAPI-Toolkits/ct-p/oneapi)

## License

Copyright © 2025 Intel Corporation

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## Acknowledgments

- **Intel oneAPI Team** - For DPC++/SYCL compiler and tools
- **JetBrains** - For CLion IDE and Plugin SDK
- **Khronos Group** - For SYCL specification
- **Open Source Community** - For feedback and contributions

---

**Made with ❤️ for heterogeneous computing developers**
# intel-dpcpp-plugin
