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
import uk.ac.ed.notify.learn.entity.GradebookMain;
import uk.ac.ed.notify.learn.entity.Tasks;
import uk.ac.ed.notify.learn.entity.Users;
import uk.ac.ed.notify.learn.repository.LearnAnnouncementRepository;
import uk.ac.ed.notify.learn.repository.LearnAssessmentRepository;
import uk.ac.ed.notify.learn.repository.LearnCourseUserRepository;
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

    private boolean ifInsertLearnNotification(String publisherId, String publisherNotificationId, Notification notification) {
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

        System.out.println("1.----------task----------");
        List<Tasks> listOfTasks = learnTaskRepository.findTasks();
        for (int i = 0; i < listOfTasks.size(); i++) {
            Tasks task = listOfTasks.get(i);            
            String publisherNotificationId = task.getPk1() + "";
            String category = "learn_task";
            String title = task.getSubject() + "";
            String body = task.getSubject() + "";
            Date startDate = null;
            Date endDate = task.getDueDate();
            String uun = learnUserRepository.findByPk1(task.getUsersPk1()).get(0).getUserId();
            String url = getTaskForwardUrl(uun, "_" + task.getCrsmainPk1() + "_1", "_" + task.getPk1() + "_1");
            Notification notification = constructLearnNotification(publisherNotificationId, category, title, body, url, startDate, endDate, uun);
            
            System.out.println(category + " - " + i + " " + uun + " " + task.getSubject());

            if (ifInsertLearnNotification(publisherId, publisherNotificationId, notification)) {
                notificationRepository.save(notification);
            }
        }



        System.out.println("2.----------system announcements----------");
        List<Announcements> listOfSystemAnnouncements = learnAnnouncementRepository.findSystemAnnouncements();
        for (int i = 0; i < listOfSystemAnnouncements.size(); i++) {
            Announcements announcement = (Announcements) listOfSystemAnnouncements.get(i);            
            String publisherNotificationId = announcement.getPk1() + "";
            String category = "learn_system_annoucement";
            String title = announcement.getSubject() + "";
            String body = announcement.getAnnouncement() + "";
            String url = "url";
            Date startDate = announcement.getStartDate();
            Date endDate = announcement.getEndDate();
            Notification notification = constructLearnNotification(publisherNotificationId, category, title, body, url, startDate, endDate, null);

            System.out.println(category + " - " + i + " " +  announcement.getSubject());

            if (ifInsertLearnNotification(publisherId, publisherNotificationId, notification)) {
                List<Users> allUsers = learnUserRepository.findAll();
                for (int r = 0; r < allUsers.size(); r++) {
                    String uun = allUsers.get(r).getUserId();
                    notificationRepository.save(constructLearnNotification(publisherNotificationId, category, title, body, url, startDate, endDate, uun));
                }
            }
        }





        System.out.println("3.----------course announcements----------");
        List<Announcements> listOfCourseAnnouncements = learnAnnouncementRepository.findCourseAnnouncements();
        for (int i = 0; i < listOfCourseAnnouncements.size(); i++) {
            Announcements announcement = (Announcements) listOfCourseAnnouncements.get(i);

            String publisherNotificationId = announcement.getPk1() + "";
            String category = "learn_course_annoucement";
            String title = announcement.getSubject() + "";
            String body = announcement.getAnnouncement() + "";
            Date startDate = announcement.getStartDate();
            Date endDate = announcement.getEndDate();
            Notification notification = constructLearnNotification(publisherNotificationId, category, title, body, "ignore", startDate, endDate, "ignore");

            System.out.println(category + " - " + i + " " +  announcement.getAnnouncement());
            
            if (ifInsertLearnNotification(publisherId, publisherNotificationId, notification)) {
                List<CourseUsers> courseUsers = learnCourseUserRepository.findByCrsmainPk1(announcement.getCrsmainPk1());
                System.out.println("insert for all these users --- " + announcement.getCrsmainPk1() + " " + courseUsers.size());

                for (int r = 0; r < courseUsers.size(); r++) {
                    String uun = learnUserRepository.findByPk1(courseUsers.get(r).getUsersPk1()).get(0).getUserId();
                    String url = getAnnouncementForwardUrl(uun, announcement.getCrsmainPk1() + "");
                    notificationRepository.save(constructLearnNotification(publisherNotificationId, category, title, body, url, startDate, endDate, uun));
                }
            }


        }




        System.out.println("4.----------assessment----------");
        List<GradebookMain> listOfAssessment = learnAssessmentRepository.findGradebookMain();
        for (int i = 0; i < listOfAssessment.size(); i++) {
            GradebookMain assessment = listOfAssessment.get(i);
            String publisherNotificationId = assessment.getPk1() + "";
            String category = "learn_assessment";
            String title = assessment.getTitle() + "";
            String body = assessment.getTitle() + "";
            Date startDate = null;
            Date endDate = assessment.getDueDate();

            Notification notification = constructLearnNotification(publisherNotificationId, category, title, body, "ignore", startDate, endDate, "ignore");

            System.out.println(category + " - " + i + " " +  assessment.getTitle());

            if (ifInsertLearnNotification(publisherId, publisherNotificationId, notification)) {
                List<CourseUsers> courseUsers = learnCourseUserRepository.findByCrsmainPk1(assessment.getCrsmainPk1());
                System.out.println("insert for all these users ---" + assessment.getCrsmainPk1() + " " + courseUsers.size());

                for (int r = 0; r < courseUsers.size(); r++) {
                    String uun = learnUserRepository.findByPk1(courseUsers.get(r).getUsersPk1()).get(0).getUserId();
                    String url = getAssignForwardUrl(uun, "_" + assessment.getCourseContentsPk1() + "_1", "_" + assessment.getCrsmainPk1() + "_1");
                    notificationRepository.save(constructLearnNotification(publisherNotificationId, category, title, body, url, startDate, endDate, uun));
                }
            }

        }

    }
}
