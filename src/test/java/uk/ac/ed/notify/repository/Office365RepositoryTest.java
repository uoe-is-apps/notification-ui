/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.repository;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import static org.junit.Assert.assertEquals;
import uk.ac.ed.notify.TestApplication;
import uk.ac.ed.notify.entity.Office365Subscription;

public class Office365RepositoryTest {}

/*
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestApplication.class)
public class Office365RepositoryTest {

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
    public void testSaveSubscription() throws ParseException {

        Office365Subscription office365Subscription = new Office365Subscription();        
        office365Subscription.setSubscriptionId("123");
        office365Subscription.setSubscriptionExpiry((new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse("2015-10-23T10:32:23.0363654Z")); 
        office365Repository.save(office365Subscription);
        
        assertEquals(1,office365Repository.count());
        
        
    }

    @Test
    public void testReadSubscription() throws ParseException {

        Office365Subscription office365Subscription = new Office365Subscription();        
        office365Subscription.setSubscriptionId("123");
        office365Subscription.setSubscriptionExpiry((new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse("2015-10-23T10:32:23.0363654Z")); 
        office365Repository.save(office365Subscription);
        
        Office365Subscription office365SubscriptionInDB = office365Repository.findAll().iterator().next();
        
        assertEquals(office365SubscriptionInDB.getSubscriptionId(), "123");
    }

}
*/