# Quick Start Guide

## 🚀 Build the Plugin in 2 Minutes

```bash
cd dpcpp-clion-plugin
./gradlew buildPlugin
```

Output: `build/distributions/dpcpp-clion-plugin-0.1.{TIMESTAMP}.zip`

## 🧪 Test in Sandbox IDE

```bash
./gradlew runIde
```

This launches CLion with the plugin pre-installed.

## 📦 Install in Your CLion

1. Build the plugin (see above)
2. Open CLion
3. `Settings/Preferences` → `Plugins`
4. Click ⚙️ → `Install Plugin from Disk...`
5. Select `build/distributions/dpcpp-clion-plugin-*.zip`
6. Restart CLion

## ✅ Verify Installation

1. Create new file: `test.dpcpp`
2. Type: `syclmain` + `Tab`
3. Should expand to complete DPC++ template
4. Check `Tools` → `DPC++` menu exists

## 🎯 Key Features to Test

### Live Templates
Type abbreviation + `Tab`:
- `syclmain` → Full main function template
- `syclqueue` → Create SYCL queue
- `syclparallel` → Parallel for kernel
- `syclbuffer` → Create buffer
- `syclaccess` → Get accessor

### Syntax Highlighting
Create file with:
```cpp
#include <sycl/sycl.hpp>

int main() {
    sycl::queue q{sycl::default_selector{}};
    q.submit([&](sycl::handler& h) {
        h.parallel_for(sycl::range<1>(1024), [=](sycl::id<1> idx) {
            // Should see highlighted keywords
        });
    });
}
```

### Actions Menu
`Tools` → `DPC++` →
- Configure oneAPI Toolchain (detects Intel oneAPI)
- Open oneAPI Code Samples (browser link)
- DPC++ Documentation (browser link)

## 📊 Version Numbering

Every build gets automatic version: `0.1.YYYYMMDDHHmm`

Example:
- Build at Jan 16, 2025 18:30 → `0.1.202501161830`
- Build at Jan 17, 2025 09:15 → `0.1.202501170915`

## 🐛 Troubleshooting

### Build Fails
```bash
# Clean and rebuild
./gradlew clean buildPlugin
```

### Java Version Error
Need Java 17+:
```bash
java -version  # Should show 17 or higher
```

### Plugin Doesn't Load in CLion
Check CLion version:
- Minimum: CLion 2023.2
- Recommended: CLion 2024.1+

## 📚 Next Steps

1. **Contribute**: See [CONTRIBUTING.md](CONTRIBUTING.md)
2. **Architecture**: See [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)
3. **Report Issues**: https://github.com/jakubjirak/dpcpp-clion-plugin/issues
4. **Documentation**: See [README.md](README.md)

## 🎓 Learn DPC++

Try example project:
```bash
cd examples/vector-add
source /opt/intel/oneapi/setvars.sh  # If oneAPI installed
cmake -B build -G Ninja
cmake --build build
./build/vector-add
```


---

**Questions?** Open [GitHub Discussion](https://github.com/jakubjirak/dpcpp-clion-plugin/discussions)
