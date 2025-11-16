# Intel DPC++ CLion Plugin

**Comprehensive Intel oneAPI DPC++/SYCL development support for JetBrains CLion IDE.**

[![Version](https://img.shields.io/badge/version-0.1.x-blue.svg)](https://github.com/jakubjirak/dpcpp-clion-plugin)
[![License](https://img.shields.io/badge/license-Apache%202.0-green.svg)](LICENSE)
[![oneAPI](https://img.shields.io/badge/Intel-oneAPI-0071C5.svg)](https://www.intel.com/content/www/us/en/developer/tools/oneapi/overview.html)

## Features

### Free (Open Source)
- ✅ **SYCL 2020 Syntax Highlighting** - Keywords, parallel constructs, device selectors
- ✅ **File Type Support** - `.dpcpp`, `.dp.cpp` file recognition  
- ✅ **Code Completion** - SYCL API autocomplete (sycl::queue, sycl::buffer, etc.)
- ✅ **Live Templates** - Quick code snippets for common DPC++ patterns
- ✅ **CMake Integration** - oneAPI compiler detection
- ✅ **Documentation on Hover** - Inline API documentation
- ✅ **Project Templates** - Quick-start DPC++ project creation

### Premium (Coming Soon)
- 🚧 **Advanced Debugging** - gdb-oneapi integration with GPU thread visualization
- 🚧 **Performance Analysis** - Intel VTune Profiler integration
- 🚧 **Kernel Analysis** - Occupancy prediction, memory bandwidth estimation
- 🚧 **Intel Advisor Integration** - Roofline analysis and optimization suggestions

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

## Debugging (v0.2 Roadmap)

Currently use command-line debugger:
```bash
source /opt/intel/oneapi/setvars.sh
gdb-oneapi ./build/main

# GPU debugging commands
(gdb) info sycl devices      # List GPUs
(gdb) info sycl kernels      # List loaded kernels
(gdb) thread 2:4            # Switch to thread 2, SIMD lane 4
```

## Documentation

- [Intel DPC++ Developer Guide](https://www.intel.com/content/www/us/en/docs/dpcpp-cpp-compiler/developer-guide-reference/current/overview.html)
- [SYCL 2020 Specification](https://registry.khronos.org/SYCL/specs/sycl-2020/html/sycl-2020.html)
- [oneAPI Code Samples](https://github.com/oneapi-src/oneAPI-samples)
- [Plugin Issue Tracker](https://github.com/jakubjirak/dpcpp-clion-plugin/issues)

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

### Phase 2 (v0.2.x - Q2 2025)
- gdb-oneapi debugger integration
- GPU thread visualization
- VTune Profiler launch integration
- Enhanced CMake configuration UI

### Phase 3 (v0.3.x - Q3 2025)
- Kernel occupancy analysis
- Memory bandwidth prediction
- Intel Advisor integration
- Roofline analysis visualization

### Phase 4 (v1.0.0 - Q4 2025)
- Full heterogeneous debugging workflow
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
