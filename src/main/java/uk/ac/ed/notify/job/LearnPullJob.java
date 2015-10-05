package uk.ac.ed.notify.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Job to implement the Learn pull
 * Created by rgood on 05/10/15.
 */
public class LearnPullJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("LearnPullJob called");

    }
}
