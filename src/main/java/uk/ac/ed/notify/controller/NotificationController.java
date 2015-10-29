package uk.ac.ed.notify.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.ac.ed.notify.entity.Notification;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;

/**
 * Created by rgood on 28/10/2015.
 */
@RestController
public class NotificationController {


    @Value("${spring.oauth2.client.clientSecret}")
    private String clientSecret;

    @Value("${spring.oauth2.client.accessTokenUri}")
    private String tokenUrl;

    @Value("${spring.oauth2.client.clientId}")
    private String clientId;

    @Value("${zuul.routes.resource.url}")
    private String notificationMsUrl;

    public NotificationController() {
        System.out.println("init");
        restTemplate = new OAuth2RestTemplate(resource());
    }

    private OAuth2RestTemplate restTemplate;

    protected OAuth2ProtectedResourceDetails resource() {
        ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
        resource.setAccessTokenUri("https://dev.oauth.ws-apps.is.ed.ac.uk:443/oauth/token");
        resource.setClientSecret("s1llycrash3s");
        resource.setClientId("notification-ui");
        System.out.println("returning resource:" + resource.getClientId());
        return resource;
    }


    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping(value = "/notification/{notification-id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Notification getNotification(@PathVariable("notification-id") String notificationId) throws ServletException {
        System.out.println(restTemplate.getResource().getClientSecret());
        ResponseEntity<Notification> response = restTemplate.getForEntity(notificationMsUrl + "/" + notificationId, Notification.class);

        return response.getBody();
    }

    @RequestMapping(value = "/notification/publisher/{publisher-id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Notification[] getPublisherNotifications(@PathVariable("publisher-id") String publisherId) throws ServletException {

        ResponseEntity<Notification[]> response = restTemplate.getForEntity(notificationMsUrl + "/publisher/" + publisherId, Notification[].class);
        return response.getBody();
    }


    @RequestMapping(value="/notification/", method=RequestMethod.POST)
    public @ResponseBody Notification setNotification(@RequestBody String notification) throws ServletException, JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request= new HttpEntity(notification, headers);
        ResponseEntity<Notification> response = restTemplate.exchange(notificationMsUrl + "/", HttpMethod.POST, request, Notification.class);
        return response.getBody();

    }

    @RequestMapping(value="/notification/{notification-id}",method=RequestMethod.PUT)
    public void updateNotification(@PathVariable("notification-id") String notificationId, @RequestBody String notification) throws ServletException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request= new HttpEntity(notification, headers);
        ResponseEntity<Notification> response = restTemplate.exchange(notificationMsUrl + "/"+notificationId, HttpMethod.PUT, request, Notification.class);
    }

    @RequestMapping(value="/notification/{notification-id}",method=RequestMethod.DELETE)
    public void deleteNotification(@PathVariable("notification-id") String notificationId) throws ServletException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity request= new HttpEntity("", headers);
        ResponseEntity<String> response = restTemplate.exchange(notificationMsUrl + "/"+notificationId, HttpMethod.DELETE, request, String.class);
    }

}
