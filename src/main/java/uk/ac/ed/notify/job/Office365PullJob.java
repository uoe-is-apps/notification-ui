package uk.ac.ed.notify.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ed.notify.service.Office365ApiService;

/**
 * Job to implement the Office365 pull
 * Created by rgood on 05/10/15.
 */
public class Office365PullJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(Office365PullJob.class);    
    
    @Autowired
    Office365ApiService office365Service;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if(true) return;
        logger.info("Office365PullJob started");
        String token = office365Service.acquireAccessToken();
        office365Service.processUnreadEmail(token);
        logger.info("Office365PullJob finished succesfully");
    }
}
