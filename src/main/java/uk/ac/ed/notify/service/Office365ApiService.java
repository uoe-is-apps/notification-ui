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
import uk.ac.ed.notify.repository.NotificationRepository;

/**
 *
 * @author hsun1
 */
@Service
public class Office365ApiService {
    private static final Logger logger = LoggerFactory.getLogger(Office365ApiService.class);           
    
    @Autowired
    Office365Repository office365Repository;
                
    @Autowired
    NotificationRepository notificationRepository;
    
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
        String token = "";
        String authority = "https://login.windows.net/" + tenantId + "/oauth2/authorize";
        ExecutorService service = null;
        service=Executors.newFixedThreadPool(1);
        try {
            AuthenticationContext authenticationContext =  new AuthenticationContext(authority, false, service);
            String certfile = pfxPath;
            InputStream pkcs12Certificate=new FileInputStream(certfile);

            AsymmetricKeyCredential credential = AsymmetricKeyCredential.create(clientId, pkcs12Certificate, pfxPassword); 
            System.out.println("certificate loaded successfully");

            Future<AuthenticationResult> future=authenticationContext.acquireToken("https://outlook.office365.com", (AsymmetricKeyCredential)credential, null);

            token=future.get().getAccessToken();

            System.out.println("token - " + token);
            service.shutdown();
        }catch(Exception e){

        }
        
        return token;
    }
     

    private String readSubscriptionId(){
        String subscriptionId = "";
        
        Iterable<Office365Subscription> iteration = office365Repository.findAll();
        while(iteration.iterator().hasNext()){
            Office365Subscription subscription = iteration.iterator().next();
            subscriptionId = subscription.getSubscriptionId();
            System.out.println("readSubscriptionId - " + subscriptionId);
            break;
        }
        
        return subscriptionId;
    }
    
    
    public void subscribeToNotification(String token){
        String url = "https://outlook.office.com/api/beta/users/" + account + "/subscriptions";
        
        System.out.println("subscribeToNotification - " + url);            
        
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
        
        //callbackUrl = "https://www-test.myed.ed.ac.uk/BlackboardVCPortlet/callback";
        //callbackUrl = "https://dev.notifyadm.is.ed.ac.uk/office365NewEmailCallback/";
        
        
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
        
        /*
        //http://blogs.msdn.com/b/exchangedev/archive/2015/10/21/outlook-rest-api-changes-to-beta-endpoint-part-iii.aspx        
Current         Interim                         Final
ResourceURL	resource                        Resource
CallbackURL	notificationURL                 NotificationURL
ClientState	context                         ClientState
ExpirationTime	subscriptionExpirationDateTime	SubscriptionExpirationDateTime
ChangeType	changeType                      ChangeType
        */
        
        try {
            System.out.println("input - " + input);   
            
            logger.debug("url - " + url);       
            logger.debug("input - " + input);       
            
            String json = httpOperationService.post(token, url, input);
            System.out.println("success " + json); 
            logger.debug("success " + json);     
            
            
            logger.debug("office365NewSubscriptionCallback - " + json);

            //2015-11-04 14:14:54.711 DEBUG 17168 --- [ryBean_Worker-3] u.a.e.n.service.HttpOperationService     : post, server response code - 201
            //server output: 
            //{"@odata.context":"https://outlook.office.com/api/beta/$metadata#Users('scotapps%40scotapps.onmicrosoft.com')/Subscriptions/$entity",
            //"@odata.type":"#Microsoft.OutlookServices.PushSubscription",
            //"@odata.id":"https://outlook.office.com/api/beta/Users('scotapps@scotapps.onmicrosoft.com')/Subscriptions('QkRDMjgwQUEtQjExNi00NjA5LTkyMjAtMEJFOTVBQTNCQjU3XzQ1MTU4OEJFLTczQzQtNDBFOS1BN0E1LUYyOTdENkEzM0NBMQ==')",
            //"Id":"QkRDMjgwQUEtQjExNi00NjA5LTkyMjAtMEJFOTVBQTNCQjU3XzQ1MTU4OEJFLTczQzQtNDBFOS1BN0E1LUYyOTdENkEzM0NBMQ==",
            //"Resource":"https://outlook.office.com/api/beta/users/scotapps@scotapps.onmicrosoft.com/messages","ChangeType":"Created, Acknowledgment, Missed","ClientState":"c75831bd-fad3-4191-9a66-280a48528679","NotificationURL":"https://dev.notifyadm.is.ed.ac.uk/office365NewEmailCallback/",
            //"SubscriptionExpirationDateTime":"2015-11-07T14:14:54.4142775Z"}
            //success  
            
            
            Office365Subscription newSubscription = office365JsonService.parseOffice365NewSubscriptionCallbackSubscriptionId(json); 
            logger.debug("newSubscription - " + newSubscription);
            office365Repository.save(newSubscription);
            logger.debug("subscription saved successfully");        
                    
        }catch(Exception e){
            e.printStackTrace();
            logger.error("e " + e.toString());       
        }

    }
        
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
    
    public void renewSubscriptionToNotification(String token, String subscriptionId){
        if(true){
            deleteSubscriptionById(token, subscriptionId);
            return;
        }
        
        
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
            
            System.out.println("renew url - " + url);
                      
            //String json  = "  {  \"@odata.type\":\"#Microsoft.OutlookServices.PushSubscription\", \"subscriptionExpirationDateTime\": \"2015-11-04T20:00:00.0Z\"}  ";   
            //System.out.println(json);
            
            
            //httpOperationService.post(token, url, json);
            //{"error":{"code":"RequestBrokerOld-ParseUri","message":"Resource not found for the segment 'renew'."}}
            
            httpOperationService.patch(token, url);
            
    
        }catch(Exception e){
            logger.error(e.toString());
        }         
    }    
   
    
    public void processUnreadEmail(String token){
        //fetchUnreadEmail - {"@odata.context":"https://outlook.office365.com/api/v1.0/$metadata#Users('hsun1%40uoeapps.onmicrosoft.com')/Folders('inbox')/Messages",
        //"value":[{"@odata.id":"https://outlook.office365.com/api/v1.0/Users('hsun1@uoeapps.onmicrosoft.com')/Messages
        //('AAMkADMzYmFjZmM5LWRjNzEtNDNiNS1iMDE4LWJjNGRlZDBlNDI5NwBGAAAAAADQ1fbd9u-oS6EUMm8vRMp5BwCfJe3G2ZDPT54eZMk1kgk2AAAAAAEMAACfJe3G2ZDPT54eZMk1kgk2AAAATKXbAAA=')","@odata.etag":"W/\"CQAAABYAAACfJe3G2ZDPT54eZMk1kgk2AAAATKgn\"","Id":"AAMkADMzYmFjZmM5LWRjNzEtNDNiNS1iMDE4LWJjNGRlZDBlNDI5NwBGAAAAAADQ1fbd9u-oS6EUMm8vRMp5BwCfJe3G2ZDPT54eZMk1kgk2AAAAAAEMAACfJe3G2ZDPT54eZMk1kgk2AAAATKXbAAA=","ChangeKey":"CQAAABYAAACfJe3G2ZDPT54eZMk1kgk2AAAATKgn","Categories":[],"DateTimeCreated":"2015-10-22T09:32:18Z","DateTimeLastModified":"2015-10-22T09:32:18Z","HasAttachments":false,"Subject":"notification create","Body":{"ContentType":"Text","Content":"<script type=\"application/ld+json\">\r\n{\r\n\"@type\": \"Notification\", \"publisherId\": \"12345\", \"publisherNotificationId\": \"12345\", \"publisherKey\": \"005AFE5E177048ABE05400144F00F4CC\", \"topic\": \"example category\", \"title\": \"example title\",\"body\": \"example body\", \"url\": \"http://www.ed.ac.uk\", \"uun\": \"rgood\", \"startDate\": \"2013-05-15T08:30\", \"endDate\": \"2013-05-20T08:30\", \"action\": \"insert\"\r\n}\r\n</script>\r\n\r\n-- \r\nThe University of Edinburgh is a charitable body, registered in\r\nScotland, with registration number SC005336.\r\n\r\n"},"BodyPreview":"<script type=\"application/ld+json\">\r\n{\r\n\"@type\": \"Notification\", \"publisherId\": \"12345\", \"publisherNotificationId\": \"12345\", \"publisherKey\": \"005AFE5E177048ABE05400144F00F4CC\", \"topic\": \"example category\", \"title\": \"example title\",\"body\": \"example body\", ","Importance":"Normal","ParentFolderId":"AQMkADMzAGJhY2ZjOS1kYzcxLTQzYjUtYjAxOC1iYzRkZWQwZTQyOTcALgAAA9DV9t327_hLoRQyby9EynkBAJ8l7cbZkM9Pnh5kyTWSCTYAAAIBDAAAAA==","Sender":{"EmailAddress":{"Address":"Hui.Sun@ed.ac.uk","Name":"SUN Michael"}},"From":{"EmailAddress":{"Address":"Hui.Sun@ed.ac.uk","Name":"SUN Michael"}},"ToRecipients":[{"EmailAddress":{"Address":"hsun1@uoeapps.onmicrosoft.com","Name":"Hui Sun"}}],"CcRecipients":[],"BccRecipients":[],"ReplyTo":[],"ConversationId":"AAQkADMzYmFjZmM5LWRjNzEtNDNiNS1iMDE4LWJjNGRlZDBlNDI5NwAQALmIdVeKe0ocnBuBKjRFxGo=","IsDeliveryReceiptRequested":null,"IsReadReceiptRequested":false,"IsRead":false,"IsDraft":false,"DateTimeReceived":"2015-10-22T09:32:18Z","DateTimeSent":"2015-10-22T09:31:37Z","WebLink":"https://outlook.office365.com/owa/?ItemID=AAMkADMzYmFjZmM5LWRjNzEtNDNiNS1iMDE4LWJjNGRlZDBlNDI5NwBGAAAAAADQ1fbd9u%2FoS6EUMm8vRMp5BwCfJe3G2ZDPT54eZMk1kgk2AAAAAAEMAACfJe3G2ZDPT54eZMk1kgk2AAAATKXbAAA%3D&exvsurl=1&viewmodel=ReadMessageItem"}]}

        try {
            String url = "https://outlook.office365.com/api/v1.0/users/" + account + "/folders/inbox/messages?$filter=IsRead%20eq%20false";
            String json = httpOperationService.get(token, url);

            System.out.println("processUnreadEmail - " + json);
            
            Hashtable<String, Notification> table = office365JsonService.parseTableOfNotification(json);
            System.out.println("fetchUnreadEmail - " + table.size());
            
            Iterator<String> iterator = table.keySet().iterator();
            while(iterator.hasNext()){
                String id = iterator.next();
                Notification notification = table.get(id);
                
                System.out.println("save notification..." + notification.getTitle());
                
                notificationRepository.save(notification);
                
                deleteEmailById(token, id);
            }
                        
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.toString());
        }
    }
    
    
    public void processEmailById(String token, String id){
        try {
            //String id = "AAMkADMzYmFjZmM5LWRjNzEtNDNiNS1iMDE4LWJjNGRlZDBlNDI5NwBGAAAAAADQ1fbd9u-oS6EUMm8vRMp5BwCfJe3G2ZDPT54eZMk1kgk2AAAAAAEMAACfJe3G2ZDPT54eZMk1kgk2AAAATKXbAAA=";
            String url = "https://outlook.office365.com/api/v1.0/users/" + account + "/folders/inbox/messages/" + id;
            
            String json = httpOperationService.get(token, url);            
            Notification notification = office365JsonService.parseNotification(json);
           
            notificationRepository.save(notification);           
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.toString());
        }
    }    
    
    
    public void deleteEmailById(String token, String id){
        if(true) return; //do not enable
        try {
            //String id = "AAMkADMzYmFjZmM5LWRjNzEtNDNiNS1iMDE4LWJjNGRlZDBlNDI5NwBGAAAAAADQ1fbd9u-oS6EUMm8vRMp5BwCfJe3G2ZDPT54eZMk1kgk2AAAAAAEMAACfJe3G2ZDPT54eZMk1kgk2AAAATKXcAAA=";
            String url = "https://outlook.office365.com/api/v1.0/users/" + account + "/folders/inbox/messages/" + id;
          
            httpOperationService.delete(token, url);   
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.toString());
        }
    }    
    
    
    public void deleteSubscriptionById(String token, String id){
        try {            
            String url = "https://outlook.office365.com/api/v1.0/users/" + account + "/subscriptions/" + id + "";       
            
            //works in fiddler
            url = "https://outlook.office.com/api/beta/users/" + account + "/subscriptions/" + id + ""; //ODYzNUJEMjQtRDJFMi00RDA3LTlENUYtNjZBMzExMkYwN0VEXzQ1MTU4OEJFLTczQzQtNDBFOS1BN0E1LUYyOTdENkEzM0NBMQ=="
            
            
            System.out.println(url);
            httpOperationService.delete(token, url);
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.toString());
        }
    }    
    
//new subscription - {"value":[{"@odata.type":"#Microsoft.OutlookServices.Notification","Id":null,"subscriptionId":"OUJCOEMwQUYtMzk5OS00QUNBLUJBQzAtQzNFRTNDMzQzNDg3XzMzQkFDRkM5LURDNzEtNDNCNS1CMDE4LUJDNERFRDBFNDI5Nw==","subscriptionExpirationDateTime":"2015-10-30T17:52:32.1693974Z","sequenceNumber":0,"changeType":"Acknowledgment"}]}    
//new email - {"value":[{"@odata.type":"#Microsoft.OutlookServices.Notification","Id":null,"subscriptionId":"RTEyRUVBOTUtMjI4NS00MEE0LUFGQ0UtOTgxNkNBRDAyQzkwXzQ1MTU4OEJFLTczQzQtNDBFOS1BN0E1LUYyOTdENkEzM0NBMQ==","subscriptionExpirationDateTime":"2015-10-30T17:32:37.4526939Z","sequenceNumber":1,"changeType":"Created","resource":"https://outlook.office.com/api/beta/Users('scotapps@scotapps.onmicrosoft.com')/Messages('AAMkADQ1MTU4OGJlLTczYzQtNDBlOS1hN2E1LWYyOTdkNmEzM2NhMQBGAAAAAAAQSU6klR_CS7f2u_Zotqz3BwCK45XT6t02QqdGHEQt1wAjAAAAAAEMAACK45XT6t02QqdGHEQt1wAjAAAAAAF6AAA=')","resourceData":{"@odata.type":"#Microsoft.OutlookServices.Message","@odata.id":"https://outlook.office.com/api/beta/Users('scotapps@scotapps.onmicrosoft.com')/Messages('AAMkADQ1MTU4OGJlLTczYzQtNDBlOS1hN2E1LWYyOTdkNmEzM2NhMQBGAAAAAAAQSU6klR_CS7f2u_Zotqz3BwCK45XT6t02QqdGHEQt1wAjAAAAAAEMAACK45XT6t02QqdGHEQt1wAjAAAAAAF6AAA=')","@odata.etag":"W/\"CQAAABYAAACK45XT6t02QqdGHEQt1wAjAAAAAAGj\"","Id":"AAMkADQ1MTU4OGJlLTczYzQtNDBlOS1hN2E1LWYyOTdkNmEzM2NhMQBGAAAAAAAQSU6klR_CS7f2u_Zotqz3BwCK45XT6t02QqdGHEQt1wAjAAAAAAEMAACK45XT6t02QqdGHEQt1wAjAAAAAAF6AAA="}}]}
    
    
    
    
}
