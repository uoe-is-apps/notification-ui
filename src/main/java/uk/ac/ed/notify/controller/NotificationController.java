package uk.ac.ed.notify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.bind.annotation.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.client.RestTemplate;
import uk.ac.ed.notify.entity.JsonNotification;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.ac.ed.notify.entity.Notification;
import uk.ac.ed.notify.entity.NotificationUser;
import uk.ac.ed.notify.entity.NotificationUserPK;
import uk.ac.ed.notify.service.GroupService;

/**
 * Created by rgood on 28/10/2015.
 */
@RestController
public class NotificationController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Value("${zuul.routes.resource.url}")
    private String notificationMsUrl;

    /**
     * Username for the 'notification-ui' (service) user.
     */
    @Value("${uk.ac.ed.notify.security.notificationUiUsername:notification-ui}")
    private String notificationUiUsername;

    /**
     * Password for the 'notification-ui' (service) user.
     */
    @Value("${uk.ac.ed.notify.security.notificationUiPassword:CHANGEME}")
    private String notificationUiPassword;

    @Value("${uk.ac.ed.notify.groups.minLevel:6}")
    private int minGroupLevel;

    @Autowired
    private GroupService groupService;

    private HttpHeaders basicAuthHeaders;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void init() {

        // Basic AuthN
        String plainCreds = notificationUiUsername + ":" + notificationUiPassword;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encode(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        basicAuthHeaders = new HttpHeaders();
        basicAuthHeaders.add("Authorization", "Basic " + base64Creds);

    }

    @RequestMapping(value = "/healthcheck", method = RequestMethod.GET)
    public JsonNotification healthcheck(HttpServletRequest httpRequest) throws ServletException {
        logger.info("healthcheck");             
        JsonNotification result = new JsonNotification();
     
        String serverHostname;
        try {
           java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
           serverHostname = localMachine.getHostName();
        } catch(Exception e) {
           serverHostname = "Unknown";
        }        
        
        result.setUrl(serverHostname);      
        return result;     
    }
        
    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping(value = "/notification/{notification-id}", method = RequestMethod.GET)
    public JsonNotification getNotification(HttpServletRequest httpRequest, @PathVariable("notification-id") String notificationId) {
        logger.info("getNotification called by [" + httpRequest.getRemoteUser() + "]");        
        logger.info("notificationId - " + notificationId);
        final HttpEntity<String> entity = new HttpEntity<>(basicAuthHeaders);
        ResponseEntity<JsonNotification> response = restTemplate.exchange(
                notificationMsUrl + "/notification/" + notificationId,
                HttpMethod.GET,
                entity,
                JsonNotification.class);
        logger.info("response - " + response);
        return response.getBody();
    }

    @RequestMapping(value = "/notifications/publisher/{publisher-id}", method = RequestMethod.GET)
    public JsonNotification[] getPublisherNotifications(HttpServletRequest httpRequest, @PathVariable("publisher-id") String publisherId) {
        logger.info("getPublisherNotifications called by [" + httpRequest.getRemoteUser() + "]");
        logger.info("publisherId - " + publisherId);
        final HttpEntity<String> entity = new HttpEntity<>(basicAuthHeaders);
        ResponseEntity<JsonNotification[]> response = restTemplate.exchange(
                notificationMsUrl + "/notifications/publisher/" + publisherId,
                HttpMethod.GET,
                entity,
                JsonNotification[].class);
        logger.info("response - " + response);
        return response.getBody();
    }


    @RequestMapping(value="/notification/", method=RequestMethod.POST)
    public JsonNotification setNotification(HttpServletRequest httpRequest, @RequestBody JsonNotification notification) {
        logger.info("setNotification called by [" + httpRequest.getRemoteUser() + "]");
        logger.info("notification - " + notification);       
                
        if(notification.getNotificationGroup() != null) {
            String ldapGroup = notification.getNotificationGroup();
            int numOfLevel = countNumberOfOccurrences("ou=",ldapGroup);
            if(numOfLevel < minGroupLevel) {
                JsonNotification result = new JsonNotification();
                result.setTitle("ERROR_GROUP_NOTIFICATION_CREATION_INCORRECT_LEVEL");
                logger.error("ladp group - " + ldapGroup + " num of ou: " + numOfLevel);
                logger.error("notification - ERROR_GROUP_NOTIFICATION_CREATION_INCORRECT_LEVEL");      
                return result;                
            }
        }

        notification = constructNotificationWithLdapGroup(notification);    

        if(notification.getTopic().equals("Group") && notification.getNotificationUsers() != null) {
            if(notification.getNotificationUsers().size() == 0) {
                JsonNotification result = new JsonNotification();
                result.setTitle("ERROR_GROUP_NOTIFICATION_CREATION_NO_MEMBER");
                logger.error("notification - ERROR_GROUP_NOTIFICATION_CREATION_NO_MEMBER");      
                return result;
            }else if(notification.getNotificationUsers().size() > 5000) {
                JsonNotification result = new JsonNotification();
                result.setTitle("ERROR_GROUP_NOTIFICATION_CREATION_TOO_MANY_MEMBER");
                logger.error("notification - ERROR_GROUP_NOTIFICATION_CREATION_TOO_MANY_MEMBER");      
                return result;
            }
        }
        
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.putAll(basicAuthHeaders);
            HttpEntity entity= new HttpEntity(notification, headers);
            ResponseEntity<JsonNotification> response = restTemplate.exchange(
                    notificationMsUrl + "/notification/",
                    HttpMethod.POST,
                    entity,
                    JsonNotification.class);
            logger.info("response - " + response);
            return response.getBody();
        } catch(Exception e) {
            JsonNotification result = new JsonNotification();
            result.setTitle("ERROR_NOTIFICATION_CREATION");
            logger.error("notification - ERROR_NOTIFICATION_CREATION - " + e.toString());      
            return result;
        }        

    }

    @RequestMapping(value="/notification/{notification-id}",method=RequestMethod.PUT)
    public JsonNotification updateNotification(HttpServletRequest httpRequest, @PathVariable("notification-id") String notificationId, @RequestBody JsonNotification notification) throws ServletException {       
        logger.info("updateNotification called by [" + httpRequest.getRemoteUser() + "]");
        logger.info("notificationId - " + notificationId);      
        logger.info("notification - " + notification);      
        notification = constructNotificationWithLdapGroup(notification);   
        
        if(notification.getTopic().equals("Group") && notification.getNotificationUsers() != null) {
            if(notification.getNotificationUsers().size() == 0) {
                JsonNotification result = new JsonNotification();
                result.setTitle("ERROR_GROUP_NOTIFICATION_CREATION_NO_MEMBER");
                logger.error("notification - ERROR_GROUP_NOTIFICATION_CREATION_NO_MEMBER");      
                return result;
            }else if(notification.getNotificationUsers().size() > 5000) {
                JsonNotification result = new JsonNotification();
                result.setTitle("ERROR_GROUP_NOTIFICATION_CREATION_TOO_MANY_MEMBER");
                logger.error("notification - ERROR_GROUP_NOTIFICATION_CREATION_TOO_MANY_MEMBER");      
                return result;
            }
        }
                        
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.putAll(basicAuthHeaders);
            HttpEntity entity = new HttpEntity(notification, headers);
            ResponseEntity<JsonNotification> response = restTemplate.exchange(notificationMsUrl + "/notification/"+notificationId, HttpMethod.PUT, entity, JsonNotification.class);
            logger.info("response - " + response);        
            return response.getBody();
        } catch(Exception e) {
            JsonNotification result = new JsonNotification();
            result.setTitle("ERROR_NOTIFICATION_CREATION");
            logger.error("notification - ERROR_NOTIFICATION_CREATION - " + e.toString());      
            return result;
        }
    }

    @RequestMapping(value="/notification/{notification-id}",method=RequestMethod.DELETE)
    public void deleteNotification(HttpServletRequest httpRequest, @PathVariable("notification-id") String notificationId) {
        logger.info("deleteNotification called by [" + httpRequest.getRemoteUser() + "]");
        logger.info("notificationId - " + notificationId);      
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.putAll(basicAuthHeaders);
        HttpEntity entity = new HttpEntity("", headers);
        ResponseEntity<String> response = restTemplate.exchange(notificationMsUrl + "/notification/" + notificationId, HttpMethod.DELETE, entity, String.class);
        logger.info("response - " + response);
    }
    
    @RequestMapping(value="/notifications/user/{uun}", method= RequestMethod.GET)
    public JsonNotification[] getUserNotifications(HttpServletRequest httpRequest, @PathVariable("uun") String uun) {
        logger.info("getUserNotifications called by [" + httpRequest.getRemoteUser() + "]");
        logger.info("notificationId - " + uun);
        final HttpEntity<String> entity = new HttpEntity<>(basicAuthHeaders);
    	ResponseEntity<JsonNotification[]> response = restTemplate.exchange(
    	        notificationMsUrl + "/notifications/user/" + uun,
                HttpMethod.GET,
                entity,
                JsonNotification[].class);
        logger.info("response - " + response);
        return response.getBody();
    }

    @RequestMapping(value = "/checkIfLdapGroupContainMember/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String checkIfLdapGroupContainMember(@RequestBody JsonNotification notification) {        
        if(notification.getNotificationGroup() != null) {
            List<String> ldapUsers = groupService.getMembers(notification.getNotificationGroup());
            if(ldapUsers.size() > 0) {
                return "{\"member\": \"yes\"}";
            }
        }
        return "{\"member\": \"no\"}";
    }    
    
    private JsonNotification constructNotificationWithLdapGroup(JsonNotification notification) {
        logger.info("constructNotificationWithLdapGroup - " + notification);
        
        if(notification.getNotificationGroup() == null) {
            logger.info("user hasn't selected any ldap group");
            return notification;
        } else {
            String groupName = groupService.getGroupName(notification.getNotificationGroup());
            notification.setNotificationGroupName(groupName);

            Notification dbNotification = new Notification();
            dbNotification.setBody(notification.getBody());
            dbNotification.setEndDate(notification.getEndDate());
            dbNotification.setNotificationId(notification.getNotificationId());
            dbNotification.setPublisherId(notification.getPublisherId());
            dbNotification.setPublisherNotificationId(notification.getPublisherNotificationId());
            dbNotification.setStartDate(notification.getStartDate());
            dbNotification.setTitle(notification.getTitle());
            dbNotification.setTopic(notification.getTopic());
            dbNotification.setUrl(notification.getUrl());
            dbNotification.setNotificationGroup(notification.getNotificationGroup()); 
            dbNotification.setNotificationGroupName(groupName);

            List<String> ldapUsers = groupService.getMembersFromParentGroup(notification.getNotificationGroup());

            logger.info("notification will be created for the following users");
            logger.info("getMembersFromParentGroup - " + notification.getNotificationGroup()+ " name - " + groupName + " numOfUsers found - " + ldapUsers.size() );
            for(int i = 0; i < ldapUsers.size(); i++) {
                logger.info("ldapUsers - " + i + " " + ldapUsers.get(i));                  
            } 
            
            ArrayList<NotificationUser> userList = new ArrayList<>();
            for(String ldapUser : ldapUsers) {
                if (!ldapUser.contains("/") && !ldapUser.contains("_")) {
                    NotificationUser user = new NotificationUser();

                    NotificationUserPK nupk = new NotificationUserPK();
                    nupk.setNotificationId(notification.getNotificationId());
                    nupk.setUun(ldapUser);
                    user.setId(nupk);
                    user.setNotification(dbNotification);

                    userList.add(user);
                }
            }
            notification.setNotificationUsers(userList);

            return notification;
        }
    }
    
        
    private int countNumberOfOccurrences(String source, String sentence) {
        int i=0;
        Pattern p = Pattern.compile(source);
        Matcher m = p.matcher(sentence);
        while (m.find()) {
            i++;
        }        
        return i;
    }    

}
