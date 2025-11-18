cmake_minimum_required(VERSION 3.20)
project(profiling-example LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Find Intel DPC++ compiler
find_program(DPCPP_COMPILER icpx REQUIRED)
set(CMAKE_CXX_COMPILER ${DPCPP_COMPILER})

# SYCL flags with optimization
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fsycl -O2")

add_executable(profiling-example main.dpcpp)

message(STATUS "===========================================")
message(STATUS "Profiling Example")
message(STATUS "===========================================")
message(STATUS "Build: cmake -B build && cmake --build build")
message(STATUS "Run:   ./build/profiling-example")
message(STATUS "")
message(STATUS "Profiling in CLion (Premium):")
message(STATUS "  1. Tools → DPC++ → Profile with VTune")
message(STATUS "  2. Select 'GPU Hotspots'")
message(STATUS "  3. Compare v1 (unoptimized) vs v2 (optimized)")
message(STATUS "  4. Analyze memory bandwidth")
message(STATUS "")
message(STATUS "Command-line VTune:")
message(STATUS "  vtune -collect gpu-hotspots -result-dir vtune_results -- ./build/profiling-example")
message(STATUS "  vtune-gui vtune_results")
message(STATUS "===========================================")
