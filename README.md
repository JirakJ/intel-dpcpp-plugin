# Intel DPC++ Support for CLion

[![License](https://img.shields.io/badge/license-Apache--2.0-blue)](LICENSE)

> **Free & open source.** Since 2026.8.0 every feature is available to everyone, no license needed, and the
> source code is available under the Apache License 2.0.

Editor helpers and Intel oneAPI tool shortcuts for writing SYCL 2020 / DPC++ code in CLion.

## Features

### Editor

- **File type** — `.dpcpp` and `.dp.cpp` are recognised and handled as C++.
- **Completion** — entries for common SYCL APIs (queues and selectors, buffers and accessors, USM allocation,
  `range` / `nd_range`, `parallel_for`, `single_task`, device info queries).
- **Highlighting** — `sycl::` identifiers and kernel launches (`parallel_for`, `parallel_for_work_group`,
  `single_task`) are highlighted in C/C++ files.
- **Quick documentation** — for `sycl::queue` and `parallel_for`, with links to the SYCL 2020 specification.
- **Live templates** — `syclqueue`, `syclbuffer`, `syclsubmit`, `syclparallel`, `syclaccess`, `syclmain`, `syclusm`.

### Tools → DPC++

| Action | What it does |
|---|---|
| New DPC++ Project | Shows how to configure a CMake project for `icpx -fsycl` |
| Configure oneAPI Toolchain | Detects oneAPI installations containing `icpx` (`$ONEAPI_ROOT` first, then the default install locations) |
| Debug with gdb-oneapi | Locates `gdb-oneapi` and gives you the command line (copied to the clipboard) |
| Profile with VTune | Locates VTune and builds the `gpu-hotspots` / `gpu-offload` / `memory-access` collection command |
| Intel Advisor Analysis | Locates Advisor and builds the `roofline` / `offload` collection command |
| Kernel Occupancy Calculator | First-order occupancy estimate for Intel Xe GPUs from work-group size, registers per thread and local memory |
| DPC++ Documentation / oneAPI Code Samples | Opens the Intel documentation / the oneAPI samples repository |

The tool actions do not launch the tools themselves — they find them and hand you a ready-to-run command for a
terminal in which `setvars.sh` has been sourced.

## Requirements

- CLion 2023.2 or later
- Intel oneAPI Base Toolkit (DPC++ compiler, and VTune / Advisor / gdb-oneapi for the tool actions)

## Installation

- **JetBrains Marketplace**: Settings → Plugins → Marketplace → search "Intel DPC++ Support".
- **Manual**: download the ZIP from [Releases](https://github.com/JirakJ/intel-dpcpp-plugin/releases) →
  Settings → Plugins → ⚙️ → Install Plugin from Disk.

## Examples

`examples/` contains small CMake projects (`vector-add`, `debugging-example`, `profiling-example`) to try the
editor features and the tool commands on.

## Development

```bash
./gradlew buildPlugin     # plugin ZIP in build/distributions/
./gradlew runIde          # sandbox CLion with the plugin installed
./gradlew detekt test     # static analysis + unit tests
./gradlew verifyPlugin    # JetBrains Plugin Verifier (CLion 2023.2.5 and 2024.3.6)
```

CI runs the same gates on GitHub-hosted runners for every pull request.

## Resources

- [Intel DPC++ Developer Guide](https://www.intel.com/content/www/us/en/docs/dpcpp-cpp-compiler/developer-guide-reference/current/overview.html)
- [SYCL 2020 Specification](https://registry.khronos.org/SYCL/specs/sycl-2020/html/sycl-2020.html)
- [oneAPI Code Samples](https://github.com/oneapi-src/oneAPI-samples)

## Contributing

Issues and pull requests are welcome at https://github.com/JirakJ/intel-dpcpp-plugin.

## License

Copyright 2026 Jakub Jirák. Licensed under the [Apache License, Version 2.0](LICENSE).
