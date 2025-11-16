# Plugin Architecture

## Version Scheme

**Format**: `MAJOR.MINOR.BUILD_NUMBER`

- **MAJOR**: 0 = Beta, 1+ = Production
- **MINOR**: Feature milestone
- **BUILD_NUMBER**: Auto-generated timestamp `YYYYMMDDHHmm`

Example: `0.1.202501161830`

### Build Number Generation
```kotlin
val buildNumber = System.getenv("BUILD_NUMBER") ?: LocalDateTime.now()
    .format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
version = "0.1.$buildNumber"
```

## Component Overview

### File Type Support
- **DpcppFileType**: Registers `.dpcpp`, `.dp.cpp` extensions
- Reuses CLion's C++ language infrastructure

### Syntax Highlighting
- **DpcppSyntaxHighlighter**: Lexer-based keyword highlighting
- **DpcppAnnotator**: Semantic highlighting for SYCL constructs
- **DpcppColorSettingsPage**: User color customization

### Code Intelligence
- **DpcppCompletionContributor**: SYCL API autocomplete
- **DpcppDocumentationProvider**: Hover documentation
- **Live Templates**: Code snippets

### Build Integration
- **DpcppCMakeConfigurable**: CMake settings UI
- **ConfigureToolchainAction**: Auto-detect oneAPI installation

### Actions
- New project creation
- Sample browser
- Documentation links

## Development Phases

### v0.1.x - Foundation (Current)
- Basic editing experience
- Syntax highlighting
- Code completion

### v0.2.x - Debugging (Q2 2025)
- gdb-oneapi integration
- GPU thread visualization

### v0.3.x - Performance (Q3 2025)
- VTune integration
- Kernel analysis

### v1.0.0 - Production (Q4 2025)
- Full PSI implementation
- Enterprise features
