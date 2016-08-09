/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import uk.ac.ed.notify.entity.AuditActions;
import uk.ac.ed.notify.entity.ErrorCodes;
import uk.ac.ed.notify.entity.Notification;
import uk.ac.ed.notify.entity.NotificationError;
import uk.ac.ed.notify.entity.NotificationUser;
import uk.ac.ed.notify.entity.NotificationUserPK;
import uk.ac.ed.notify.entity.UserNotificationAudit;
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
import uk.ac.ed.notify.repository.NotificationErrorRepository;
import uk.ac.ed.notify.repository.NotificationRepository;
import uk.ac.ed.notify.repository.UserNotificationAuditRepository;

/**
 *
 * @author hsun1
 */
@Service
public class LearnService {
    private static final Logger logger = LoggerFactory.getLogger(LearnService.class);
    
    @Autowired
    NotificationErrorRepository notificationErrorRepository;    
    
    @Autowired
    UserNotificationAuditRepository userNotificationAuditRepository;
    
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
    
    private static final String announceForwardUrl = "/webapps/blackboard/execute/announcement?method=search&context=course&course_id=courseidtoreplace&handle=cp_announcements";
    private static final String taskForwardUrl = "/webapps/blackboard/execute/taskView?course_id=courseidtoreplace&task_id=taskidtoreplace";
    private static final String assignForwardUrl = "/webapps/assignment/uploadAssignment?content_id=contentidtoreplace&course_id=courseidtoreplace&assign_group_id=&mode=view";
    
    @Value("${learn.baseurl}")
    private String baseUrl; 
    
    @Value("${learn.notificationurl}")
    private String notificationUrl; 
    
    private final String publisherId = "learn";
    private final String taskCategory = "Learn Task";
    private final String sysAnnouncementCategory = "Learn System Announcement";
    private final String courseAnnouncementCategory = "Learn Course Announcement";
    private final String assessmentCategory = "Learn Assessment";
 
    private Notification constructLearnNotification(
            String notificationId,
            String publisherNotificationId,
            String topic,
            String title,
            String body,
            String url,
            Date startDate,
            Date endDate,
            List<String> uuns) {

        Notification notification = new Notification();
        
        if(notificationId != null){
            notification.setNotificationId(notificationId);
        }
        title = (title != null) ? title : "";
        
        notification.setTitle(title);
        if (body == null || body.equals("")) {
            body = title;
        }
        notification.setBody(body);
        notification.setTopic(topic);
        notification.setPublisherId(publisherId);
        notification.setPublisherNotificationId(publisherNotificationId);
        notification.setStartDate(startDate);
        notification.setEndDate(endDate);
        notification.setUrl(url);
        
        List<NotificationUser> notificationUsers = new ArrayList<NotificationUser>();
        NotificationUser user = null;
        
        for (String uun : uuns){
        	user = new NotificationUser();
        	user.setId(new NotificationUserPK(notificationId, uun));
        	user.setNotification(notification);
        	notificationUsers.add(user);
        }
        
        notification.setNotificationUsers(notificationUsers);
        notification.setLastUpdated(new Date());

        return notification;
    }
    
    /*
     * in Learn Service, notification can be uniquely identified by a combination of 
     * publisherId and publisherNotificationId
     * 
     * in Notification Backbone, notification is identified by notificationId
     * 
     * getAction flags notification for either insert or update
     */
    public String getAction(List<Notification> existingTaskNotificationList, Notification notification) {
        
        Notification existingNotification = null;
        Notification tempNotification = null;
        
        for(int i = 0; i < existingTaskNotificationList.size(); i++){
        	tempNotification = existingTaskNotificationList.get(i);
            
            if(tempNotification.getPublisherId().equals(notification.getPublisherId()) && tempNotification.getPublisherNotificationId().equals(notification.getPublisherNotificationId())){
                existingNotification = tempNotification;
                break;
            }            
        }
        
        if (existingNotification == null) {
            logger.debug(notification.getTitle() + " insert");
            return AuditActions.CREATE_NOTIFICATION;
            
        } else {  
        	notification.setNotificationId(existingNotification.getNotificationId());
            
        	logger.debug(notification.getTitle() + " update");
            return AuditActions.UPDATE_NOTIFICATION;
        }
    }
    
    public String getAction(Notification existingNotification, Notification notification) {
    	
    	if (existingNotification == null) {
            logger.debug(notification.getTitle() + " insert");
            return AuditActions.CREATE_NOTIFICATION;
        }
    	else {
    		notification.setNotificationId(existingNotification.getNotificationId());
            
        	logger.debug(notification.getTitle() + " update");
            return AuditActions.UPDATE_NOTIFICATION;
    	}
    }
    

    private Notification getNotificationByPublisherIdAndPublisherNotificationId(List<Notification> listOfLearnNotificationsInNB, String publisherId, String publisherNotificationId){
    	    Notification systemNotification = null;
    	    
            for(int m = 0; m < listOfLearnNotificationsInNB.size(); m++){
            	systemNotification = listOfLearnNotificationsInNB.get(m);
                if(systemNotification.getPublisherId().equals(publisherId) && systemNotification.getPublisherNotificationId().equals(publisherNotificationId)){
                    return systemNotification;
                }
            }
            return null;
    }
    
    
    private List<Notification> getListOfNotificationInNBByPublisherIdAndTopic(List<Notification> listOfLearnNotificationsInNB, String publisherId, String topic){
        List<Notification> listOfLearnSysAnnouncementInNB = new ArrayList<>();
        
        Notification n = null;
        for(int m = 0; m < listOfLearnNotificationsInNB.size(); m++){
        	n = listOfLearnNotificationsInNB.get(m);
        	if(n.getPublisherId().equals(publisherId) && n.getTopic().equals(topic)){
        		listOfLearnSysAnnouncementInNB.add(n);	
        	}
        }
        return listOfLearnSysAnnouncementInNB;
    }    
    
    public void pullLearnNotifications() {
        logger.info("pullLearnNotifications job started");
       
        List<Users> activeLearnUsers = learnUserRepository.findAllActiveUsers();
        logger.info("Total number of active users in learn - " + activeLearnUsers.size());
        
        Map<Integer, String> userIdNamePair = new HashMap<Integer, String>();
        for(int i = 0; i < activeLearnUsers.size(); i++){
            userIdNamePair.put(activeLearnUsers.get(i).getPk1(), activeLearnUsers.get(i).getUserId());
        }
        
        List<Notification> existingLearnNotificationsList = notificationRepository.findByPublisherId(publisherId);
        logger.info("Total number of Learn notifications in Notification Backbone - " + existingLearnNotificationsList.size());
               
        List<String> processedLearnNotificationsList = new ArrayList<String>();
        
        // cache for bulk save
        Map<String,List<Notification>> actionsCache = new HashMap<String, List<Notification>>();
        actionsCache.put(AuditActions.CREATE_NOTIFICATION, new ArrayList<Notification>());
        actionsCache.put(AuditActions.UPDATE_NOTIFICATION, new ArrayList<Notification>());
        
        /*
         * Learn tasks
         */
        processLearnTasks(existingLearnNotificationsList, userIdNamePair, actionsCache, processedLearnNotificationsList);

        /*
         * System Announcements
         */
        processSystemAnnouncements(existingLearnNotificationsList, userIdNamePair, actionsCache, processedLearnNotificationsList);
        
        /*
         * Course announcements
         */
        processCourseAnnouncements(existingLearnNotificationsList, userIdNamePair, actionsCache, processedLearnNotificationsList);

        /*
         * Learn assignments ???
         */
        
        logger.info("Number of notifications to save " + actionsCache.get(AuditActions.CREATE_NOTIFICATION).size());
        logger.info("Number of notifications to update " +  actionsCache.get(AuditActions.UPDATE_NOTIFICATION).size());
        
        handleNotificationByBatch(AuditActions.CREATE_NOTIFICATION, actionsCache.get(AuditActions.CREATE_NOTIFICATION));
        handleNotificationByBatch(AuditActions.UPDATE_NOTIFICATION, actionsCache.get(AuditActions.UPDATE_NOTIFICATION));

        logger.info("5.----------delete notification----------");                 
        logger.info("before delete, total number of current notifications in NB - " + existingLearnNotificationsList.size());
        logger.info("before delete, total number of source items(sys, course announce, task, assessment) in Learn - " + processedLearnNotificationsList.size());
        
        ArrayList<Notification> allCurrentNotificationsToBeDeleted = new ArrayList<Notification>();
        
        for(int i = 0; i < existingLearnNotificationsList.size(); i++){   
        	
            Notification notificationNB = existingLearnNotificationsList.get(i);
            if(!processedLearnNotificationsList.contains(notificationNB.getPublisherNotificationId())){
                allCurrentNotificationsToBeDeleted.add(notificationNB);
            }
        }
         
        logger.info("allCurrentNotificationsToBeDeleted - " + allCurrentNotificationsToBeDeleted.size());        

        handleNotificationByBatch(AuditActions.DELETE_NOTIFICATION, allCurrentNotificationsToBeDeleted);

        logger.info("pullLearnNotifications completed");
    }
    
    /**
     * @params existingLearnNotificationsList contains all exisitng notifications in Notification Backbone
     *         userIdNamePair is a mapping of user primary key in Learn to uun
     *         actionsCache stores cached notifications for bulk insert and update
     *         pulledLearnNotificationsList keeps a record of all notifications processed in Learn pull job
     */
    public void processLearnTasks(List<Notification> existingLearnNotificationsList, Map<Integer, String> userIdNamePair, Map<String,List<Notification>> actionsCache, List<String> processedLearnNotificationsList) {
    	
    	logger.info("STEP.1 Processing Learn tasks");
    	
    	List<CourseUsers> courseUsersList = null;

        List<Tasks> learnTaskList = learnTaskRepository.findTasks();
        logger.info("Total number of tasks in Learn - " + learnTaskList.size());

        List<Notification> notificationTaskList = getListOfNotificationInNBByPublisherIdAndTopic(existingLearnNotificationsList, publisherId, taskCategory);
        logger.info("Total number of tasks in Notification Backbone - " + notificationTaskList.size());

        List<Integer> listOfTaskPks = new ArrayList<Integer>(); 
        
        Tasks task = null;
        Notification taskNotification = null;
        
        for (int i = 0; i < learnTaskList.size(); i++) {
            task = learnTaskList.get(i);          
            
            if(listOfTaskPks.contains(task.getPk1())){
                continue;
            }
            listOfTaskPks.add(task.getPk1());
                  
            String topic = taskCategory;
            /*
             * unique identifier created by combining learn topic with learn task primary key
             */
            String publisherNotificationId = topic + Integer.toString(task.getPk1());
            String title = task.getSubject();
            String body = task.getDescription();
            Date startDate = null;
            Date endDate = task.getDueDate();
            
            courseUsersList = learnCourseUserRepository.findByCrsmainPk1(task.getCrsmainPk1());
            logger.info("Task identified by " + publisherNotificationId + " for course id - " + task.getCrsmainPk1() + " has " + courseUsersList.size() + " users.");
            
            List<String> uunList = new ArrayList<String>();
            String uun = null;
            
            for(int r = 0; r < courseUsersList.size(); r++){
                uun = userIdNamePair.get(courseUsersList.get(r).getUsersPk1());
  
                if(uun == null) continue;
                
                uunList.add(uun);
            }   
            
            taskNotification = constructLearnNotification(null, publisherNotificationId, topic, title, body, notificationUrl, startDate, endDate, uunList);
            
            String action = getAction(notificationTaskList, taskNotification);
            logger.info("Task notification identified by " + publisherNotificationId + " to be handled as " + action);
            actionsCache.get(action).add(taskNotification);
            
            processedLearnNotificationsList.add(publisherNotificationId);
        }  
    }
    
    public void processSystemAnnouncements(List<Notification> existingLearnNotificationsList, Map<Integer, String> userIdNamePair, Map<String,List<Notification>> actionsCache, List<String> processedLearnNotificationsList) {
    	
    	logger.info("STEP 2. Processing system announcements");
    	
        List<Announcements> listOfSystemAnnouncements = learnAnnouncementRepository.findSystemAnnouncements();
        int systemAnnouncementsCount = listOfSystemAnnouncements.size();
        
        logger.info("Total number of system announcements in Learn - " + systemAnnouncementsCount);
        
        if (systemAnnouncementsCount > 0) {
            /*
             * System announcements are sent to all active learn users
             */
        	List<String> uunList = new ArrayList<String>(userIdNamePair.values());
        	
        	Notification systemNotification = null;
        	Announcements systemAnnouncement = null;
        	Notification existingNotification = null;
        	
        	for (int i = 0; i < systemAnnouncementsCount; i++) {
                systemAnnouncement = listOfSystemAnnouncements.get(i);  
     
                String category = sysAnnouncementCategory;
                String publisherNotificationId = category + Integer.toString(systemAnnouncement.getPk1());            
                String title = systemAnnouncement.getSubject();
                String body = systemAnnouncement.getAnnouncement();
                Date startDate = systemAnnouncement.getStartDate();
                Date endDate = systemAnnouncement.getEndDate();
                
                systemNotification = constructLearnNotification(null, publisherNotificationId, category, title, body, notificationUrl, startDate, endDate, uunList);
     
                logger.info("Constructed system announcement identified by " + publisherNotificationId + " with subject " + systemAnnouncement.getSubject());
     
                existingNotification = getNotificationByPublisherIdAndPublisherNotificationId(existingLearnNotificationsList, publisherId, publisherNotificationId);
            
                String action = getAction(existingNotification, systemNotification);
                
                actionsCache.get(action).add(systemNotification);
                logger.info("System notification identified by " + publisherNotificationId + " to be handled as " + action);
                
                processedLearnNotificationsList.add(publisherNotificationId);
            }
        }
    }
    
    public void processCourseAnnouncements(List<Notification> existingLearnNotificationsList, Map<Integer, String> userIdNamePair, Map<String,List<Notification>> actionsCache, List<String> processedLearnNotificationsList) {
    	
    	   logger.info("STEP 3. Processing course announcements"); 
    	   
           List<Announcements> listOfCourseAnnouncements = learnAnnouncementRepository.findCourseAnnouncements();
           logger.info("Total number of course announcements in Learn - " + listOfCourseAnnouncements.size());
    
           ArrayList<Integer> listOfCourseAnnouncementPks = new ArrayList<Integer>(); 
           Announcements courseAnnouncement = null;
           Notification courseNotification = null;
           Notification existingNotification = null;
           List<CourseUsers> courseUsersList = null;
           
           for (int i = 0; i < listOfCourseAnnouncements.size(); i++) {
               courseAnnouncement = listOfCourseAnnouncements.get(i);

               if(listOfCourseAnnouncementPks.contains(courseAnnouncement.getPk1())){
                   continue;
               }
               listOfCourseAnnouncementPks.add(courseAnnouncement.getPk1());

               String category = courseAnnouncementCategory;
               String publisherNotificationId = category + Integer.toString(courseAnnouncement.getPk1());            
               String title = courseAnnouncement.getSubject();
               String body = courseAnnouncement.getAnnouncement();
               Date startDate = courseAnnouncement.getStartDate();
               Date endDate = courseAnnouncement.getEndDate();
               
               courseUsersList = learnCourseUserRepository.findByCrsmainPk1(courseAnnouncement.getCrsmainPk1());
               logger.info("Course announcement identified by " + publisherNotificationId + " has " + courseUsersList.size() + " users.");
               
               List<String> uunList = new ArrayList<String>();
               String uun = null;
               
               for(int r = 0; r < courseUsersList.size(); r++){
                   uun = userIdNamePair.get(courseUsersList.get(r).getUsersPk1());
     
                   if(uun == null) continue;
                   
                   uunList.add(uun);
               }   
               
              courseNotification = constructLearnNotification(null, publisherNotificationId, category, title, body, notificationUrl, startDate, endDate, uunList);
              logger.info("Constructed course announcement identified by " + publisherNotificationId + " with subject " + courseAnnouncement.getSubject());
              
              existingNotification = getNotificationByPublisherIdAndPublisherNotificationId(existingLearnNotificationsList, publisherId, publisherNotificationId);
              
              String action = getAction(existingNotification, courseNotification);
              logger.info("Course notification identified by " + publisherNotificationId + " to be handled as " + action);
              actionsCache.get(action).add(courseNotification);
              
              processedLearnNotificationsList.add(publisherNotificationId);            
           }
    }
    
    public void handleNotificationByBatch(String action, List<Notification> notifications){
            try{
                 if(action.equals(AuditActions.CREATE_NOTIFICATION) || action.equals(AuditActions.UPDATE_NOTIFICATION)){                      
                     notificationRepository.bulkSave(notifications);
                 }else if(action.equals(AuditActions.DELETE_NOTIFICATION)){                      
                     notificationRepository.delete(notifications);
                 } 
                 
            }catch(Exception e){
                 if(action.equals(AuditActions.CREATE_NOTIFICATION)){
                     logErrorNotification(ErrorCodes.SAVE_ERROR ,e); 
                 }else if(action.equals(AuditActions.UPDATE_NOTIFICATION)){
                     logErrorNotification(ErrorCodes.SAVE_ERROR ,e); 
                 }else if(action.equals(AuditActions.DELETE_NOTIFICATION)){
                     logErrorNotification(ErrorCodes.DELETE_ERROR ,e); 
                 }
            }        
    }    
     
    /**
     * 
     * @param action AuditActions
     * @param notification
     * @throws Exception
     */
    private void logNotification(String action, Notification notification) throws Exception {
    	
    	UserNotificationAudit userNotificationAudit = new UserNotificationAudit();
        userNotificationAudit.setAction(action);
        userNotificationAudit.setAuditDate(new Date());
        userNotificationAudit.setPublisherId(notification.getPublisherId());
        userNotificationAudit.setNotificationId(notification.getNotificationId());
        userNotificationAudit.setAuditDescription(new ObjectMapper().writeValueAsString(notification));
        userNotificationAuditRepository.save(userNotificationAudit);               
    }  
    
    /**
     * 
     * @param errorCode ErrorCodes
     * @param e
     */
    private void logErrorNotification(String errorCode, Exception e){
            
            NotificationError notificationError = new NotificationError();
            notificationError.setErrorCode(errorCode);
            notificationError.setErrorDescription(e.getMessage());
            notificationError.setErrorDate(new Date());
            notificationErrorRepository.save(notificationError);        
    }
    
    private String getAnnouncementForwardUrl() {
        return notificationUrl;
    }
    
    private String getAnnouncementForwardUrl(String uun, String courseId) {
        /* WEB007-6, deep linking is not in use
        //getAnnouncementForwardUrl(uun, announcement.getCrsmainPk1() + "");
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
        */
        return notificationUrl;
    }

    private String getTaskForwardUrl(String uun, String courseId, String taskId) {
        /* WEB007-6, deep linking is not in use
        //getTaskForwardUrl(uun, "_" + task.getCrsmainPk1() + "_1", "_" + task.getPk1() + "_1");
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
        */
        
        return notificationUrl;
    }

    private String getAssignForwardUrl(String uun, String contentId, String courseId) {
        /* WEB007-6, deep linking is not in use
        //getAssignForwardUrl(uun, "_" + assessment.getCourseContentsPk1() + "_1", "_" + assessment.getCrsmainPk1() + "_1");
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
        */
        
        return notificationUrl;
    }
    
    
}
