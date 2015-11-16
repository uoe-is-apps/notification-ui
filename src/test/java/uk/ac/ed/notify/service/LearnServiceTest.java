package uk.ac.ed.notify.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ed.notify.TestApplication;
import uk.ac.ed.notify.entity.Notification;
import uk.ac.ed.notify.repository.NotificationRepository;

import java.util.Date;
import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import uk.ac.ed.notify.entity.AuditActions;

/**
 * Created by rgood on 06/11/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
public class LearnServiceTest {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    LearnService learnService;

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

    }

    @After
    public void cleanup()
    {
        notificationRepository.deleteAll();
    }


    @Test
    public void testIfSaveLearnNotificationWithoutUUNInsert()
    {
        Notification notification = new Notification();
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("2");
        notification.setTitle("title");
        notification.setBody("body"); 
        assertEquals("insert", learnService.ifSaveLearnNotification("learn","2",notification)); 
    }    
    
    @Test
    public void testIfSaveLearnNotificationWithoutUUNUpdate()
    {
        Notification notification = new Notification();
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("1");
        notification.setTitle("title updated");
        notification.setBody("body"); 
        assertEquals("update", learnService.ifSaveLearnNotification("learn","1",notification)); 
    }      
    
    @Test
    public void testIfSaveLearnNotificationWithoutUUNIgnore()
    {
        Notification notification = new Notification();
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("1");
        notification.setTitle("title");
        notification.setBody("body"); 
        assertEquals("ignore", learnService.ifSaveLearnNotification("learn","1",notification));
    }

    @Test
    public void testIfSaveLearnNotificationWithUUNInsert()
    {
        Notification notification = new Notification();
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("2");
        notification.setTitle("title");
        notification.setBody("body"); 
        notification.setUun("existing");
        assertEquals("insert", learnService.ifSaveLearnNotification("learn","2", "existing", notification)); 
    }    
    
    @Test
    public void testIfSaveLearnNotificationWithUUNUpdate()
    {
        Notification notification = new Notification();
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("1");
        notification.setTitle("title updated");
        notification.setBody("body"); 
        notification.setUun("existing");
        assertEquals("update", learnService.ifSaveLearnNotification("learn","1","existing", notification)); 
    }      
    
    @Test
    public void testIfSaveLearnNotificationWithUUNUpdateCheckDetail()
    {
        Notification notification = new Notification();
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("1");
        notification.setTitle("title updated");
        notification.setBody("body updated"); 
        notification.setUun("existing");
        learnService.ifSaveLearnNotification("learn","1","existing", notification); 
        
        notificationRepository.save(notification);
        Notification existingNotification = notificationRepository.findOne(notification.getNotificationId());
        
        assertEquals("title updated", existingNotification.getTitle());
        assertEquals("body updated", existingNotification.getBody());
    }      
    
    @Test
    public void testIfSaveLearnNotificationWithUUNIgnore()
    {
        Notification notification = new Notification();
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("1");
        notification.setTitle("title");
        notification.setBody("body"); 
        notification.setUun("existing");
        assertEquals("ignore", learnService.ifSaveLearnNotification("learn","1","existing",notification));
    }    
    
    @Test    
    public void testHandleNotificationCreate(){
        Notification notification = new Notification();
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("3");
        notification.setTitle("title");
        notification.setBody("body"); 
        notification.setUun("existing");

        learnService.handleNotification(AuditActions.CREATE_NOTIFICATION, notification); 
        assertEquals(2, notificationRepository.count());
    }
    
    
    @Test    
    public void testHandleNotificationUpdate(){
        Notification notification = new Notification();
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("3");
        notification.setTitle("title");
        notification.setBody("body"); 
        notification.setUun("existing");

        learnService.handleNotification(AuditActions.CREATE_NOTIFICATION, notification); 
        assertEquals(2, notificationRepository.count());
        
        String id = notification.getNotificationId();
        notification.setNotificationId(id);
        notification.setTitle("title update");
                
        learnService.handleNotification(AuditActions.UPDATE_NOTIFICATION, notification); 
        assertEquals(2, notificationRepository.count());
    }    
    
    @Test    
    public void testHandleNotificationDelete(){
        Notification notification = new Notification();
        notification.setPublisherId("learn");
        notification.setPublisherNotificationId("3");
        notification.setTitle("title");
        notification.setBody("body"); 
        notification.setUun("existing");

        learnService.handleNotification(AuditActions.CREATE_NOTIFICATION, notification); 
        assertEquals(2, notificationRepository.count());
        
        String id = notification.getNotificationId();
        notification.setNotificationId(id);
        notification.setTitle("title update");
                
        learnService.handleNotification(AuditActions.DELETE_NOTIFICATION, notification); 
        assertEquals(1, notificationRepository.count());
    }       
    
}
