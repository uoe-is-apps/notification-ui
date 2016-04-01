package uk.ac.ed.notify.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ed.notify.service.LearnService;


/**
 * Job to implement the Learn pull
 * Created by rgood on 05/10/15.
 */
@DisallowConcurrentExecution
public class LearnPullJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(LearnPullJob.class);
     
    @Autowired
    LearnService learnService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //if(true) return;
        logger.info("LearnPullJob started");
        learnService.pullLearnNotifications();
        logger.info ("LearnPullJob finished successfully");
    }
}
