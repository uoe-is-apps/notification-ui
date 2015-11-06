/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.ac.ed.notify.entity.Notification;
import uk.ac.ed.notify.learn.entity.Announcements;
import uk.ac.ed.notify.learn.entity.CourseUsers;
import uk.ac.ed.notify.learn.entity.GradebookGrade;
import uk.ac.ed.notify.learn.entity.GradebookMain;
import uk.ac.ed.notify.learn.entity.Tasks;
import uk.ac.ed.notify.learn.entity.Users;
import uk.ac.ed.notify.learn.repository.LearnAnnouncementRepository;
import uk.ac.ed.notify.learn.repository.LearnAssessmentRepository;
import uk.ac.ed.notify.learn.repository.LearnCourseUserRepository;
import uk.ac.ed.notify.learn.repository.LearnGradebookGradeRepository;
import uk.ac.ed.notify.learn.repository.LearnTaskRepository;
import uk.ac.ed.notify.learn.repository.LearnUserRepository;
import uk.ac.ed.notify.repository.NotificationRepository;

/**
 *
 * @author hsun1
 */
@Service
public class LearnService {
    private static final Logger logger = LoggerFactory.getLogger(LearnService.class);
    
    @Autowired
    LearnAnnouncementRepository learnAnnouncementRepository;
    
    @Autowired
    LearnTaskRepository learnTaskRepository;
    
    @Autowired
    LearnAssessmentRepository learnAssessmentRepository;
    
    @Autowired
    NotificationRepository notificationRepository;
    
    @Autowired
    LearnUserRepository learnUserRepository;
    
    @Autowired
    LearnCourseUserRepository learnCourseUserRepository;

    @Autowired
    LearnGradebookGradeRepository learnGradebookGradeRepository;
    
    @Autowired
    private MacService macService;
    private static final String announceForwardUrl = "/webapps/blackboard/execute/announcement?method=search&context=course&course_id=courseidtoreplace&handle=cp_announcements";
    private static final String taskForwardUrl = "/webapps/blackboard/execute/taskView?course_id=courseidtoreplace&task_id=taskidtoreplace";
    private static final String assignForwardUrl = "/webapps/assignment/uploadAssignment?content_id=contentidtoreplace&course_id=courseidtoreplace&assign_group_id=&mode=view";
    
    @Value("${learn.baseurl}")
    private String baseUrl; 
    
    private final String publisherId = "learn";

    private Notification constructLearnNotification(
            String publisherNotificationId,
            String category,
            String title,
            String body,
            String url,
            Date startDate,
            Date endDate,
            String uun) {

        Notification notification = new Notification();
        notification.setTitle(title);
        if (body == null || body.equals("")) {
            body = title;
        }
        notification.setBody(body);
        notification.setCategory(category);
        notification.setPublisherId(publisherId);
        notification.setPublisherNotificationId(publisherNotificationId);
        notification.setStartDate(startDate);
        notification.setEndDate(endDate);
        notification.setUrl(url);
        notification.setUun(uun);
        notification.setLastUpdated(new Date());

        return notification;
    }

    private boolean ifInsertLearnNotification(String publisherId, String publisherNotificationId, String uun, Notification notification) {
        List<Notification> existingNotifications = notificationRepository.findByPublisherIdAndPublisherNotificationIdAndUun(publisherId, publisherNotificationId, uun);
        if (existingNotifications.size() == 0) {
            return true;
        } else {
            Notification existingNotification = existingNotifications.get(0);
            if (!notification.equals(existingNotification)) {
                return true;
            }
        }

        return false;
    }
    
    public boolean ifInsertLearnNotification(String publisherId, String publisherNotificationId, Notification notification) {
        List<Notification> existingNotifications = notificationRepository.findByPublisherIdAndPublisherNotificationId(publisherId, publisherNotificationId);
        if (existingNotifications.size() == 0) {
            return true;
        } else {
            Notification existingNotification = existingNotifications.get(0);
            if (!notification.equals(existingNotification)) {
                return true;
            }
        }

        return false;
    }
    

    private String getAnnouncementForwardUrl(String uun, String courseId) {
        try {
            String forwardUrl = URLEncoder.encode(announceForwardUrl.replace("courseidtoreplace", courseId), "UTF-8");
            String timestamp = macService.getTimestamp();
            String mac = null;

            String url = "forward=" + forwardUrl + "&timestamp=" + timestamp + "&userId=" + uun;

            String[] forwardUrlValues = {timestamp, uun};
            try {
                mac = macService.getSecureMAC(forwardUrlValues);
            } catch (NoSuchAlgorithmException e) {
            }

            if (mac != null) {
                return (baseUrl + url + "&auth=" + mac);
            } else {
                return "error_in_encoding_announcement_url";
            }


        } catch (UnsupportedEncodingException e) {
            return "error_in_encoding_announcement_url";
        }
    }

    private String getTaskForwardUrl(String uun, String courseId, String taskId) {
        try {
            String forwardUrl = URLEncoder.encode(taskForwardUrl.replace("courseidtoreplace", courseId).replace("taskidtoreplace", taskId), "UTF-8");

            String timestamp = macService.getTimestamp();
            String mac = null;

            String url = "forward=" + forwardUrl + "&timestamp=" + timestamp + "&userId=" + uun;

            String[] forwardUrlValues = {timestamp, uun};
            try {
                mac = macService.getSecureMAC(forwardUrlValues);
            } catch (NoSuchAlgorithmException e) {
            }

            if (mac != null) {
                return baseUrl + url + "&auth=" + mac;
            } else {
                return "error_in_encoding_task_url";
            }


        } catch (UnsupportedEncodingException e) {
            return "error_in_encoding_task_url";
        }
    }

    private String getAssignForwardUrl(String uun, String contentId, String courseId) {
        try {
            String forwardUrl = URLEncoder.encode(assignForwardUrl.replace("courseidtoreplace", courseId).replace("contentidtoreplace", contentId), "UTF-8");

            String timestamp = macService.getTimestamp();
            String mac = null;

            String url = "forward=" + forwardUrl + "&timestamp=" + timestamp + "&userId=" + uun;

            String[] forwardUrlValues = {timestamp, uun};
            try {
                mac = macService.getSecureMAC(forwardUrlValues);
            } catch (NoSuchAlgorithmException e) {
            }

            if (mac != null) {
                return (baseUrl + url + "&auth=" + mac);
            } else {
                return "error_in_assign_url_encoding";
            }

        } catch (UnsupportedEncodingException e) {
            return "error_in_assign_url_encoding";
        }
    }

    
    public void pullLearnNotifications() {
        logger.debug("pullLearnNotifications");
       
        logger.debug("1.----------task----------");
        List<Tasks> listOfTasks = learnTaskRepository.findTasks();
        for (int i = 0; i < listOfTasks.size(); i++) {
            Tasks task = listOfTasks.get(i);            
            String publisherNotificationId = task.getPk1() + "";
            String category = "Learn Task";
            String title = task.getSubject() + "";
            String body = task.getSubject() + "";
            Date startDate = null;
            Date endDate = task.getDueDate();
            String uun = learnUserRepository.findByPk1(task.getUsersPk1()).get(0).getUserId();
            String url = getTaskForwardUrl(uun, "_" + task.getCrsmainPk1() + "_1", "_" + task.getPk1() + "_1");
            Notification notification = constructLearnNotification(publisherNotificationId, category, title, body, url, startDate, endDate, uun);
            
            logger.debug(category + " - " + i + " " + uun + " " + task.getSubject());

            if (ifInsertLearnNotification(publisherId, publisherNotificationId, uun, notification)) {
                notificationRepository.save(notification);
            }
        }



        logger.debug("2.----------system announcements----------");
        List<Announcements> listOfSystemAnnouncements = learnAnnouncementRepository.findSystemAnnouncements();
        for (int i = 0; i < listOfSystemAnnouncements.size(); i++) {
            Announcements announcement = (Announcements) listOfSystemAnnouncements.get(i);            
            String publisherNotificationId = announcement.getPk1() + "";
            String category = "Learn System Announcement";
            String title = announcement.getSubject() + "";
            String body = announcement.getAnnouncement() + "";
            String url = "";
            Date startDate = announcement.getStartDate();
            Date endDate = announcement.getEndDate();
            Notification notification = constructLearnNotification(publisherNotificationId, category, title, body, "ignore", startDate, endDate, "ignore");

            logger.debug(category + " - " + i + " " +  announcement.getSubject());

            if (ifInsertLearnNotification(publisherId, publisherNotificationId, notification)) {
                List<Users> allUsers = learnUserRepository.findAll();
                for (int r = 0; r < allUsers.size(); r++) {
                    String uun = allUsers.get(r).getUserId();
                    notificationRepository.save(constructLearnNotification(publisherNotificationId, category, title, body, url, startDate, endDate, uun));
                }
            }
        }





        logger.debug("3.----------course announcements----------");
        List<Announcements> listOfCourseAnnouncements = learnAnnouncementRepository.findCourseAnnouncements();
        for (int i = 0; i < listOfCourseAnnouncements.size(); i++) {
            Announcements announcement = (Announcements) listOfCourseAnnouncements.get(i);

            String publisherNotificationId = announcement.getPk1() + "";
            String category = "Learn Course Announcement";
            String title = announcement.getSubject() + "";
            String body = announcement.getAnnouncement() + "";
            Date startDate = announcement.getStartDate();
            Date endDate = announcement.getEndDate();
            Notification notification = constructLearnNotification(publisherNotificationId, category, title, body, "ignore", startDate, endDate, "ignore");

            logger.debug(category + " - " + i + " " +  announcement.getAnnouncement());
            
            if (ifInsertLearnNotification(publisherId, publisherNotificationId, notification)) {
                List<CourseUsers> courseUsers = learnCourseUserRepository.findByCrsmainPk1(announcement.getCrsmainPk1());
                logger.debug("insert for all these users --- " + announcement.getCrsmainPk1() + " " + courseUsers.size());

                for (int r = 0; r < courseUsers.size(); r++) {
                    String uun = learnUserRepository.findByPk1(courseUsers.get(r).getUsersPk1()).get(0).getUserId();
                    String url = getAnnouncementForwardUrl(uun, announcement.getCrsmainPk1() + "");
                    notificationRepository.save(constructLearnNotification(publisherNotificationId, category, title, body, url, startDate, endDate, uun));
                }
            }


        }




        logger.debug("4.----------assessment----------");
        List<GradebookMain> listOfAssessment = learnAssessmentRepository.findGradebookMain();
        logger.debug("listOfAssessment - " + listOfAssessment.size());
        

        for (int i = 0; i < listOfAssessment.size(); i++) {
            GradebookMain assessment = listOfAssessment.get(i);
            
            List<CourseUsers> courseUsers = learnCourseUserRepository.findByCrsmainPk1(assessment.getCrsmainPk1());
            logger.debug("insert for all these users ---" + assessment.getCrsmainPk1() + " " + courseUsers.size());

            for (int r = 0; r < courseUsers.size(); r++) {
            
                //CASE WHEN ( NOT gg.MANUAL_SCORE IS NULL) AND ( NOT gg.MANUAL_GRADE  IS NULL) THEN gg.MANUAL_SCORE ELSE NVL(gg.AVERAGE_SCORE,0)
                List<GradebookGrade> grades = learnGradebookGradeRepository.findByGradebookMainPk1(assessment.getPk1());
                String grade = "";
                if(grades.size() == 0){
                    grade = "";
                }else{
                    GradebookGrade gg = grades.get(0);
                    if( gg.getManualScore() != null && gg.getManualGrade() != null){
                        grade = gg.getManualScore() + "";
                    }else{
                        if(gg.getAverageScore() != null){
                            grade = gg.getAverageScore() + "";
                        }
                    }
                }                           
                String publisherNotificationId = assessment.getPk1() + "";
                String category = "Learn Assessment";
                String title = "Assessment Notification: " + assessment.getTitle() + "";
                String body = "Assessment: " + assessment.getTitle() + " Possible Score:(" + assessment.getPossible() + ")";
                if(grade.equals("")){
                    body =  body + " Your assessment hasn't been marked yet";
                }else{
                    body =  body + " Your score is (" + grade + ")";
                }          
                Date startDate = null;
                Date endDate = assessment.getDueDate();
                String uun = learnUserRepository.findByPk1(courseUsers.get(r).getUsersPk1()).get(0).getUserId();
                String url = getAssignForwardUrl(uun, "_" + assessment.getCourseContentsPk1() + "_1", "_" + assessment.getCrsmainPk1() + "_1");                                   
                
                Notification notification = constructLearnNotification(publisherNotificationId, category, title, body, url, startDate, endDate, uun);
                
                logger.debug(i + " " + category + " - " + assessment.getCrsmainPk1() + " " + assessment.getTitle() + " " + assessment.getPk1() + " " + assessment.getPossible() + " " + grade);

                if (ifInsertLearnNotification(publisherId, publisherNotificationId, uun, notification)) {                
                    logger.debug("insert");
                    notificationRepository.save(notification);
                }
            }


        }

    }
}
