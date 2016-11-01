/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import static junit.framework.Assert.assertEquals;
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

/**
 *
 * @author hsun1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
public class NotificationTidyupServiceTest {

    @Autowired
    NotificationRepository notificationRepository;
	
    @Autowired
    NotificationTidyupService notificationTidyupService;
    
    @Before
    public void setup()
    {
        Notification notificationNotInclude = new Notification();
        notificationNotInclude.setTitle("title");
        notificationNotInclude.setBody("body");
        notificationNotInclude.setStartDate(parseDate("2099-01-01"));
        notificationNotInclude.setEndDate(parseDate("2099-01-02"));
    	notificationRepository.save(notificationNotInclude);
        
        Notification notificationInclude1 = new Notification();
        notificationInclude1.setTitle("title");
        notificationInclude1.setBody("body");
        notificationInclude1.setStartDate(parseDate("2001-01-01"));
        notificationInclude1.setEndDate(parseDate("2001-01-02"));
    	notificationRepository.save(notificationInclude1);     
        
        Notification notificationNotInclude2 = new Notification();
        notificationNotInclude2.setTitle("title");
        notificationNotInclude2.setBody("body");
        notificationNotInclude2.setStartDate(parseDate("2001-01-01"));
        notificationNotInclude2.setEndDate(null);
    	notificationRepository.save(notificationNotInclude2);           
    }

    @After
    public void cleanup()
    {
        notificationRepository.deleteAll();
    }
    
    @Test 
    public void testRepositorySave() {       
        assertEquals(new Integer("3"),new Integer(notificationRepository.count() + ""));
    }
    	    
    @Test 
    public void test() {    
        List<Notification> list = notificationTidyupService.findDeletableNotification();
        assertEquals(new Integer("1"), new Integer(list.size() + ""));
    }    
            
    private Date parseDate(String dateString){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
          Date date = format.parse(dateString);
          return date;
        }catch(Exception e){
            return null;
        }
    }
    
}
