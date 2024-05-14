import numpy as np
import pandas as pd
from pycuda.compiler import SourceModule
import pycuda.autoinit
import pycuda.driver as cuda
from pycuda import gpuarray

df = pd.read_csv("data.csv")

funs = [line.strip() for line in open("functions.txt").readlines()]

# define the CUDA kernel
kernel_code_template = """
__global__ void score(float* a, float* b, float* e, int n)
{
    int tid = blockIdx.x * blockDim.x + threadIdx.x;
    if (tid < n)
    {
        float diff = a[tid] - b[tid];
        e[tid] = diff * diff;
    }
}
"""

# compile the kernel code
mod = SourceModule(kernel_code_template)

# get the kernel function from the compiled module
score = mod.get_function("score")

# convert dataframe to numpy array and then to GPU array
df_gpu = gpuarray.to_gpu(df.values.astype(np.float32))

# allocate GPU memory for the result
e_gpu = gpuarray.empty((df.shape[0],), np.float32)

min_error = None
best_fun = None

for line in funs:
    for u in ["sinf", "cosf", "tanf", "sqrtf", "expf"]:
        line = line.replace(u, f"np.{u[:-1]}")
    for c in df.columns:
        line = line.replace(f"_{c}_", f"(df[\"{c}\"].values)")
    
    # evaluate the function and copy the result to the GPU
    a = eval(line)
    a_gpu = gpuarray.to_gpu(a.astype(np.float32))
    
    # call the kernel
    score(a_gpu, df_gpu, e_gpu, np.int32(df.shape[0]), block=(256,1,1), grid=(df.shape[0]//256,1))
    
    # copy the result back to the CPU
    e = e_gpu.get()
    
    # compute the mean square error
    mse = np.mean(e)
    
    if min_error is None or mse < min_error:
        min_error = mse
        best_fun = line

print(f"Best function: {best_fun} with MSE: {min_error}")
