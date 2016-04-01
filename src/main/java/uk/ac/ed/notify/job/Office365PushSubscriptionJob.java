package uk.ac.ed.notify.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ed.notify.entity.Office365Subscription;
import uk.ac.ed.notify.repository.Office365Repository;
import uk.ac.ed.notify.service.Office365ApiService;

/**
 * Job to handle the Office 365 Push Subscription refresh
 * Created by rgood on 05/10/15.
 */
@DisallowConcurrentExecution
public class Office365PushSubscriptionJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(Office365PushSubscriptionJob.class);   
    
    @Autowired
    Office365ApiService office365ApiService;
    
    @Autowired
    Office365Repository office365Repository;
    
    
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //if(true) return;
        logger.info("Office365PushSubscriptionJob started");
                
        String token = office365ApiService.acquireAccessToken();  
        logger.debug("token - " + token);        
        //if no previous subscription, subscribe
        if(office365Repository.count() == 0){
            logger.debug("no prevous subscription found, subscribe now");       
            office365ApiService.subscribeToNotification(token);
        }else{//if found previous subscription, renew
            logger.debug("prevous subscription found, delete it now");     
            Iterable<Office365Subscription> existingSubscriptions = office365Repository.findAll();
            if(existingSubscriptions.iterator().hasNext()){
                Office365Subscription office365Subscription = existingSubscriptions.iterator().next();              
                office365ApiService.deleteSubscriptionById(token, office365Subscription.getSubscriptionId());
            }
            
            logger.debug("create new subscription now");
            office365ApiService.subscribeToNotification(token);
            logger.info("Office365PushSubscriptionJob finished successfully.");
            
            /* delete later, renew API is broken on office 365
            logger.debug("prevous subscription found, renew now");     
            Iterable<Office365Subscription> existingSubscriptions = office365Repository.findAll();
            if(existingSubscriptions.iterator().hasNext()){
                Office365Subscription office365Subscription = existingSubscriptions.iterator().next();                                        
                office365ApiService.renewSubscriptionToNotification(token, office365Subscription.getSubscriptionId());
            } 
            */
        }
    }
}
