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
import uk.ac.ed.notify.entity.Notification;

import javax.servlet.ServletException;

import java.security.Principal;
import java.util.List;

/**
 * Created by rgood on 28/10/2015.
 */
@RestController
public class NotificationController {

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
    public
    @ResponseBody
    JsonNotification getNotification(@PathVariable("notification-id") String notificationId) throws ServletException {
        ResponseEntity<JsonNotification> response = restTemplate.getForEntity(notificationMsUrl + "/" + notificationId, JsonNotification.class);

        return response.getBody();
    }

    @RequestMapping(value = "/notification/publisher/{publisher-id}", method = RequestMethod.GET)
    public
    @ResponseBody
    JsonNotification[] getPublisherNotifications(@PathVariable("publisher-id") String publisherId) throws ServletException {

        ResponseEntity<JsonNotification[]> response = restTemplate.getForEntity(notificationMsUrl + "/publisher/" + publisherId, JsonNotification[].class);
        return response.getBody();
    }


    @RequestMapping(value="/notification/", method=RequestMethod.POST)
    public @ResponseBody
    JsonNotification setNotification(@RequestBody JsonNotification notification) throws ServletException, JsonProcessingException {
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request= new HttpEntity(notification, headers);
        ResponseEntity<JsonNotification> response = restTemplate.exchange(notificationMsUrl + "/", HttpMethod.POST, request, JsonNotification.class);
        return response.getBody();

    }

    @RequestMapping(value="/notification/{notification-id}",method=RequestMethod.PUT)
    public void updateNotification(@PathVariable("notification-id") String notificationId, @RequestBody JsonNotification notification) throws ServletException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request= new HttpEntity(notification, headers);
        ResponseEntity<JsonNotification> response = restTemplate.exchange(notificationMsUrl + "/"+notificationId, HttpMethod.PUT, request, JsonNotification.class);
    }

    @RequestMapping(value="/notification/{notification-id}",method=RequestMethod.DELETE)
    public void deleteNotification(@PathVariable("notification-id") String notificationId) throws ServletException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request= new HttpEntity("", headers);
        ResponseEntity<String> response = restTemplate.exchange(notificationMsUrl + "/"+notificationId, HttpMethod.DELETE, request, String.class);
    }
    
    @RequestMapping(value="/notification/user/{uun}", method= RequestMethod.GET)
    public JsonNotification[] getUserNotifications(@PathVariable("uun") String uun) {
    	
    	ResponseEntity<JsonNotification[]> response = restTemplate.getForEntity(notificationMsUrl + "/user/" + uun, JsonNotification[].class);
        return response.getBody();
    }

}
