/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.ac.ed.notify.TestApplication;
import uk.ac.ed.notify.entity.PublisherDetails;
import uk.ac.ed.notify.repository.NotificationRepository;
import uk.ac.ed.notify.repository.PublisherDetailsRepository;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author hsun1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
public class EWSServiceTest {

    @Autowired 
    NotificationRepository notificationRepository;
    
    @Autowired
    PublisherDetailsRepository publisherDetailsRepository;
    
    @Autowired 
    EWSService ewsService;
    
    @Before
    public void setup()
    {

    }

    @After
    public void cleanup()
    {
        notificationRepository.deleteAll();
    }    
    
    @Test
    public void testProcessEmailByBody() throws Exception {                

    	PublisherDetails publisher = new PublisherDetails();
        publisher.setPublisherId("learn");
        publisher.setPublisherType("BOTH");
        publisher.setKey("valid");
        publisherDetailsRepository.save(publisher);
    	
        String id = "id1";
        String body = "<script type=\"application/ld+json\">{\"@type\": \"Notification\", \"publisherId\": \"learn\", \"publisherNotificationId\": \"1234567\", \"publisherKey\": \"valid\", \"topic\": \"example category67\", \"title\": \"example title7\",\"body\": \"example body7\", \"url\": \"http://www.ed.ac.uk7\", \"notificationUsers\":[{\"user\":{\"uun\":\"donald\"}},{\"user\":{\"uun\":\"bambi\"}},{\"user\":{\"uun\":\"gozer\"}}], \"startDate\": \"2013-05-15T08:30\", \"endDate\": \"2013-05-20T08:30\", \"action\": \"insert\"}</script>";

        ewsService.processEmailByBody(id, body);

        assertThat(notificationRepository.count(), is(1L));

    }    
}
