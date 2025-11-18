# Release Notes - Version 2025.1.15

**Release Date**: November 18, 2025  
**Type**: Feature Enhancement

## 🎉 Major Changes

### Multi-IDE Support
Plugin now supports **all JetBrains IDEs** (not just CLion)!

- ✅ **CLion** 2023.2+
- ✅ **IntelliJ IDEA Ultimate** 2023.2+ (with C/C++ plugin)
- ✅ **PyCharm Professional** 2023.2+ (with C/C++ plugin)
- ✅ **WebStorm** 2023.2+ (with C/C++ plugin)
- ✅ **Rider** 2023.2+ (with C/C++ plugin)
- ✅ Any JetBrains IDE with C/C++ support

## 📋 What's New

### Compatibility
- **Minimum Version**: 2023.2 (build 232)
- **Maximum Version**: Unlimited (no upper bound)
- **Platform**: All JetBrains IDEs with CIDR support

### Technical Improvements
1. Removed IDE-specific restrictions
2. Optimized plugin dependencies
3. Enhanced cross-IDE compatibility
4. Updated documentation for multi-IDE usage

## 🔧 Breaking Changes
**None** - Fully backward compatible with existing CLion installations

## 📦 Installation

### From JetBrains Marketplace
1. Open your IDE → **Settings** → **Plugins**
2. Search for "Intel DPC++ Support"
3. Click **Install** and restart

### Requirements
- JetBrains IDE 2023.2+
- C/C++ language support
- Intel oneAPI Base Toolkit 2024.0+ (optional, for compilation)

## 🚀 Future Plans

- IDE-specific optimizations
- Enhanced IntelliJ IDEA integration
- Python/SYCL bindings support (PyCharm)
- .NET/SYCL interop (Rider)

## 📚 Documentation

- [Multi-IDE Support Guide](MULTI_IDE_SUPPORT.md)
- [GitHub Repository](https://github.com/jakubjirak/dpcpp-clion-plugin)
- [Report Issues](https://github.com/jakubjirak/dpcpp-clion-plugin/issues)

## 🙏 Acknowledgments

Thanks to all users who requested multi-IDE support!
