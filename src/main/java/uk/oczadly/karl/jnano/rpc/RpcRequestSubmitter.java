package uk.oczadly.karl.jnano.rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RpcRequestSubmitter {
    
    public String submit(URL address, String authToken, String jsonRequest, int timeout) throws IOException {
        if (jsonRequest == null)
            throw new IllegalArgumentException("JSON request string cannot be null.");
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
    
        // Set authorization token header (if set)
        if (authToken != null) {
            con.setRequestProperty("Authorization", "Bearer " + authToken);
        }
    
        // Write request data
        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
        writer.write(jsonRequest);
        writer.close();
    
        // Read response data
        InputStreamReader input = new InputStreamReader(con.getInputStream());
        BufferedReader inputReader = new BufferedReader(input);
    
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = inputReader.readLine()) != null) {
            response.append(line);
        }
        inputReader.close();
    
        return response.toString();
    }

}