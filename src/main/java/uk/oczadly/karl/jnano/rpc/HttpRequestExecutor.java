/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * The standard implementation of {@link RpcRequestExecutor}, which submits requests through an HTTP POST request.
 */
public class HttpRequestExecutor implements RpcRequestExecutor {
    
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
        return makeRequest(con, request);
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
    public static String makeRequest(URLConnection con, String body) throws IOException {
        try {
            // Write request data
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body);
            writer.close();
        
            // Read response data
            InputStreamReader input = new InputStreamReader(con.getInputStream());
            BufferedReader inputReader = new BufferedReader(input);
            int expectedLength = con.getContentLength();
            StringBuilder response = new StringBuilder(expectedLength >= 0 ? expectedLength : 32);
            String line;
            while ((line = inputReader.readLine()) != null) {
                response.append(line);
            }
            
            return response.toString();
        } finally {
            if (con.getInputStream() != null) {
                con.getInputStream().close();
            }
        }
    }

}
