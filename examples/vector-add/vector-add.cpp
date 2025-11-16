#include <sycl/sycl.hpp>
#include <iostream>
#include <vector>

int main() {
    try {
        // Create SYCL queue on default device
        sycl::queue q{sycl::default_selector{}};
        
        // Print device information
        std::cout << "Running on: " 
                  << q.get_device().get_info<sycl::info::device::name>()
                  << std::endl;
        
        // Vector size
        constexpr size_t N = 1024;
        
        // Host vectors
        std::vector<int> a(N, 1);
        std::vector<int> b(N, 2);
        std::vector<int> c(N, 0);
        
        // SYCL buffers for data management
        sycl::buffer<int> buf_a(a.data(), sycl::range<1>(N));
        sycl::buffer<int> buf_b(b.data(), sycl::range<1>(N));
        sycl::buffer<int> buf_c(c.data(), sycl::range<1>(N));
        
        // Submit command group to queue
        q.submit([&](sycl::handler& h) {
            auto acc_a = buf_a.get_access<sycl::access::mode::read>(h);
            auto acc_b = buf_b.get_access<sycl::access::mode::read>(h);
            auto acc_c = buf_c.get_access<sycl::access::mode::write>(h);
            
            h.parallel_for(sycl::range<1>(N), [=](sycl::id<1> idx) {
                acc_c[idx] = acc_a[idx] + acc_b[idx];
            });
        }).wait();
        
        // Verify results
        {
            auto host_acc = buf_c.get_host_access();
            bool success = true;
            for (size_t i = 0; i < N; i++) {
                if (host_acc[i] != 3) {
                    std::cerr << "Error at index " << i << std::endl;
                    success = false;
                    break;
                }
            }
            
            if (success) {
                std::cout << "Success! Vector addition completed." << std::endl;
            }
        }
        
    } catch (sycl::exception const& e) {
        std::cerr << "SYCL exception: " << e.what() << std::endl;
        return 1;
    }
    
    return 0;
}
