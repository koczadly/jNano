/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import org.jocl.*;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;
import uk.oczadly.karl.jnano.util.workgen.policy.WorkDifficultyPolicy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.jocl.CL.*;

/**
 * This {@code WorkGenerator} can generate work on the system's CPU or GPU using the OpenCL API.
 *
 * <p>This method may be unavailable on some computers without a compatible OpenCL device or supported operating
 * system platform.</p>
 *
 * <p>Instances of this class should be re-used throughout your application, as each instance will spawn new
 * background threads. This practice also ensures that tasks are queued correctly in the order of request.</p>
 */
public final class OpenCLWorkGenerator extends AbstractWorkGenerator {
    
    private static final int DEFAULT_THREADS = 1024 * 1024;
    private static final Random RANDOM = new Random(); // Used for work initialization
    
    private final int platformId, deviceId, threadCount;
    private volatile String deviceName;
    
    // OpenCL buffers and params
    private final ByteBuffer attemptBuf, resultBuf;
    private final Pointer attemptPtr, resultPtr;
    private volatile cl_command_queue clQueue;
    private volatile cl_kernel clKernel;
    private volatile cl_mem clMemAttempt, clMemRoot, clMemDifficulty, clMemResult;
    
    /**
     * Constructs an {@code OpenCLWorkGenerator} with the default Nano difficulty policy using {@value #DEFAULT_THREADS}
     * work threads on the default OpenCL device (platform {@code 0}, device {@code 0}).
     *
     * @throws OpenCLInitializerException if no default device cannot be found, or OpenCL isn't supported
     */
    public OpenCLWorkGenerator() throws OpenCLInitializerException {
        this(0, 0);
    }
    
    /**
     * Constructs an {@code OpenCLWorkGenerator} with the default Nano difficulty policy using {@value #DEFAULT_THREADS}
     * work threads on the specified OpenCL device.
     *
     * @param platformId the OpenCL platform ID/index
     * @param deviceId   the OpenCL device ID/index
     *
     * @throws OpenCLInitializerException if the device cannot be found, or OpenCL isn't supported
     */
    public OpenCLWorkGenerator(int platformId, int deviceId) throws OpenCLInitializerException {
        this(platformId, deviceId, DEFAULT_THREADS, AbstractWorkGenerator.DEFAULT_POLICY);
    }
    
    /**
     * Constructs an {@code OpenCLWorkGenerator} with the default Nano difficulty policy using the specified number of
     * work threads on the specified OpenCL device.
     *
     * @param platformId  the OpenCL platform ID/index
     * @param deviceId    the OpenCL device ID/index
     * @param threadCount the number of parallel compute threads
     *
     * @throws OpenCLInitializerException if the device cannot be found, or OpenCL isn't supported
     */
    public OpenCLWorkGenerator(int platformId, int deviceId, int threadCount) throws OpenCLInitializerException {
        this(platformId, deviceId, threadCount, AbstractWorkGenerator.DEFAULT_POLICY);
    }
    
    /**
     * Constructs an {@code OpenCLWorkGenerator} with the given difficulty policy using {@value #DEFAULT_THREADS}
     * work threads on the specified OpenCL device.
     *
     * @param platformId the OpenCL platform ID/index
     * @param deviceId   the OpenCL device ID/index
     * @param policy     the work difficulty policy
     *
     * @throws OpenCLInitializerException if the device cannot be found, or OpenCL isn't supported
     */
    public OpenCLWorkGenerator(int platformId, int deviceId, WorkDifficultyPolicy policy)
            throws OpenCLInitializerException {
        this(platformId, deviceId, DEFAULT_THREADS, policy);
    }
    
    /**
     * Constructs an {@code OpenCLWorkGenerator} with the given difficulty policy using the specified number of work
     * threads on the specified OpenCL device.
     *
     * @param platformId  the OpenCL platform ID/index
     * @param deviceId    the OpenCL device ID/index
     * @param threadCount the number of parallel compute threads
     * @param policy      the work difficulty policy
     *
     * @throws OpenCLInitializerException if the device cannot be found, or OpenCL isn't supported
     */
    public OpenCLWorkGenerator(int platformId, int deviceId, int threadCount, WorkDifficultyPolicy policy)
            throws OpenCLInitializerException {
        super(policy);
        if (platformId < 0 || deviceId < 0)
            throw new IllegalArgumentException("Invalid platform or device ID.");
        if (threadCount < 1)
            throw new IllegalArgumentException("Must have at least 1 thread.");
        this.platformId = platformId;
        this.deviceId = deviceId;
        this.threadCount = threadCount;
    
        attemptBuf = ByteBuffer.allocateDirect(8).order(ByteOrder.LITTLE_ENDIAN);
        attemptPtr = Pointer.to(attemptBuf);
        resultBuf = ByteBuffer.allocateDirect(8).order(ByteOrder.LITTLE_ENDIAN);
        resultPtr = Pointer.to(resultBuf);
        initCL(); // Initialize OpenCL kernel
    }
    
    
    /**
     * Returns the number of parallel threads to compute work solutions.
     * @return the number of threads used
     */
    public int getThreadCount() {
        return threadCount;
    }
    
    /**
     * Returns the OpenCL platform index of the device.
     * @return the OpenCL platform id
     */
    public int getPlatformId() {
        return platformId;
    }
    
    /**
     * Returns the OpenCL device index on which the work will be generated.
     * @return the OpenCL device id
     */
    public int getDeviceId() {
        return deviceId;
    }
    
    /**
     * Returns the name of the OpenCL device on which the work will be generated.
     * @return the OpenCL device name
     */
    public String getDeviceName() {
        return deviceName;
    }
    
    
    @Override
    protected void cleanup() {
        try {
            // Free memory
            clFinish(clQueue);
            clReleaseKernel(clKernel);
            clReleaseCommandQueue(clQueue);
            clReleaseMemObject(clMemAttempt);
            clReleaseMemObject(clMemDifficulty);
            clReleaseMemObject(clMemResult);
            clReleaseMemObject(clMemRoot);
        } catch (CLException e) {
            e.printStackTrace();
        } finally {
            super.cleanup();
        }
    }
    
    @Override
    public String toString() {
        return "OpenCLWorkGenerator{" +
                "device='" + deviceName + '\'' +
                ", platformId=" + platformId +
                ", deviceId=" + deviceId +
                ", threadCount=" + threadCount + "}";
    }
    
    @Override
    protected WorkSolution generateWork(HexData root, WorkDifficulty difficulty, RequestContext context)
            throws WorkGenerationException, InterruptedException {
        try {
            // Set root hash arg
            clEnqueueWriteBuffer(clQueue, clMemRoot, true, 0, Sizeof.cl_uchar * 32,
                    Pointer.to(root.toByteArray()), 0, null, null);
            // Set difficulty arg
            clEnqueueWriteBuffer(clQueue, clMemDifficulty, true, 0, Sizeof.cl_ulong,
                    Pointer.to(new long[] { difficulty.getAsLong() }), 0, null, null);
            
            long attempt = RANDOM.nextLong(); // Initialize with random work
            long ignoreVal = resultBuf.getLong(0); // Ignore the current result value
            long[] workSize = { threadCount };
            
            // Repeatedly process generation attempts until a result is found
            long result;
            do {
                if (Thread.interrupted()) throw new InterruptedException(); // Terminate if interrupted
                attemptBuf.putLong(0, attempt += threadCount); // Increment and update attempt
                clEnqueueWriteBuffer(clQueue, clMemAttempt, false, 0, Sizeof.cl_ulong, attemptPtr, 0, null, null);
                clEnqueueNDRangeKernel(clQueue, clKernel, 1, null, workSize, null, 0, null, null);
                clEnqueueReadBuffer(clQueue, clMemResult, false, 0, Sizeof.cl_ulong, resultPtr, 0, null, null);
                clFinish(clQueue);
                result = resultBuf.getLong(0);
            } while (result == ignoreVal); // Compute until value is changed (solution found)
            return new WorkSolution(result);
        } catch (CLException e) {
            throw new WorkGenerationException("A problem with OpenCL occurred.", e);
        }
    }
    
    
    private void initCL() throws OpenCLInitializerException {
        try {
            setExceptionsEnabled(true); //TODO: parse errors manually
            
            String programSrc = getProgramSource("workgen.cl");
            
            // Obtain the number of platforms
            int[] numPlatformsArray = new int[1];
            clGetPlatformIDs(0, null, numPlatformsArray);
            int numPlatforms = numPlatformsArray[0];
            if (platformId >= numPlatforms)
                throw new OpenCLInitializerException("Platform ID not recognized.");
            
            // Obtain a platform ID
            cl_platform_id[] platforms = new cl_platform_id[numPlatforms];
            clGetPlatformIDs(platforms.length, platforms, null);
            cl_platform_id platform = platforms[platformId];
            
            // Initialize the context properties
            cl_context_properties contextProperties = new cl_context_properties();
            contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
            
            // Obtain the number of devices for the platform
            int[] numDevicesArray = new int[1];
            clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, 0, null, numDevicesArray);
            int numDevices = numDevicesArray[0];
            if (deviceId >= numDevices)
                throw new OpenCLInitializerException("Device ID not recognized.");
            
            // Obtain a device ID
            cl_device_id[] devices = new cl_device_id[numDevices];
            clGetDeviceIDs(platform, CL_DEVICE_TYPE_ALL, numDevices, devices, null);
            cl_device_id device = devices[deviceId];
            
            // Fetch device name
            byte[] deviceName = new byte[256];
            clGetDeviceInfo(device, CL_DEVICE_NAME, 256, Pointer.to(deviceName), null);
            this.deviceName = new String(deviceName, StandardCharsets.UTF_8);
            
            // Create a context for the selected device
            cl_context context = clCreateContext(
                    contextProperties, 1, new cl_device_id[] { device }, null, null, null);
            
            // Create a command-queue for the selected device
            cl_queue_properties properties = new cl_queue_properties();
            clQueue = clCreateCommandQueueWithProperties(context, device, properties, null);
            
            // Create the program from the source code
            cl_program program = clCreateProgramWithSource(
                    context, 1, new String[] { programSrc }, null, null);
            clBuildProgram(program, 1, new cl_device_id[] { device }, null, null, null);
            
            // Buffers
            clMemAttempt = clCreateBuffer(context,
                    CL_MEM_READ_ONLY | CL_MEM_HOST_WRITE_ONLY | CL_MEM_USE_HOST_PTR,
                    Sizeof.cl_ulong, attemptPtr, null);
            clMemResult = clCreateBuffer(context,
                    CL_MEM_WRITE_ONLY | CL_MEM_HOST_READ_ONLY | CL_MEM_USE_HOST_PTR,
                    Sizeof.cl_ulong, resultPtr, null);
            clMemRoot = clCreateBuffer(context,
                    CL_MEM_READ_ONLY | CL_MEM_HOST_WRITE_ONLY,
                    Sizeof.cl_uchar * 32, null, null);
            clMemDifficulty = clCreateBuffer(context,
                    CL_MEM_READ_ONLY | CL_MEM_HOST_WRITE_ONLY,
                    Sizeof.cl_ulong, null, null);
            
            // Create the kernel
            clKernel = clCreateKernel(program, "nano_work", null);
            clSetKernelArg(clKernel, 0, Sizeof.cl_mem, Pointer.to(clMemAttempt));
            clSetKernelArg(clKernel, 1, Sizeof.cl_mem, Pointer.to(clMemResult));
            clSetKernelArg(clKernel, 2, Sizeof.cl_mem, Pointer.to(clMemRoot));
            clSetKernelArg(clKernel, 3, Sizeof.cl_mem, Pointer.to(clMemDifficulty));
            
            // Cleanup memory
            clReleaseDevice(device);
            clReleaseProgram(program);
            clReleaseContext(context);
        } catch (CLException e) {
            throw new OpenCLInitializerException(e);
        }
    }
    
    private static String getProgramSource(String filename) throws OpenCLInitializerException {
        InputStream resource = OpenCLWorkGenerator.class.getClassLoader().getResourceAsStream(filename);
        if (resource == null)
            throw new OpenCLInitializerException("Could not locate resource workgen.cl");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line).append('\n');
            return sb.toString();
        } catch (IOException e) {
            throw new OpenCLInitializerException("Could not load resource workgen.cl", e);
        } finally {
            try {
                resource.close();
            } catch (IOException ignored) {}
        }
    }
    
    
    /**
     * Thrown when an OpenCL exception occurs during initialization.
     */
    public final static class OpenCLInitializerException extends Exception {
        OpenCLInitializerException(String message) {
            super(message);
        }
        
        OpenCLInitializerException(Throwable cause) {
            super(cause);
        }
    
        OpenCLInitializerException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
}
