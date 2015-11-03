/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.ac.ed.notify.entity.Office365Subscription;
import uk.ac.ed.notify.repository.Office365Repository;
import uk.ac.ed.notify.service.Office365ApiService;
import uk.ac.ed.notify.service.Office365JsonService;

@Controller
public class Office365CallbackController {
    private static final Logger logger = LoggerFactory.getLogger(Office365CallbackController.class);
    
    @Autowired
    Office365JsonService office365JsonService;        
      
    @Autowired
    Office365ApiService office365ApiService;
    
    @Autowired
    Office365Repository office365Repository;
    
    
    //@RequestMapping(value="/csvDownload" , method = RequestMethod.POST)
    //public void csvDownload(HttpServletRequest request, HttpServletResponse response)    
    
    @RequestMapping(value="/office365NewEmailCallback/", method=RequestMethod.POST)
    public void office365NewEmailCallback(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        logger.debug("office365NewEmailCallback request - " + request); 
        String json = getBody(request);
        logger.debug("office365NewEmailCallback json - " + json); 
        
        if(json.indexOf("Acknowledgment") != -1){
            logger.debug("office365NewSubscriptionCallback - " + json);
                  
            Office365Subscription newSubscription = office365JsonService.parseOffice365NewSubscriptionCallbackSubscriptionId(json); 
            logger.debug("newSubscription - " + newSubscription);
            office365Repository.save(newSubscription);
            logger.debug("subscription successful");
        }else{        
            String token = office365ApiService.acquireAccessToken();             
            String newEmailId = office365JsonService.parseOffice365NewEmailCallbackEmailId(json);
            logger.debug("newEmailId - " + newEmailId);
            office365ApiService.processEmailById(token, newEmailId);
        }
    }    
    
    @RequestMapping(value="/office365NewSubscriptionCallback/", method=RequestMethod.POST)
    public @ResponseBody void office365NewSubscriptionCallback(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        logger.debug("office365NewSubscriptionCallback request - " + request); 
        String json = getBody(request);
        logger.debug("office365NewSubscriptionCallback json - " + json);                 
                  
        Office365Subscription newSubscription = office365JsonService.parseOffice365NewSubscriptionCallbackSubscriptionId(json); 
        office365Repository.save(newSubscription);
    }  
    
    @RequestMapping(value="/office365RenewSubscriptionCallback/", method=RequestMethod.POST)
    public @ResponseBody void office365RenewSubscriptionCallback(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        logger.debug("office365RenewSubscriptionCallback request - " + request); 
        String json = getBody(request);
        logger.debug("office365RenewSubscriptionCallback json - " + json);          
        
        Office365Subscription newSubscription = office365JsonService.parseOffice365NewSubscriptionCallbackSubscriptionId(json);                
        office365Repository.deleteAll();
        office365Repository.save(newSubscription);
    }    
    
    
    public String getBody(HttpServletRequest request) throws java.io.IOException {
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        java.io.BufferedReader bufferedReader = null;

        try {
            java.io.InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (java.io.IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (java.io.IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }    
}
