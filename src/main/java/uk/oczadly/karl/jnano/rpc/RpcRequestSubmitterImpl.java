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

public class RpcRequestSubmitterImpl implements RpcRequestSubmitter {
    
    @Override
    public String submit(URL address, String request, int timeout) throws IOException {
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
        con.setRequestMethod("POST");
        
        // Write request data
        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
        writer.write(request);
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
        inputReader.close();
    
        return response.toString();
    }

}
