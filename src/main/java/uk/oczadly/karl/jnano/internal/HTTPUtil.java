/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * @author Karl Oczadly
 */
public class HTTPUtil {
    
    public static String request(HttpURLConnection con, String body) throws IOException {
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
