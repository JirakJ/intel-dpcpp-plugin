# Vector Addition Example

Simple DPC++/SYCL vector addition demonstrating basic parallel kernel execution.

## Build and Run

```bash
# Source oneAPI environment
source /opt/intel/oneapi/setvars.sh

# Build with CMake
cmake -B build -G Ninja
cmake --build build

# Run
./build/vector-add
```

## Expected Output

```
Running on: Intel(R) UHD Graphics 770
Success! Vector addition completed.
```
