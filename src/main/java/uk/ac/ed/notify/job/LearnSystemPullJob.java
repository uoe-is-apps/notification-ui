package uk.ac.ed.notify.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ed.notify.service.LearnService;

//DTI020-109 Remove Learn Integration
public class LearnSystemPullJob{}

/*
@DisallowConcurrentExecution
public class LearnSystemPullJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(LearnSystemPullJob.class);
      
    @Autowired
    LearnService learnService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //if(true) return;
        logger.info("LearnSystemPullJob started...........................................................scheduled jobs starts");
        learnService.pullLearnNotifications(true);
        logger.info ("LearnSystemPullJob finished successfully............................................scheduled jobs completes");
    }
}
*/