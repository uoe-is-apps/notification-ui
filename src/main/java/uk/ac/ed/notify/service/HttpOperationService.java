/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
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

            logger.debug("get, server response code - " + con.getResponseCode());
            
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

            logger.debug("post, server response code - " + con.getResponseCode());
            
            BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
            String output = "";
            String json = "";
            while ((output = br.readLine()) != null) {  
                logger.debug("server output: " + output);
                if(output.indexOf("json") != -1 || output.indexOf("odata") != -1){
                    json = output;
                }
            }

/*
2015-11-04 14:14:54.711 DEBUG 17168 --- [ryBean_Worker-3] u.a.e.n.service.HttpOperationService     : post, server response code - 201
server output: {"@odata.context":"https://outlook.office.com/api/beta/$metadata#Users('scotapps%40scotapps.onmicrosoft.com')/Subscriptions/$entity","@odata.type":"#Microsoft.OutlookServices.PushSubscription","@odata.id":"https://outlook.office.com/api/beta/Users('scotapps@scotapps.onmicrosoft.com')/Subscriptions('QkRDMjgwQUEtQjExNi00NjA5LTkyMjAtMEJFOTVBQTNCQjU3XzQ1MTU4OEJFLTczQzQtNDBFOS1BN0E1LUYyOTdENkEzM0NBMQ==')","Id":"QkRDMjgwQUEtQjExNi00NjA5LTkyMjAtMEJFOTVBQTNCQjU3XzQ1MTU4OEJFLTczQzQtNDBFOS1BN0E1LUYyOTdENkEzM0NBMQ==","Resource":"https://outlook.office.com/api/beta/users/scotapps@scotapps.onmicrosoft.com/messages","ChangeType":"Created, Acknowledgment, Missed","ClientState":"c75831bd-fad3-4191-9a66-280a48528679","NotificationURL":"https://dev.notifyadm.is.ed.ac.uk/office365NewEmailCallback/","SubscriptionExpirationDateTime":"2015-11-07T14:14:54.4142775Z"}
success  
 */            
            
            
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

            logger.debug("delete, server response code - " + con.getResponseCode());
            
            if (con.getResponseCode() != HttpURLConnection.HTTP_CREATED 
                    && con.getResponseCode() != HttpURLConnection.HTTP_OK 
                    && con.getResponseCode() != HttpURLConnection.HTTP_NO_CONTENT ) {
                throw new RuntimeException("Failed : HTTP error code : " + con.getResponseCode());
            }
            con.disconnect();               
    }
        
    
    public void patch(String token, String url){

        /*
        try {
            HttpClient httpclient = HttpClients.createDefault();
            
            org.apache.http.client.methods.HttpPatch httppatch = new org.apache.http.client.methods.HttpPatch();
            httppatch.setURI(new URI(url));
        
            httppatch.setHeader("Accept","application/json"); 
            httppatch.setHeader("Content-Type", "application/json");
            httppatch.setHeader("User-Agent","Testing/1.0 abc/1.1");
            httppatch.setHeader("Authorization","Bearer "+token);             
          
            
            //List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            //params.add(new BasicNameValuePair("", ""));
            //httppatch.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            
            
            HttpResponse response = httpclient.execute(httppatch);
            HttpEntity entity = response.getEntity();

            if (entity != null) {                
                InputStream is = entity.getContent();
                StringBuilder sb = new StringBuilder();
                try {
                    System.out.println("ok");
                    
                    BufferedReader br = null;
                    String line;
                    try {

                            br = new BufferedReader(new InputStreamReader(is));
                            while ((line = br.readLine()) != null) {
                                    sb.append(line);
                            }

                    } catch (IOException e) {
                            e.printStackTrace();
                    } finally {
                            if (br != null) {
                                    try {
                                            br.close();
                                    } catch (IOException e) {
                                            e.printStackTrace();
                                    }
                            }
                    }
                    
                    
                } finally {
                    is.close();
                }
                
                System.out.println(sb.toString());
            }            
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        */

        
    }
    
}
