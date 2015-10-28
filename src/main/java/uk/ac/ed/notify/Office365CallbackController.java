/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ed.notify.entity.Office365Subscription;
import uk.ac.ed.notify.repository.Office365Repository;
import uk.ac.ed.notify.service.Office365ApiService;
import uk.ac.ed.notify.service.Office365JsonService;

@RestController
public class Office365CallbackController {
    private static final Logger logger = LoggerFactory.getLogger(Office365CallbackController.class);
    
    @Autowired
    Office365JsonService office365JsonService;        
      
    @Autowired
    Office365ApiService office365ApiService;
    
    @Autowired
    Office365Repository office365Repository;
    
    @RequestMapping(value="/office365NewEmailCallback/", method=RequestMethod.POST)
    public @ResponseBody void office365NewEmailCallback(@RequestBody String json)
    {
        logger.debug("office365NewEmailCallback - " + json); 
        
        if(json.indexOf("Acknowledgment") != -1){
            logger.debug("office365NewSubscriptionCallback - " + json);
                  
            Office365Subscription newSubscription = office365JsonService.parseOffice365NewSubscriptionCallbackSubscriptionId(json); 
            logger.debug("newSubscription - " + newSubscription);
            office365Repository.save(newSubscription);
            logger.debug("subscription successful");
        }else{        
            String token = office365ApiService.acquireAccessToken();             
            String newEmailId = office365JsonService.parseOffice365NewEmailCallbackEmailId(json);
            logger.debug("newEmailId - " + newEmailId);
            office365ApiService.processEmailById(token, newEmailId);
        }
    }    
    
    @RequestMapping(value="/office365NewSubscriptionCallback/", method=RequestMethod.POST)
    public @ResponseBody void office365NewSubscriptionCallback(@RequestBody String json)
    {
        logger.debug("office365NewSubscriptionCallback - " + json);
                  
        Office365Subscription newSubscription = office365JsonService.parseOffice365NewSubscriptionCallbackSubscriptionId(json); 
        office365Repository.save(newSubscription);
    }  
    
    @RequestMapping(value="/office365RenewSubscriptionCallback/", method=RequestMethod.POST)
    public @ResponseBody void office365RenewSubscriptionCallback(@RequestBody String json)
    {
        logger.debug("office365RenewSubscriptionCallback - " + json);
        
        Office365Subscription newSubscription = office365JsonService.parseOffice365NewSubscriptionCallbackSubscriptionId(json);                
        office365Repository.deleteAll();
        office365Repository.save(newSubscription);
    }     
}
