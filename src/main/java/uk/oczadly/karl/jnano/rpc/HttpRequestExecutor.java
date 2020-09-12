/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * The standard implementation of {@link RpcRequestExecutor}, which submits requests through an HTTP POST request.
 *
 * <p>If {@code allowErrors} is set to true, non-200 HTTP errors returned by the server will <em>not</em> throw an
 * {@link IOException} and will just forward the body data to the deserializer.</p>
 *
 * <p>The request method and headers may be changed by overriding the {@link #setRequestHeaders(HttpURLConnection)}
 * method.</p>
 */
public class HttpRequestExecutor implements RpcRequestExecutor {
    
    private final boolean allowErrors;
    
    
    /**
     * Constructs an executor with {@code allowErrors} set to false.
     * @see #HttpRequestExecutor(boolean)
     */
    public HttpRequestExecutor() {
        this(false);
    }
    
    /**
     * @param allowErrors whether HTTP errors should be forwarded to the deserializer
     */
    public HttpRequestExecutor(boolean allowErrors) {
        this.allowErrors = allowErrors;
    }
    
    
    public final boolean getAllowErrors() {
        return allowErrors;
    }
    
    
    @Override
    public final String submit(URL address, String request, int timeout) throws IOException {
        if (address == null)
            throw new IllegalArgumentException("Address cannot be null.");
        if (!address.getProtocol().equalsIgnoreCase("http") && !address.getProtocol().equalsIgnoreCase("https"))
            throw new IllegalArgumentException(
                    "HttpRequestExecutor only supports the 'HTTP' and 'HTTPS' protocols.");
        if (request == null)
            throw new IllegalArgumentException("Request body cannot be null.");
        if (timeout < 0)
            throw new IllegalArgumentException("Timeout period must be positive or zero.");
        
        // Open connection
        HttpURLConnection con = (HttpURLConnection)address.openConnection();
        
        // Configure connection
        con.setConnectTimeout(timeout);
        con.setReadTimeout(timeout);
        con.setDoOutput(true);
        con.setDoInput(true);
        setRequestHeaders(con);
        
        // Submit
        return makeRequest(con, request, allowErrors);
    }
    
    /**
     * Set the headers and request method of the connection. May be overridden.
     * @param con the connection
     */
    protected void setRequestHeaders(HttpURLConnection con) throws IOException {
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
    }
    
    
    /**
     * Makes a request to a configured {@link URLConnection}.
     * @param con  the connection
     * @param body the request body
     * @return the returned data, as a {@link String}
     * @throws IOException if an exception occurs with the connection
     */
    public static String makeRequest(HttpURLConnection con, String body) throws IOException {
        return makeRequest(con, body, true);
    }
    
    private static String makeRequest(HttpURLConnection con, String body, boolean allowErrors) throws IOException {
        try (OutputStream os = con.getOutputStream()) {
            // Write request data
            OutputStreamWriter writer = new OutputStreamWriter(os);
            writer.write(body);
            writer.close();
            
            // Find input stream
            InputStream is;
            try {
                is = con.getInputStream();
            } catch (IOException e) {
                if (allowErrors) {
                    is = con.getErrorStream();
                } else {
                    throw e;
                }
            }
            
            // Read response data
            InputStreamReader input = new InputStreamReader(is);
            BufferedReader inputReader = new BufferedReader(input);
            int expectedLength = con.getContentLength();
            StringBuilder response = new StringBuilder(expectedLength >= 0 ? expectedLength : 32);
            String line;
            while ((line = inputReader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

}
