/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uk.ac.ed.notify.entity.Notification;
import uk.ac.ed.notify.entity.Office365Subscription;

/**
 *
 * @author hsun1
 */
@Service
public class Office365JsonService {
    private static final Logger logger = LoggerFactory.getLogger(Office365JsonService.class); 
    
    public Office365Subscription parseOffice365NewSubscriptionCallbackSubscriptionId(String json) {
         /* v1
         Office365Subscription office365Subscription = null;
         JSONObject jsonObj = new JSONObject(json);
         JSONArray array = jsonObj.getJSONArray("value");
         
         try{
         if(array.length() == 1){ //should always be 1
             office365Subscription = new Office365Subscription();
             
             String id = ((JSONObject)array.get(0)).get("SubscriptionId").toString(); //subscriptionId  //SubscriptionId
             String expiryDate = ((JSONObject)array.get(0)).get("SubscriptionExpirationDateTime").toString(); //subscriptionExpirationDateTime //SubscriptionExpirationTime
             
             office365Subscription.setSubscriptionId(id);
             office365Subscription.setSubscriptionExpiry((new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(expiryDate)); //"2015-10-23T10:32:23.0363654Z"
             office365Subscription.setSubscriptionRenew(new Date());
         }
         }catch(Exception e){
             e.printStackTrace();
         }
         
         return office365Subscription;
         */
        
         Office365Subscription office365Subscription = null;
         JSONObject jsonObj = new JSONObject(json);

         try{
             office365Subscription = new Office365Subscription();             
             String id = jsonObj.get("Id").toString(); //subscriptionId  //SubscriptionId
             String expiryDate = jsonObj.get("SubscriptionExpirationDateTime").toString(); //subscriptionExpirationDateTime //SubscriptionExpirationTime             
             office365Subscription.setSubscriptionId(id);
             office365Subscription.setSubscriptionExpiry((new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(expiryDate)); //"2015-10-23T10:32:23.0363654Z"
             office365Subscription.setSubscriptionRenew(new Date());
         }catch(Exception e){
             logger.error(e.toString());
         }
         
         return office365Subscription;        
    }    
    
    public String parseOffice365NewEmailCallbackEmailId(String json) {
         String id = null;
         JSONObject jsonObj = new JSONObject(json);
         JSONArray array = jsonObj.getJSONArray("value");
         if(array.length() == 1){ //should always be 1
             String entity = ((JSONObject)array.get(0)).get("ResourceData").toString();  //resourceData //Entity
             id = new JSONObject(entity).get("Id").toString();
         }
         
         return id;
    }
    
    public Hashtable<String, Notification> parseTableOfNotification(String json){
         Hashtable<String, Notification> table = new Hashtable<String, Notification>();
         
         JSONObject jsonObj = new JSONObject(json);
         JSONArray array = jsonObj.getJSONArray("value");
         for(int i = 0; i < array.length(); i++){                 
             String id = ((JSONObject)array.get(i)).get("Id").toString();             
             Notification notification = parseNotification(array.get(i).toString());             
             table.put(id, notification);
         }              
         return table;
    }
    
    public Notification parseNotification(String json) {
        
        Notification notification = new Notification();

        JSONObject jsonObj = new JSONObject(json);
        String content = jsonObj.getJSONObject("Body").get("Content").toString();
        try {
            content = content.replaceAll("<script type=\"application\\/ld\\+json\">", "");
            content = content.replaceAll("</script>", "");

            JSONObject jsonNotification = new JSONObject(content);

            //notification.setNotificationId(jsonNotification.getString("")); //auto generate
            notification.setTitle(jsonNotification.getString("title"));
            notification.setBody(jsonNotification.getString("body"));
            notification.setCategory(jsonNotification.getString("topic"));
            notification.setPublisherId(jsonNotification.getString("publisherId"));
            notification.setPublisherNotificationId(jsonNotification.getString("publisherNotificationId"));
            SimpleDateFormat dateFormat = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"));
            notification.setStartDate(dateFormat.parse(jsonNotification.getString("startDate")));
            notification.setEndDate(dateFormat.parse(jsonNotification.getString("endDate")));
            notification.setUrl(jsonNotification.getString("url"));
            notification.setUun(jsonNotification.getString("uun"));
            notification.setLastUpdated(new Date());
        } catch (Exception e) {           
            logger.error(e.toString());
        }

        return notification;
    }
}
