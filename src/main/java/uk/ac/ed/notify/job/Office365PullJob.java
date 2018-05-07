package uk.ac.ed.notify.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.ac.ed.notify.service.EWSService;
import uk.ac.ed.notify.service.Office365ApiService;

/**
 * Job to implement the Office365 pull
 * Created by rgood on 05/10/15.
 */
@DisallowConcurrentExecution
public class Office365PullJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(Office365PullJob.class);    
    
    @Autowired
    Office365ApiService office365Service;
    
    @Autowired
    EWSService ewsService;    
    
    @Value("${office365PullJob.source}")
    private String source;        
    
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //if(true) return;
        logger.info("Office365PullJob started, source: " + source + ".............................scheduled jobs start");
        
        if(source.equals("office365")){
            String token = office365Service.acquireAccessToken();                
            if(token.length() > 0) office365Service.processUnreadEmail(token);
            
        }else if(source.equals("exchange")){
            logger.info("fetching email from exchange: " + source + ".............................scheduled jobs start");
            ewsService.processUnreadEmail();
        }
        
        logger.info("Office365PullJob finished successfully" + ".................................scheduled jobs completes");
    }
}
