# Contributing to Intel DPC++ CLion Plugin

Thank you for your interest in contributing! This project follows the Apache 2.0 license and welcomes community contributions.

## Development Setup

### Prerequisites
- Java 17+
- Gradle 8.5+
- CLion 2023.2+ (optional, for testing)
- Git

### Clone and Build
```bash
git clone https://github.com/jakubjirak/dpcpp-clion-plugin.git
cd dpcpp-clion-plugin
./gradlew buildPlugin
```

### Run Plugin in Test IDE
```bash
./gradlew runIde
```
This launches a sandbox CLion instance with the plugin installed.

### Run Tests
```bash
./gradlew test
```

## Code Structure

```
src/main/
├── java/com/intel/dpcpp/
│   ├── DpcppFileType.java              # File type registration
│   ├── highlighting/                    # Syntax highlighting
│   │   ├── DpcppSyntaxHighlighter.java
│   │   ├── DpcppAnnotator.java
│   │   └── DpcppColorSettingsPage.java
│   ├── completion/                      # Code completion
│   │   └── DpcppCompletionContributor.java
│   ├── actions/                         # Menu actions
│   ├── cmake/                           # CMake integration
│   ├── project/                         # Project templates
│   └── documentation/                   # Documentation provider
└── resources/
    ├── META-INF/plugin.xml              # Plugin configuration
    ├── icons/                           # SVG icons
    └── liveTemplates/                   # Code snippets
```

## Contribution Guidelines

### Reporting Issues
- Use [GitHub Issues](https://github.com/jakubjirak/dpcpp-clion-plugin/issues)
- Include CLion version, plugin version, oneAPI version
- Provide minimal reproducible example
- Attach relevant logs from `Help → Show Log in Finder/Explorer`

### Feature Requests
- Open a [Discussion](https://github.com/jakubjirak/dpcpp-clion-plugin/discussions)
- Describe use case and expected behavior
- Consider Intel partnership opportunities for major features

### Pull Requests
1. **Fork** the repository
2. **Create branch**: `git checkout -b feature/your-feature-name`
3. **Implement** changes with tests
4. **Test**: `./gradlew test verifyPlugin`
5. **Commit**: Follow [Conventional Commits](https://www.conventionalcommits.org/)
   ```
   feat: add VTune integration
   fix: correct SYCL keyword highlighting
   docs: update installation instructions
   ```
6. **Push**: `git push origin feature/your-feature-name`
7. **Pull Request**: Include description, motivation, testing done

### Code Style
- Java: Follow [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- Use IntelliJ IDEA default formatter
- 4 spaces indentation, no tabs
- Maximum line length: 120 characters

### Testing
- Unit tests for all new functionality
- Integration tests for IDE interactions
- Manual testing in sandbox IDE (`./gradlew runIde`)

## Areas for Contribution

### High Priority
- **Debugger Integration** (gdb-oneapi support)
- **CMake Auto-configuration** (detect oneAPI, set flags)
- **PSI Implementation** (proper AST parsing for SYCL)
- **Code Inspections** (detect common DPC++ mistakes)

### Medium Priority
- **VTune Integration** (launch profiler from IDE)
- **Refactoring Support** (rename SYCL entities)
- **Find Usages** (track buffer/queue usage)
- **Performance Hints** (suggest optimizations)

### Documentation
- Tutorial videos
- Blog posts
- Example projects
- API documentation

## Release Process

Automated via GitHub Actions:

1. **Commit to `develop`** → Build and test
2. **Merge to `main`** → Build, test, and publish to JetBrains Marketplace
3. **Version**: Auto-incremented as `0.MINOR.YYYYMMDDHHmm`

Manual release:
```bash
export BUILD_NUMBER=$(date +%Y%m%d%H%M)
./gradlew buildPlugin publishPlugin
```

## Community

- **Discussions**: [GitHub Discussions](https://github.com/jakubjirak/dpcpp-clion-plugin/discussions)
- **Intel Forums**: [oneAPI Community](https://community.intel.com/t5/Intel-oneAPI-Toolkits/ct-p/oneapi)
- **Email**: jakub.jirak@pm.me (for security issues)

## License

By contributing, you agree that your contributions will be licensed under the Apache License 2.0.

## Code of Conduct

This project follows the [Contributor Covenant Code of Conduct](https://www.contributor-covenant.org/version/2/1/code_of_conduct/).

---

**Questions?** Open a [Discussion](https://github.com/jakubjirak/dpcpp-clion-plugin/discussions) or ping maintainers in Issues.
