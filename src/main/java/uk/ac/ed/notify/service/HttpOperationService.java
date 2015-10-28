/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author hsun1
 */
@Service
public class HttpOperationService {
    private static final Logger logger = LoggerFactory.getLogger(HttpOperationService.class);      
    
    public String get(String token, String url) throws Exception{
            HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection(); 

            con.setRequestMethod("GET"); 
            con.setRequestProperty("Accept","application/json"); 
            con.setRequestProperty("User-Agent","Testing/1.0 abc/1.1");
            con.setRequestProperty("Authorization","Bearer "+token);

            if (con.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + con.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

            String json = "";
            String output = "";
            while ((output = br.readLine()) != null) { 
                if(output.indexOf("json") != -1){
                    json = output;
                }
            }
            con.disconnect();                
            return json;
    }
    
    public String post(String token, String url, String input) throws Exception{
            HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection(); 
            
            con.setDoOutput(true);
            con.setRequestMethod("POST"); 
            con.setRequestProperty("Accept","application/json"); 
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("User-Agent","Testing/1.0 abc/1.1");
            con.setRequestProperty("Authorization","Bearer "+token);

            OutputStream os = con.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (con.getResponseCode() != HttpURLConnection.HTTP_CREATED && con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + con.getResponseCode());
            }

            System.out.println("server response code - " + con.getResponseCode());
            
            BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
            String output = "";
            String json = "";
            while ((output = br.readLine()) != null) {                
                if(output.indexOf("json") != -1){
                    json = output;
                }
            }

            con.disconnect();     
            return json;
    }

    public void delete(String token, String url) throws Exception{
            HttpURLConnection con = (HttpURLConnection) (new URL(url)).openConnection(); 
            
            con.setDoOutput(true);
            con.setRequestMethod("DELETE"); 
            con.setRequestProperty("Accept","application/json"); 
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("User-Agent","Testing/1.0 abc/1.1");
            con.setRequestProperty("Authorization","Bearer "+token);

            String input = " {} ";

            OutputStream os = con.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (con.getResponseCode() != HttpURLConnection.HTTP_CREATED && con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + con.getResponseCode());
            }
            con.disconnect();               
    }
        
    
}
