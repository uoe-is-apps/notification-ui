package uk.ac.ed.notify.job;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ed.notify.controller.Office365CallbackController;
import uk.ac.ed.notify.entity.DatePartSerializer;
import uk.ac.ed.notify.entity.Notification;
import uk.ac.ed.notify.learn.entity.Announcements;
import uk.ac.ed.notify.learn.entity.CourseMain;
import uk.ac.ed.notify.learn.entity.CourseUsers;
import uk.ac.ed.notify.learn.entity.GradebookMain;
import uk.ac.ed.notify.learn.entity.Tasks;
import uk.ac.ed.notify.learn.entity.Users;
import uk.ac.ed.notify.learn.repository.LearnAnnouncementRepository;
import uk.ac.ed.notify.learn.repository.LearnAssessmentRepository;
import uk.ac.ed.notify.learn.repository.LearnCourseUserRepository;
import uk.ac.ed.notify.learn.repository.LearnTaskRepository;
import uk.ac.ed.notify.learn.repository.LearnUserRepository;
import uk.ac.ed.notify.repository.NotificationRepository;
import uk.ac.ed.notify.service.LearnService;
import uk.ac.ed.notify.service.Office365ApiService;

/**
 * Job to implement the Learn pull
 * Created by rgood on 05/10/15.
 */
public class LearnPullJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(LearnPullJob.class);
     
    @Autowired
    LearnService learnService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if(true) return;
        logger.debug("LearnPullJob");        
        learnService.pullLearnNotifications();
    }
}
