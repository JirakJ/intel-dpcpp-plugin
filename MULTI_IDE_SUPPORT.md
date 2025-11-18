# Multi-IDE Support - Version 2025.1.15

## Overview
Starting with version 2025.1.15, Intel DPC++ Support plugin now works with **all JetBrains IDEs** that have C/C++ support, not just CLion.

## Supported IDEs

The plugin is now compatible with:
- **CLion** 2023.2+ (full support)
- **IntelliJ IDEA Ultimate** 2023.2+ (with C/C++ plugin)
- **PyCharm Professional** 2023.2+ (with C/C++ plugin)
- **WebStorm** 2023.2+ (with C/C++ plugin)
- **Rider** 2023.2+ (with C/C++ plugin)
- Any other JetBrains IDE with C/C++ language support

## Compatibility

- **Minimum IDE Version**: 2023.2 (build 232)
- **Maximum IDE Version**: Unlimited (no upper bound)
- **Required**: C/C++ language support (CIDR)

## What Changed

### Technical Changes
1. **Build Configuration**
   - Changed from CLion-specific (`type="CL"`) to generic platform
   - Removed explicit CLion plugin dependency
   - Kept only essential CIDR base modules

2. **Plugin Dependencies**
   - `com.intellij.modules.platform` (required)
   - `com.intellij.cidr.lang` (required for C/C++ support)
   - CLion-specific features are now optional

3. **Plugin Description**
   - Updated title from "Support for CLion" to "Support for JetBrains IDEs"
   - Updated requirements to mention all compatible IDEs

## Features by IDE

### CLion (Full Support)
✅ All features including CMake integration
✅ Native debugging support
✅ Project templates
✅ Full toolchain integration

### IntelliJ IDEA Ultimate + C/C++ Plugin
✅ Syntax highlighting
✅ Code completion
✅ Live templates
✅ Documentation
⚠️ Limited CMake integration (requires C/C++ plugin configuration)

### Other IDEs with C/C++ Support
✅ Syntax highlighting
✅ Code completion
✅ Live templates
✅ Documentation
⚠️ Limited project management features

## Installation

### From JetBrains Marketplace
1. Open your JetBrains IDE
2. Go to **Settings/Preferences** → **Plugins**
3. Search for "Intel DPC++ Support"
4. Click **Install**
5. Restart IDE

### Requirements
- JetBrains IDE 2023.2 or later
- C/C++ language support (built-in for CLion, plugin for others)
- Intel oneAPI Base Toolkit 2024.0+ (for compilation)

## Migration Notes

If you were using the plugin with CLion before version 2025.1.15:
- ✅ No action required
- ✅ All existing features continue to work
- ✅ Plugin will update automatically via JetBrains Marketplace

## Publishing

- **Published**: November 18, 2025
- **Version**: 2025.1.15
- **Marketplace**: https://plugins.jetbrains.com/plugin/com.jakubjirak.dpcpp

## Future Enhancements

We plan to add IDE-specific optimizations:
- IntelliJ IDEA: Better integration with Java/Kotlin projects using SYCL interop
- PyCharm: Python bindings support for SYCL
- Rider: .NET interop with SYCL

## Support

- **Issues**: https://github.com/jakubjirak/dpcpp-clion-plugin/issues
- **Documentation**: https://github.com/jakubjirak/dpcpp-clion-plugin
- **Intel DPC++ Documentation**: https://www.intel.com/content/www/us/en/docs/dpcpp-cpp-compiler/
