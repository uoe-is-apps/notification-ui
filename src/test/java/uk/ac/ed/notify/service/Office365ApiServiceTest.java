/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import java.text.ParseException;
import java.util.Hashtable;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ed.notify.TestApplication;
import uk.ac.ed.notify.entity.Notification;
import uk.ac.ed.notify.entity.PublisherDetails;
import uk.ac.ed.notify.repository.NotificationRepository;
import uk.ac.ed.notify.repository.PublisherDetailsRepository;

/**
 *
 * @author hsun1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
public class Office365ApiServiceTest {

    @Autowired
    EmailNotificationHandlingService emailNotificationHandlingService;    
    
    @Autowired 
    Office365JsonService office365JsonService;
    
    @Autowired 
    Office365ApiService office365ApiService;    
    
    @Autowired
    NotificationRepository notificationRepository;    
    
    @Autowired
    PublisherDetailsRepository publisherDetailsRepository;        
    
    @Before
    public void setup()
    {
        Notification notification = new Notification();
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("1");
        notification.setTitle("title");
        notification.setBody("body");                
        notification.setUun("existing");
        notificationRepository.save(notification);

        PublisherDetails publisher = new PublisherDetails();
        publisher.setPublisherId("learn");
        publisher.setPublisherType("BOTH");
        publisher.setKey("valid");
        
        publisherDetailsRepository.save(publisher);
    }

    @After
    public void cleanup()
    {
        notificationRepository.deleteAll();
    }    
    
    @Test
    public void testProcessSingleNotificationValidKeyInsert() throws ParseException {
        
        Notification notification = new Notification();
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("3");
        notification.setTitle("title");
        notification.setBody("body");                
        notification.setUun("existing");
        notification.setPublisherKey("valid");
        notification.setAction("insert");
        emailNotificationHandlingService.processSingleNotification(notification);
                
        assertEquals(2,notificationRepository.count()); 
    }    
    
    @Test
    public void testProcessSingleNotificationValidKeyUpdateAsInsertDueToNoExistingNotificationFound() throws ParseException {
        
        Notification notification = new Notification();
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("3");
        notification.setTitle("title");
        notification.setBody("body");                
        notification.setUun("existing");
        notification.setPublisherKey("valid");
        notification.setAction("update");
        emailNotificationHandlingService.processSingleNotification(notification);
                
        assertEquals(2,notificationRepository.count()); 
    }       
    
    
    @Test
    public void testProcessSingleNotificationValidKeyUpdate() throws ParseException {
        
        Notification notification = new Notification();
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("1");
        notification.setTitle("title");
        notification.setBody("body");                
        notification.setUun("existing");
        notification.setPublisherKey("valid");
        notification.setAction("update");
        emailNotificationHandlingService.processSingleNotification(notification);
                
        assertEquals(1,notificationRepository.count()); 
    }      
    
    
    @Test
    public void testProcessSingleNotificationInvalidKeyNoInsert() throws ParseException {
        
        Notification notification = new Notification();
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("3");
        notification.setTitle("title");
        notification.setBody("body");                
        notification.setUun("existing");
        notification.setPublisherKey("invalid");
        emailNotificationHandlingService.processSingleNotification(notification);
                
        assertEquals(1,notificationRepository.count()); 
    }    
    
    
    @Test
    public void testProcessSingleNotificationValidKeyDelete() throws ParseException {
        
        Notification notification = new Notification();
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("1");
        notification.setTitle("title");
        notification.setBody("body");                
        notification.setUun("existing");
        notification.setPublisherKey("valid");
        notification.setAction("delete");
        emailNotificationHandlingService.processSingleNotification(notification);
                
        assertEquals(0,notificationRepository.count()); 
    }        
    
    /*
    //only run this test when developing on a desktop pc, no need to run on server
    @Test
    public void testToken() throws ParseException {         
        String token = office365ApiService.acquireAccessToken();
        Boolean res = token.length() > 0;        
        assertTrue(res); 
    }      
    */
   
}
