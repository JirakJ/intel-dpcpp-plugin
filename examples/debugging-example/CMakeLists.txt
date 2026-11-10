cmake_minimum_required(VERSION 3.20)
project(debugging-example LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Enable debug symbols
set(CMAKE_BUILD_TYPE Debug)
set(CMAKE_CXX_FLAGS_DEBUG "-g -O0")

# Find Intel DPC++ compiler
find_program(DPCPP_COMPILER icpx REQUIRED)
set(CMAKE_CXX_COMPILER ${DPCPP_COMPILER})

# SYCL flags
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fsycl")

add_executable(debugging-example main.dpcpp)

# Instructions
message(STATUS "===========================================")
message(STATUS "Debugging Example")
message(STATUS "===========================================")
message(STATUS "Build: cmake -B build && cmake --build build")
message(STATUS "Run:   ./build/debugging-example")
message(STATUS "")
message(STATUS "Debugging in CLion:")
message(STATUS "  1. Set breakpoint inside parallel_for lambda")
message(STATUS "  2. Tools → DPC++ → Debug with gdb-oneapi")
message(STATUS "  3. Inspect GPU threads")
message(STATUS "  4. Find and fix the off-by-one bug")
message(STATUS "===========================================")
