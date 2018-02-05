package uk.ac.ed.notify.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.bind.annotation.*;

import uk.ac.ed.notify.entity.JsonNotification;

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
import uk.ac.ed.notify.service.LdapService;

/**
 * Created by rgood on 28/10/2015.
 */
@RestController
public class NotificationController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Value("${zuul.routes.resource.url}")
    private String notificationMsUrl;

    private String clientSecret;
    private String tokenUrl;
    private String clientId;

    @Autowired
    public NotificationController( @Value("${spring.oauth2.client.clientSecret}") String clientSecret,
                                   @Value("${spring.oauth2.client.accessTokenUri}") String tokenUrl,
                                   @Value("${spring.oauth2.client.clientId}") String clientId) {
        this.clientId=clientId;
        this.clientSecret=clientSecret;
        this.tokenUrl=tokenUrl;
        restTemplate = new OAuth2RestTemplate(resource());
    }

    private OAuth2RestTemplate restTemplate;

    protected OAuth2ProtectedResourceDetails resource() {
        ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
        resource.setAccessTokenUri(tokenUrl);
        resource.setClientSecret(clientSecret);
        resource.setClientId(clientId);
        return resource;
    }


    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping(value = "/notification/{notification-id}", method = RequestMethod.GET)
    public JsonNotification getNotification(HttpServletRequest httpRequest, @PathVariable("notification-id") String notificationId) throws ServletException {
        logger.info("getNotification called by [" + httpRequest.getRemoteUser() + "]");        
        logger.info("notificationId - " + notificationId);        
        ResponseEntity<JsonNotification> response = restTemplate.getForEntity(notificationMsUrl + "/notification/" + notificationId, JsonNotification.class);
        logger.info("response - " + response);
        return response.getBody();
    }

    @Autowired
    LdapService ldapService;        

    @RequestMapping(value = "/notifications/publisher/{publisher-id}", method = RequestMethod.GET)
    public JsonNotification[] getPublisherNotifications(HttpServletRequest httpRequest, @PathVariable("publisher-id") String publisherId) throws ServletException {
        logger.info("getPublisherNotifications called by [" + httpRequest.getRemoteUser() + "]");
        logger.info("publisherId - " + publisherId);        
        ResponseEntity<JsonNotification[]> response = restTemplate.getForEntity(notificationMsUrl + "/notifications/publisher/" + publisherId, JsonNotification[].class);
        logger.info("response - " + response);
        return response.getBody();
    }


    @RequestMapping(value="/notification/", method=RequestMethod.POST)
    public JsonNotification setNotification(HttpServletRequest httpRequest, @RequestBody JsonNotification notification) throws ServletException, JsonProcessingException {              
        logger.info("setNotification called by [" + httpRequest.getRemoteUser() + "]");
        logger.info("notification - " + notification);        
        notification = constructNotificationWithLdapGroup(notification);        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request= new HttpEntity(notification, headers);
        ResponseEntity<JsonNotification> response = restTemplate.exchange(notificationMsUrl + "/notification/", HttpMethod.POST, request, JsonNotification.class);
        logger.info("response - " + response);
        return response.getBody();

    }

    @RequestMapping(value="/notification/{notification-id}",method=RequestMethod.PUT)
    public void updateNotification(HttpServletRequest httpRequest, @PathVariable("notification-id") String notificationId, @RequestBody JsonNotification notification) throws ServletException {       
        logger.info("updateNotification called by [" + httpRequest.getRemoteUser() + "]");
        logger.info("notificationId - " + notificationId);      
        logger.info("notification - " + notification);      
        notification = constructNotificationWithLdapGroup(notification);        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request= new HttpEntity(notification, headers);
        ResponseEntity<JsonNotification> response = restTemplate.exchange(notificationMsUrl + "/notification/"+notificationId, HttpMethod.PUT, request, JsonNotification.class);
        logger.info("response - " + response);
    }

    @RequestMapping(value="/notification/{notification-id}",method=RequestMethod.DELETE)
    public void deleteNotification(HttpServletRequest httpRequest, @PathVariable("notification-id") String notificationId) throws ServletException {
        logger.info("deleteNotification called by [" + httpRequest.getRemoteUser() + "]");
        logger.info("notificationId - " + notificationId);      
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request= new HttpEntity("", headers);
        ResponseEntity<String> response = restTemplate.exchange(notificationMsUrl + "/notification/"+notificationId, HttpMethod.DELETE, request, String.class);
        logger.info("response - " + response);
    }
    
    @RequestMapping(value="/notifications/user/{uun}", method= RequestMethod.GET)
    public JsonNotification[] getUserNotifications(HttpServletRequest httpRequest, @PathVariable("uun") String uun) {
        logger.info("getUserNotifications called by [" + httpRequest.getRemoteUser() + "]");
        logger.info("notificationId - " + uun);      
    	ResponseEntity<JsonNotification[]> response = restTemplate.getForEntity(notificationMsUrl + "/notifications/user/" + uun, JsonNotification[].class);
        logger.info("response - " + response);
        return response.getBody();
    }

    @RequestMapping(value = "/checkIfLdapGroupContainMember/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String checkIfLdapGroupContainMember(@RequestBody JsonNotification notification) {
        if(notification.getNotificationGroup() != null){
            List<String> ldapUsers = ldapService.getMembers(notification.getNotificationGroup());
            if(ldapUsers.size() > 0){
                return "{\"member\": \"yes\"}";
            }
        }
        return "{\"member\": \"no\"}";
    }    
    
    private JsonNotification constructNotificationWithLdapGroup(JsonNotification notification){
        logger.info("constructNotificationWithLdapGroup - " + notification);
        
        if(notification.getNotificationGroup() == null){
            logger.info("user hasn't selected any ldap group");
            return notification;
        }else{        
            String groupName = ldapService.getGroupName(notification.getNotificationGroup()); 
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

            List<String> ldapUsers = ldapService.getMembersFromParentGroup(notification.getNotificationGroup());

            logger.info("getMembersFromParentGroup - " + notification.getNotificationGroup()+ " name - " + groupName + " numOfUsers found - " + ldapUsers.size() );
            for(int i = 0; i < ldapUsers.size(); i++){
                System.out.println("ldapUsers - " + i + " " + ldapUsers.get(i));                  
            } 
            
            ArrayList<NotificationUser> userList = new ArrayList<NotificationUser>();
            for(int i = 0; i < ldapUsers.size(); i++){
               NotificationUser user = new NotificationUser();

               NotificationUserPK nupk = new NotificationUserPK();
               nupk.setNotificationId(notification.getNotificationId());
               nupk.setUun(ldapUsers.get(i));
               user.setId(nupk);
               user.setNotification(dbNotification);

               userList.add(user);
            }
            notification.setNotificationUsers(userList);

            return notification;
        }
    }
    
        
}
