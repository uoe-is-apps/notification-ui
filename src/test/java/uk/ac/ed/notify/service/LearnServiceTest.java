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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

    private Date dateNow;

    @Before
    public void setup()
    {
        dateNow = new Date();
        Notification notification = new Notification();
        notification.setBody("TestBody");
        notification.setTitle("testtitle");
        notification.setStartDate(dateNow);
        notification.setPublisherNotificationId("1");
        notification.setPublisherId("learn");
        notificationRepository.save(notification);

    }

    @After
    public void cleanup()
    {
        notificationRepository.deleteAll();
    }

    @Test
    public void testIfInsertLearnNotification()
    {
        Notification notification = new Notification();
        notification.setBody("TestBody");
        notification.setTitle("testtitle");
        notification.setStartDate(dateNow);
        notification.setPublisherNotificationId("1");
        notification.setPublisherId("learn");
        assertFalse(learnService.ifInsertLearnNotification("learn","1",notification));

        notification.setTitle("newtitle");

        assertTrue(learnService.ifInsertLearnNotification("learn","1",notification));
    }

}
