/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ed.notify.TestApplication;
import uk.ac.ed.notify.repository.NotificationRepository;

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

        String id = "id1";
        String body = "<script type=\"application/ld+json\">{\"@type\": \"Notification\", \"publisherId\": \"1234567\", \"publisherNotificationId\": \"1234567\", \"publisherKey\": \"005AFE5E177048ABE05400144F00F4CC\", \"topic\": \"example category67\", \"title\": \"example title7\",\"body\": \"example body7\", \"url\": \"http://www.ed.ac.uk7\", \"uun\": \"hsun1\", \"startDate\": \"2013-05-15T08:30\", \"endDate\": \"2013-05-20T08:30\", \"action\": \"insert\"}</script>";
        String uun = ewsService.processEmailByBody(id, body);
                
        assertEquals("hsun1", uun); 
    }    

}
