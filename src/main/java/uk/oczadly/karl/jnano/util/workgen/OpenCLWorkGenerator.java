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
    private String deviceName;
    
    // OpenCL buffers and params
    private cl_command_queue clQueue;
    private cl_kernel clKernel;
    private cl_mem clMemAttempt;
    private cl_mem clMemRoot;
    private cl_mem clMemDifficulty;
    private cl_mem clMemResult;
    private long[] resultBuffer;
    private Pointer clMemResultPtr;
    
    /**
     * Constructs an {@code OpenCLWorkGenerator} with the default Nano difficulty policy using {@value #DEFAULT_THREADS}
     * work threads on the default OpenCL device (platform {@code 0}, device {@code 0}).
     *
     * @throws OpenCLInitializerException if no default device cannot be found, or OpenCL isn't supported
     */
    public OpenCLWorkGenerator() {
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
    public OpenCLWorkGenerator(int platformId, int deviceId) {
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
    public OpenCLWorkGenerator(int platformId, int deviceId, int threadCount) {
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
    public OpenCLWorkGenerator(int platformId, int deviceId, WorkDifficultyPolicy policy) {
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
    public OpenCLWorkGenerator(int platformId, int deviceId, int threadCount, WorkDifficultyPolicy policy) {
        super(policy);
        if (platformId < 0 || deviceId < 0)
            throw new IllegalArgumentException("Invalid platform or device ID.");
        if (threadCount < 1)
            throw new IllegalArgumentException("Must have at least 1 thread.");
        this.platformId = platformId;
        this.deviceId = deviceId;
        this.threadCount = threadCount;
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
     * Returns the OpenCL device name on which the work will be generated.
     * @return the OpenCL device id
     */
    public String getDeviceName() {
        return deviceName;
    }
    
    
    @Override
    public void cleanup() {
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
                ", threadCount=" + threadCount + "'}";
    }
    
    @Override
    protected WorkSolution generateWork(HexData root, WorkDifficulty difficulty, RequestContext context)
            throws WorkGenerationException, InterruptedException {
        try {
            clEnqueueWriteBuffer(clQueue, clMemRoot, true, 0, Sizeof.cl_uchar * 32,
                    Pointer.to(root.toByteArray()), 0, null, null);
            clEnqueueWriteBuffer(clQueue, clMemDifficulty, true, 0, Sizeof.cl_ulong,
                    Pointer.to(new long[] {difficulty.getAsLong()}), 0, null, null);
    
            long[] work_size = {threadCount, 0, 0};
            long[] arg_attempt = {RANDOM.nextLong()};
            Pointer attemptPointer = Pointer.to(arg_attempt);
            long ignoreResult = resultBuffer[0];
    
            do {
                if (Thread.currentThread().isInterrupted())
                    throw new InterruptedException();
        
                arg_attempt[0] += threadCount;
                clEnqueueWriteBuffer(clQueue, clMemAttempt, true, 0, Sizeof.cl_ulong, attemptPointer, 0, null, null);
                clEnqueueNDRangeKernel(clQueue, clKernel, 1, null, work_size, null, 0, null, null);
                clEnqueueReadBuffer(clQueue, clMemResult, true, 0, Sizeof.cl_ulong, clMemResultPtr, 0, null, null);
                clFinish(clQueue);
            } while (resultBuffer[0] == ignoreResult); // Skip initial value (result from previous)
            return new WorkSolution(resultBuffer[0]);
        } catch (CLException e) {
            throw new WorkGenerationException("A problem with OpenCL occurred.", e);
        }
    }
    
    
    private void initCL() {
        try {
            String programSrc = getProgramSource();
            
            setExceptionsEnabled(true); //TODO: parse errors manually
            
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
            clMemAttempt = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_HOST_WRITE_ONLY,
                    Sizeof.cl_ulong, null, null);
            clMemResult = clCreateBuffer(context, CL_MEM_WRITE_ONLY | CL_MEM_HOST_READ_ONLY,
                    Sizeof.cl_ulong, null, null);
            resultBuffer = new long[1];
            clMemResultPtr = Pointer.to(resultBuffer);
            clMemRoot = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_HOST_WRITE_ONLY,
                    Sizeof.cl_uchar * 32, null, null);
            clMemDifficulty = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_HOST_WRITE_ONLY,
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
    
    private static String getProgramSource() {
        InputStream resource = OpenCLWorkGenerator.class.getClassLoader().getResourceAsStream("workgen.cl");
        if (resource == null)
            throw new AssertionError("Could not locate resource workgen.cl");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)
                sb.append(line).append('\n');
            return sb.toString();
        } catch (IOException e) {
            throw new AssertionError("Could not load resource workgen.cl", e);
        }
    }
    
    
    /**
     * Thrown when an OpenCL exception occurs during initialization.
     */
    public final static class OpenCLInitializerException extends RuntimeException {
        OpenCLInitializerException(String message) {
            super(message);
        }
        
        OpenCLInitializerException(Throwable cause) {
            super(cause);
        }
    }
    
}
