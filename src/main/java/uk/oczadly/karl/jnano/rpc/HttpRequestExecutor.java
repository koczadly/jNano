/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

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
    
    private final URL address;
    
    /**
     * Configures a request executor over the {@code http} or {@code https} protocols.
     * @param address the endpoint address
     */
    public HttpRequestExecutor(URL address) {
        if (address == null)
            throw new IllegalArgumentException("Address cannot be null.");
        if (!address.getProtocol().equalsIgnoreCase("http") && !address.getProtocol().equalsIgnoreCase("https"))
            throw new IllegalArgumentException("HttpRequestExecutor only supports the HTTP and HTTPS protocols.");
        
        this.address = address;
    }
    
    
    /**
     * @return the URL address of the endpoint
     */
    public final URL getAddress() {
        return address;
    }
    
    
    @Override
    public final String submit(String request, int timeout) throws IOException {

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
        return makeRequest(con, request);
    }
    
    /**
     * Set the headers and request method of the connection. May be overridden.
     * @param con the connection
     * @throws IOException if an IO error prevents setting the header
     */
    protected void setRequestHeaders(HttpURLConnection con) throws IOException {
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
    }
    
    
    /**
     * Makes an HTTP request.
     * @param con  the HTTP connection
     * @param body the request body
     * @return the response body as a string
     * @throws IOException if an exception occurs
     */
    protected static String makeRequest(HttpURLConnection con, String body) throws IOException {
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
                is = con.getErrorStream();
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
