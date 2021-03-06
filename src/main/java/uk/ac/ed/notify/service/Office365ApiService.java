/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import com.microsoft.aad.adal4j.AsymmetricKeyCredential;
import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.ac.ed.notify.entity.Notification;
import uk.ac.ed.notify.repository.Office365Repository;
import uk.ac.ed.notify.entity.Office365Subscription;

/**
 *
 * @author hsun1
 */
@Service
public class Office365ApiService{
    private static final Logger logger = LoggerFactory.getLogger(Office365ApiService.class);           

    @Autowired
    EmailNotificationHandlingService emailNotificationHandlingService;
    
    @Autowired
    Office365Repository office365Repository;

    @Autowired
    Office365JsonService office365JsonService;
    
    @Autowired
    HttpOperationService httpOperationService;
    
    @Value("${office365.tenantId}")
    private String tenantId;
    
    @Value("${office365.pfxPath}")
    private String pfxPath;
    
    @Value("${office365.pfxPassword}")
    private String pfxPassword;
    
    @Value("${office365.clientId}")
    private String clientId;
    
    @Value("${office365.account}")
    private String account;
    
    @Value("${office365.callbackUrl}")
    private String callbackUrl;
    
    @Value("${office365.clientState}")
    private String clientState;
        
    public String acquireAccessToken(){
        logger.info("acquireAccessToken");
        String token = "";
        String authority = "https://login.windows.net/" + tenantId + "/oauth2/authorize";
        ExecutorService service = null;
        service=Executors.newFixedThreadPool(1);
        try {
            AuthenticationContext authenticationContext =  new AuthenticationContext(authority, false, service);
            String certfile = pfxPath;
            InputStream pkcs12Certificate=new FileInputStream(certfile);

            AsymmetricKeyCredential credential = AsymmetricKeyCredential.create(clientId, pkcs12Certificate, pfxPassword); 
            logger.info("certificate loaded successfully");

            Future<AuthenticationResult> future=authenticationContext.acquireToken("https://outlook.office365.com", (AsymmetricKeyCredential)credential, null);

            token=future.get().getAccessToken();

            logger.info("token - " + token);
            service.shutdown();
            
            logger.info("success");    
        }catch(Exception e){
            logger.error("acquireAccessToken - " + e.toString());
        }
        
        return token;
    }

    public void subscribeToNotification(String token){
        String url = "https://outlook.office.com/api/beta/users/" + account + "/subscriptions";
        
        logger.info("subscribeToNotification - " + url);            
        
        /*do not delete
            http://blogs.msdn.com/b/exchangedev/archive/2015/10/21/outlook-rest-api-changes-to-beta-endpoint-part-iii.aspx        
            Current         Interim                         Final
            ResourceURL	resource                        Resource
            CallbackURL	notificationURL                 NotificationURL
            ClientState	context                         ClientState
            ExpirationTime	subscriptionExpirationDateTime	SubscriptionExpirationDateTime
            ChangeType	changeType                      ChangeType
        */
                
        
        /*
        String input = 
                    "  {                                                                                                                " +
                    "  \"@odata.type\":\"#Microsoft.OutlookServices.PushSubscription\",                                                 " +           
                    "  \"ResourceURL\": \"https://outlook.office.com/api/beta/users/" + account + "/folders/inbox/messages\",           " +
                    "  \"CallbackURL\": \"" + callbackUrl + "\",                                                                        " +
                    "  \"ChangeType\": \"Created\",                                                                                     " +
                    "  \"ClientState\": \"" + clientState + "\"                                                                         " +
                    "  }                                                                                                                ";                
        */

        /*
        String input = 
                    "  {                                                                                                                " +
                    "  \"@odata.type\":\"#Microsoft.OutlookServices.PushSubscription\",                                                 " +           
                    "  \"resource\": \"https://outlook.office.com/api/beta/users/" + account + "/folders/inbox/messages\",              " +
                    "  \"notificationURL\": \"" + callbackUrl + "\",                                                                    " +
                    "  \"changeType\": \"Created\",                                                                                     " +
                    "  \"subscriptionExpirationDateTime\": \"2015-11-02T18:40:00.0Z\",                                                  " +
                    "  \"context\": \"" + clientState + "\"                                                                             " +
                    "  }                                                                                                                ";     
        */
        
        String input = 
                    "  {                                                                                                                " +
                    "  \"@odata.type\":\"#Microsoft.OutlookServices.PushSubscription\",                                                 " +           
                    //"  \"Resource\": \"https://outlook.office.com/api/beta/users/" + account + "/folders/inbox/messages\",              " +
                    "  \"Resource\": \"https://outlook.office.com/api/beta/users/" + account + "/messages\",              " +
                    "  \"NotificationURL\": \"" + callbackUrl + "\",                                                                    " +
                    "  \"ChangeType\": \"Created\",                                                                                     " +
                    "  \"SubscriptionExpirationDateTime\": \"2015-11-02T18:40:00.0Z\",                                                  " +
                    "  \"ClientState\": \"" + clientState + "\"                                                                             " +
                    "  }                                                                                                                ";    
        
        try {   
            logger.info("input - " + input);  
            
            String json = httpOperationService.post(token, url, input);            
            logger.info("success " + json); 

            Office365Subscription newSubscription = office365JsonService.parseOffice365NewSubscriptionCallbackSubscriptionId(json); 
            logger.info("newSubscription - " + newSubscription);
            
            office365Repository.save(newSubscription);
            logger.info("subscription saved successfully");        
                    
        }catch(Exception e){
            logger.error("subscribeToNotification - " + e.toString());       
        }

    }
        

 
    public void processUnreadEmail(String token){
        try {
            String url = "https://outlook.office365.com/api/v1.0/users/" + account + "/folders/inbox/messages?$filter=IsRead%20eq%20false";
            String json = httpOperationService.get(token, url);

            logger.info("construct all unread notifications from email - " + json);
            
            Hashtable<String, Notification> table = office365JsonService.parseTableOfNotification(json);
            logger.info("fetchUnreadEmail - found notifications, size - " + table.size());
            logger.info(table + "");
            Iterator<String> iterator = table.keySet().iterator();
            while(iterator.hasNext()){
                String id = iterator.next();
                Notification notification = table.get(id);
                emailNotificationHandlingService.processSingleNotification(notification);
                deleteEmailById(token, id);
            }
            logger.info("success");    
        }catch(Exception e){
            logger.error(e.toString());
        }
    }
    
    
    public void processEmailById(String token, String id){
        try {            
            String url = "https://outlook.office365.com/api/v1.0/users/" + account + "/folders/inbox/messages/" + id;
            
            logger.info("processEmailById - " + id);
            
            String json = httpOperationService.get(token, url);            
            Notification notification = office365JsonService.parseNotification(json);
           
            logger.info("construct notification from email - " + notification);
                      
            emailNotificationHandlingService.processSingleNotification(notification);    
            
            deleteEmailById(token, id);
            
            logger.info("success");    
        }catch(Exception e){
            logger.error(e.toString());
        }
    }    
    
    
    public void deleteEmailById(String token, String id){
        if(true) return;
        try {
            String url = "https://outlook.office365.com/api/v1.0/users/" + account + "/folders/inbox/messages/" + id;
          
            logger.info("deleteEmailById - " + url);
            
            httpOperationService.delete(token, url);   
            
            logger.info("success");
        }catch(Exception e){
            logger.error(e.toString());
        }
    }    
    
    
    public void deleteSubscriptionById(String token, String id){
        try {     
            //v1
            //String url = "https://outlook.office365.com/api/v1.0/users/" + account + "/subscriptions/" + id + ""; 
            
            String url = url = "https://outlook.office.com/api/beta/users/" + account + "/subscriptions/" + id + ""; 
            
            logger.info("deleteSubscriptionById - " + url);
            
            httpOperationService.delete(token, url);
            
            office365Repository.delete(id);
            
            logger.info("success");
        }catch(Exception e){
            logger.error(e.toString());
        }
    }    
    

    public void renewSubscriptionToNotification(String token, String subscriptionId){
        //renew subscription does not work on office365 at the moment, this method is not used, do not delete, for future reference
        
        /*
        Interim version     
        {
             @odata.type:"#Microsoft.OutlookServices.PushSubscription",
             subscriptionExpirationDateTime: "2015-10-24T20:00:00.0Z"
         } 

        {"@odata.type":"#Microsoft.OutlookServices.PushSubscription", "subscriptionExpirationDateTime": "2015-11-08T17:17:45.9028722Z"}       
        {"@odata.type":"#Microsoft.OutlookServices.PushSubscription", "subscriptionExpirationDateTime": "2015-11-08T20:00:00.0Z"}  
        {"@odata.type":"#Microsoft.OutlookServices.PushSubscription", "subscriptionExpirationDateTime": "2015-11-08T20:00:00.0Z"}  

        Final version  
         {
           @odata.type:"#Microsoft.OutlookServices.PushSubscription",
           SubscriptionExpirationDateTime: "2015-10-24T20:00:00.0Z"
         } 
        */
            
        
        try {
            
            //subscript result json
            //{"@odata.context":"https://outlook.office.com/api/beta/$metadata#Users('hsun1%40uoeapps.onmicrosoft.com')/Subscriptions/$entity",
            //"@odata.type":"#Microsoft.OutlookServices.PushSubscription",
            //"@odata.id":"https://outlook.office.com/api/beta/Users('hsun1@uoeapps.onmicrosoft.com')/Subscriptions
            //('MjNEMEUzNUQtNDU0MC00OUJDLUEyNDYtNDI3NDE1MDc5ODhGXzMzQkFDRkM5LURDNzEtNDNCNS1CMDE4LUJDNERFRDBFNDI5Nw==')","Id":"MjNEMEUzNUQtNDU0MC00OUJDLUEyNDYtNDI3NDE1MDc5ODhGXzMzQkFDRkM5LURDNzEtNDNCNS1CMDE4LUJDNERFRDBFNDI5Nw==","ResourceURL":"https://outlook.office.com/api/beta/users/hsun1@uoeapps.onmicrosoft.com/folders/inbox/messages","ChangeType":"Created, Acknowledgment, Missed","ClientState":"c75831bd-fad3-4191-9a66-280a48528679","CallbackURL":"https://www-test.myed.ed.ac.uk/BlackboardVCPortlet/callback","ExpirationTime":"2015-10-25T09:23:09.0141474Z"}

            //renew result json
            //{"@odata.context":"https://outlook.office.com/api/beta/$metadata#Users('hsun1%40uoeapps.onmicrosoft.com')/Subscriptions/$entity","@odata.type":"#Microsoft.OutlookServices.PushSubscription","@odata.id":"https://outlook.office.com/api/beta/Users('hsun1@uoeapps.onmicrosoft.com')/Subscriptions('MjNEMEUzNUQtNDU0MC00OUJDLUEyNDYtNDI3NDE1MDc5ODhGXzMzQkFDRkM5LURDNzEtNDNCNS1CMDE4LUJDNERFRDBFNDI5Nw==')","Id":"MjNEMEUzNUQtNDU0MC00OUJDLUEyNDYtNDI3NDE1MDc5ODhGXzMzQkFDRkM5LURDNzEtNDNCNS1CMDE4LUJDNERFRDBFNDI5Nw==","ResourceURL":"https://outlook.office.com/api/beta/users/hsun1@uoeapps.onmicrosoft.com/folders/inbox/messages","ChangeType":"Created, Acknowledgment, Missed","ClientState":"c75831bd-fad3-4191-9a66-280a48528679"}

            //hardcode for testing
            //String subscriptionId = "MjNEMEUzNUQtNDU0MC00OUJDLUEyNDYtNDI3NDE1MDc5ODhGXzMzQkFDRkM5LURDNzEtNDNCNS1CMDE4LUJDNERFRDBFNDI5Nw==";
            //String subscriptionId = readSubscriptionId();
            String url = "https://outlook.office.com/api/beta/users/" + account + "/subscriptions/" + subscriptionId + "/renew";
            
            logger.debug("renew url - " + url);
                      
            //String json  = "  {  \"@odata.type\":\"#Microsoft.OutlookServices.PushSubscription\", \"subscriptionExpirationDateTime\": \"2015-11-04T20:00:00.0Z\"}  ";   
            //logger.debug(json);
            
            
            //httpOperationService.post(token, url, json);
            //{"error":{"code":"RequestBrokerOld-ParseUri","message":"Resource not found for the segment 'renew'."}}
            
            httpOperationService.patch(token, url);
            
    
        }catch(Exception e){
            logger.error(e.toString());
        }         
    }    
       
    
}
