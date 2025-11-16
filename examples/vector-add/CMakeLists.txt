cmake_minimum_required(VERSION 3.20)
project(VectorAddDpcpp LANGUAGES CXX)

# Intel DPC++ Compiler Configuration
set(CMAKE_CXX_COMPILER icpx)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# DPC++/SYCL flags
add_compile_options(-fsycl)
add_link_options(-fsycl)

# Optional: Ahead-of-Time compilation for Intel GPU
# add_compile_options(-fsycl-targets=spir64_gen -Xsycl-target-backend "-device gen9")

add_executable(vector-add vector-add.cpp)

# Optional: Link oneAPI libraries
# find_package(oneDPL REQUIRED)
# target_link_libraries(vector-add PRIVATE oneDPL)
