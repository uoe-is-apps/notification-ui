/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ed.notify.service.NotificationTidyupService;

/**
 * Job to implement the NotificationTidyupJob
 * Created by rgood on 05/10/15.
 */
@DisallowConcurrentExecution
public class NotificationTidyupJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(NotificationTidyupJob.class);
     
    @Autowired
    NotificationTidyupService notificationTidyupService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //if(true) return;
        logger.info("NotificationTidyupJob started.............................scheduled jobs starts");
        notificationTidyupService.tidyupNotification();
        logger.info ("NotificationTidyupJob finished successfully..............scheduled jobs completes");
    }
}
