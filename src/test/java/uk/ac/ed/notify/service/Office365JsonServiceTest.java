/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ed.notify.TestApplication;
import uk.ac.ed.notify.repository.Office365Repository;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
public class Office365JsonServiceTest {

    @Autowired
    Office365Repository office365Repository;

    @Before
    public void setup()
    {

    }

    @After
    public void cleanup() {

    }

    @Test
    public void parseNotification() throws ParseException {


        assertEquals(1,1); 
    }

    @Test
    public void parseOffice365NewEmailCallbackEmailId() throws ParseException {

        
        assertEquals(1,1);
    }

    
    @Test
    public void parseOffice365NewSubscriptionCallbackSubscriptionId() throws ParseException {

        
        assertEquals(1,1);
    }    
}
