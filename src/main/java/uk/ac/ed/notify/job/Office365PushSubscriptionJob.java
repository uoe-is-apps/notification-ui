package uk.ac.ed.notify.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Job to handle the Office 365 Push Subscription refresh
 * Created by rgood on 05/10/15.
 */
public class Office365PushSubscriptionJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        System.out.println("Office365PushSubscriptionJob called");

    }
}
