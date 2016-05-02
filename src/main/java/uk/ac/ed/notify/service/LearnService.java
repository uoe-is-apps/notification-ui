/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ed.notify.entity.AuditActions;
import uk.ac.ed.notify.entity.ErrorCodes;
import uk.ac.ed.notify.entity.Notification;
import uk.ac.ed.notify.entity.NotificationError;
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
            String category,
            String title,
            String body,
            String url,
            Date startDate,
            Date endDate,
            String uun) {

        Notification notification = new Notification();
        
        if(notificationId != null){
            notification.setNotificationId(notificationId);
        }
        
        notification.setTitle(title + "");
        if (body == null || body.equals("")) {
            body = title + "";
        }
        notification.setBody(body + "");
        notification.setTopic(category);
        notification.setPublisherId(publisherId);
        notification.setPublisherNotificationId(publisherNotificationId);
        notification.setStartDate(startDate);
        notification.setEndDate(endDate);
        notification.setUrl(url);
        notification.setUun(uun);
        notification.setLastUpdated(new Date());

        return notification;
    }

    public String ifSaveLearnNotification(List<Notification> existingNotificationInNB, String publisherId, String publisherNotificationId, String uun, Notification notification) {
        //List<Notification> existingNotifications = notificationRepository.findByPublisherIdAndPublisherNotificationIdAndUun(publisherId, publisherNotificationId, uun);
        
        Notification existingNotification = null;
        for(int i = 0; i < existingNotificationInNB.size(); i++){
            Notification noti = existingNotificationInNB.get(i);
            
//            logger.info("[[[[[[");
//            logger.info(noti.getPublisherId());
//            logger.info(publisherId);
//            logger.info(noti.getPublisherNotificationId());
//            logger.info(publisherNotificationId); 
//            logger.info(noti.getUun());
//            logger.info(uun);
//            logger.info("]]]]]]");
            
            if(noti.getPublisherId().equals(publisherId) && noti.getPublisherNotificationId().equals(publisherNotificationId) && noti.getUun().equals(uun)){
                existingNotification = noti;
                break;
            }            
        }
        
        if (existingNotification == null) {
            notification.setNotificationId(null);
            logger.debug(notification.getTitle() + " insert");
            return "insert";
        } else {
            if (!notification.equals(existingNotification)) {
                notification.setNotificationId(existingNotification.getNotificationId());
                        
                logger.debug(notification.getTitle() + " update");
                return "update";
            }
        }
        
        logger.debug(notification.getTitle() + " ignore");
        return "ignore";
    }
    
    
    
    public String ifSaveLearnNotification(List<Notification> existingNotificationInNB, String publisherId, String publisherNotificationId, Notification notification) {
        //List<Notification> existingNotifications = notificationRepository.findByPublisherIdAndPublisherNotificationId(publisherId, publisherNotificationId);
        
        Notification existingNotification = null;
        for(int i = 0; i < existingNotificationInNB.size(); i++){
            Notification noti = existingNotificationInNB.get(i);
            
            if(noti.getPublisherId().equals(publisherId) && noti.getPublisherNotificationId().equals(publisherNotificationId)){
                existingNotification = noti;
                break;
            }            
        }
        
        if (existingNotification == null) {
            notification.setNotificationId(null);
            logger.debug(notification.getTitle() + " insert");
            return "insert";
        } else {            
            if (!notification.equals(existingNotification)) {
                notification.setNotificationId(existingNotification.getNotificationId());
                
                logger.debug(notification.getTitle() + " update");
                return "update";
            }
        }

        logger.debug(notification.getTitle() + " ignore");
        return "ignore";
    }
    

    private List<Notification> getListOfNotificationInNBByPublisherIdAndPublisherNotificationId(List<Notification> listOfLearnNotificationsInNB, String publisherId, String publisherNotificationId){
        List<Notification> listOfLearnSysAnnouncementInNB = new ArrayList<>();
            for(int m = 0; m < listOfLearnNotificationsInNB.size(); m++){
                Notification n = listOfLearnNotificationsInNB.get(m);
                if(n.getPublisherId().equals(publisherId) && n.getPublisherNotificationId().equals(publisherNotificationId)){
                    listOfLearnSysAnnouncementInNB.add(n);
                }
            }
            return listOfLearnSysAnnouncementInNB;
    }
    
    
    private List<Notification> getListOfNotificationInNBByPublisherIdAndTopic(List<Notification> listOfLearnNotificationsInNB, String publisherId, String topic){
        List<Notification> listOfLearnSysAnnouncementInNB = new ArrayList<>();
            for(int m = 0; m < listOfLearnNotificationsInNB.size(); m++){
                Notification n = listOfLearnNotificationsInNB.get(m);
                if(n.getPublisherId().equals(publisherId) && n.getTopic().equals(topic)){
                    listOfLearnSysAnnouncementInNB.add(n);
                }
            }
            return listOfLearnSysAnnouncementInNB;
    }    
    
    public void pullLearnNotifications() {
        logger.info("pullLearnNotifications started - " + new Date());
       
        
        ArrayList<Users> allUsers = (ArrayList)learnUserRepository.findAllActiveUsers();
        Hashtable<String, String> userIdNamePair = new Hashtable<String, String>();
        for(int i = 0; i < allUsers.size(); i++){
            Users user = allUsers.get(i);
            userIdNamePair.put(user.getPk1() + "", user.getUserId());
        }
        logger.info("total number of users in learn - " + allUsers.size());
        
        List<Notification> listOfLearnNotificationsInNB = notificationRepository.findByPublisherId(publisherId);
        logger.info("total number of Learn notifications in Notification Backbone - " + listOfLearnNotificationsInNB.size());
               
         
        ArrayList<String> allCurrentPublisherNotificationIdInLearn = new ArrayList<String>();
        
               
   

        

        logger.info("1.----------task----------");
        List<Tasks> listOfTasks = learnTaskRepository.findTasks();
        logger.info("total number of tasks in Learn - " + listOfTasks.size());

        List<Notification> listOfLearnTaskInNB = getListOfNotificationInNBByPublisherIdAndTopic(listOfLearnNotificationsInNB, publisherId, taskCategory);
        logger.info("total number of tasks in Notification Backbone - " + listOfLearnTaskInNB.size());

        ArrayList<Integer> listOfTaskPks = new ArrayList<Integer>(); 
        for (int i = 0; i < listOfTasks.size(); i++) {
            Tasks task = listOfTasks.get(i);          
            
            if(listOfTaskPks.contains(task.getPk1())){
                continue;
            }
            listOfTaskPks.add(task.getPk1());
                  
            String category = taskCategory;
            String publisherNotificationId = category + task.getPk1() + "";
            String title = task.getSubject() + "";
            String body = task.getDescription() + "";
            Date startDate = null;
            Date endDate = task.getDueDate();
            
            List<CourseUsers> courseUsers = learnCourseUserRepository.findByCrsmainPk1(task.getCrsmainPk1());
            logger.info("task index [" + i + "] check insert/update for all these users ------------------ course id - " + task.getCrsmainPk1() + " number of users - " + courseUsers.size());
            for(int r = 0; r < courseUsers.size(); r++){
                String uun = userIdNamePair.get(courseUsers.get(r).getUsersPk1() + ""); //learnUserRepository.findByPk1(courseUsers.get(r).getUsersPk1()).get(0).getUserId();
  
                if(uun == null) continue;
                
                Notification notification = constructLearnNotification(null, publisherNotificationId, category, title, body, notificationUrl, startDate, endDate, uun);
                String mode = ifSaveLearnNotification(listOfLearnTaskInNB, publisherId, publisherNotificationId, uun, notification);
//String mode = "ignore";
                
                logger.info("need [" + mode + "]");
                if(mode.equals("insert")) {    
                    logger.debug(r + "----------insert task----------" + uun + " " + notification);
                    handleNotification(AuditActions.CREATE_NOTIFICATION, notification);           
                }else if (mode.equals("update")) {                                   
                    logger.debug(r + "----------update task----------" + uun + " " + notification);
                    handleNotification(AuditActions.UPDATE_NOTIFICATION, notification);
                } 
                allCurrentPublisherNotificationIdInLearn.add(publisherNotificationId);
            }   
            logger.info("task index [" + i + "], complete for " + courseUsers.size() + " users");
        }

        
        
        
        

        
        
        
        
        
        
        
        

        logger.info("2.----------system announcements----------");
        List<Announcements> listOfSystemAnnouncements = learnAnnouncementRepository.findSystemAnnouncements();        
        logger.info("total number of system announcements in Learn - " + listOfSystemAnnouncements.size());
        
        for (int i = 0; i < listOfSystemAnnouncements.size(); i++) {
            Announcements announcement = (Announcements) listOfSystemAnnouncements.get(i);  
 
            String category = sysAnnouncementCategory;
            String publisherNotificationId = category + announcement.getPk1() + "";            
            String title = announcement.getSubject() + "";
            String body = announcement.getAnnouncement() + "";
            Date startDate = announcement.getStartDate();
            Date endDate = announcement.getEndDate();
            Notification notificationCompare = constructLearnNotification(null, publisherNotificationId, category, title, body, notificationUrl, startDate, endDate, "ignore");

            logger.info(category + " - " + i + " " +  announcement.getSubject());
 
            
            List<Notification> listOfLearnSysAnnouncementInNB = getListOfNotificationInNBByPublisherIdAndPublisherNotificationId(listOfLearnNotificationsInNB, publisherId, publisherNotificationId);
        
            String mode = ifSaveLearnNotification(listOfLearnSysAnnouncementInNB, publisherId, publisherNotificationId, notificationCompare);
//String mode = "ignore";            
            logger.info("system announcements index [" + i + "] check insert/update for all users in learn ------------------ need [" + mode + "]");
            
            if (mode.equals("insert")) {  

                List<Notification> notificationsToBeInserted = new ArrayList<Notification>();
                for (int r = 0; r < allUsers.size(); r++) {    
                   Notification notificationInsert = constructLearnNotification(null, publisherNotificationId, category, title, body, notificationUrl, startDate, endDate, "ignore");
                   String uun = allUsers.get(r).getUserId();
                   notificationInsert.setUun(uun);
                   notificationsToBeInserted.add(notificationInsert);
                   //logger.debug(r + "----------insert sys announce----------" + uun + " " + notificationInsert);
                }

                logger.info("start inserting notifications in database - " + new Date() + " [" + listOfLearnSysAnnouncementInNB.size() + "] user size - " + allUsers.size());
                handleNotificationByBatch(AuditActions.CREATE_NOTIFICATION, notificationsToBeInserted);
                logger.info("complete - " + new Date());
                
            }else if (mode.equals("update")) {  

                /*method 1. batch update is still slow...
                logger.info("1. start updating notifications in for loop - " + new Date() + " [" + listOfLearnSysAnnouncementInNB.size() + "] user size - " + allUsers.size());            
                for(int r = 0; r < listOfLearnSysAnnouncementInNB.size(); r++){
                    Notification notificationUpdate = listOfLearnSysAnnouncementInNB.get(r);
                    notificationUpdate.setTitle(title);
                    notificationUpdate.setBody(body);
                    notificationUpdate.setStartDate(startDate);
                    notificationUpdate.setEndDate(endDate);
                }
                logger.info("1. complete - " + new Date());
                logger.info("2. start updating notifications in database - " + new Date());
                handleNotificationByBatch(AuditActions.UPDATE_NOTIFICATION, listOfLearnSysAnnouncementInNB);
                logger.info("2. complete - " + new Date());
                */
                
                logger.info("start updating notifications in database - " + new Date());
                notificationRepository.updateByPublisherIdAndPublisherNotificationId(title, body, startDate, endDate, publisherId, publisherNotificationId);
                logger.info("complete - " + new Date());
            }
            
            logger.info("system announcements index [" + i + "], complete for all users [" + allUsers.size() + "] users");
            
            allCurrentPublisherNotificationIdInLearn.add(publisherNotificationId);
            
        }
   


        
        
        
        
        
        
        
        

        
      
        logger.info("3.----------course announcements----------"); 
        List<Announcements> listOfCourseAnnouncements = learnAnnouncementRepository.findCourseAnnouncements();
        logger.info("total number of course announcements in Learn - " + listOfCourseAnnouncements.size());
               

        List<Notification> notificationsToBeInserted = new ArrayList<Notification>();
        
        ArrayList<Integer> listOfCourseAnnouncementPks = new ArrayList<Integer>(); 
        for (int i = 0; i < listOfCourseAnnouncements.size(); i++) {
            Announcements announcement = (Announcements) listOfCourseAnnouncements.get(i);

            if(listOfCourseAnnouncementPks.contains(announcement.getPk1())){
                continue;
            }
            
            listOfCourseAnnouncementPks.add(announcement.getPk1());

            String category = courseAnnouncementCategory;
            String publisherNotificationId = category + announcement.getPk1() + "";            
            String title = announcement.getSubject() + "";
            String body = announcement.getAnnouncement() + "";
            Date startDate = announcement.getStartDate();
            Date endDate = announcement.getEndDate();

            Notification notificationCompare = constructLearnNotification(null, publisherNotificationId, category, title, body, notificationUrl, startDate, endDate, "ignore");                
            
            
            List<Notification> listOfLearnCourseAnnouncementInNB = getListOfNotificationInNBByPublisherIdAndPublisherNotificationId(listOfLearnNotificationsInNB, publisherId, publisherNotificationId);
        
            String mode = ifSaveLearnNotification(listOfLearnCourseAnnouncementInNB, publisherId, publisherNotificationId, notificationCompare);
            
            logger.info("couse announcements index [" + i + "] check insert/update for course users in learn ------------------ need [" + mode + "]");
            if (mode.equals("insert")) {  
                List<CourseUsers> courseUsers = learnCourseUserRepository.findByCrsmainPk1(announcement.getCrsmainPk1());

                logger.info("1. start inserting notifications in for loop - " + new Date() + " [" + listOfLearnCourseAnnouncementInNB.size() + "] user size - " + courseUsers.size()); 
                for (int r = 0; r < courseUsers.size(); r++) {     
                   Notification notificationInsert = constructLearnNotification(null, publisherNotificationId, category, title, body, notificationUrl, startDate, endDate, "ignore");
                   String uun = userIdNamePair.get(courseUsers.get(r).getUsersPk1() + "");
                   notificationInsert.setUun(uun);
                   
                   if(notificationInsert.getTitle() != null && notificationInsert.getBody() != null && uun != null){
                        notificationsToBeInserted.add(notificationInsert); 
                   }
                   
                }
                logger.info("1. complete - " + new Date());

            }else if (mode.equals("update")) {  
                
                /*method 1. still slow...
                logger.info("1. start updating notifications in for loop - " + new Date() + " [" + listOfLearnCourseAnnouncementInNB.size() + "] ");            
                for(int r = 0; r < listOfLearnCourseAnnouncementInNB.size(); r++){
                    Notification notificationUpdate = listOfLearnCourseAnnouncementInNB.get(r);
                    notificationUpdate.setTitle(title);
                    notificationUpdate.setBody(body);
                    notificationUpdate.setStartDate(startDate);
                    notificationUpdate.setEndDate(endDate);
                    
                }
                logger.info("1. complete - " + new Date());
                logger.info("2. start updating notifications in database - " + new Date());
                handleNotificationByBatch(AuditActions.UPDATE_NOTIFICATION, listOfLearnCourseAnnouncementInNB);
                logger.info("2. complete - " + new Date());
                */
                
                logger.info("start updating course annoucement notifications in database - " + new Date());
                notificationRepository.updateByPublisherIdAndPublisherNotificationId(title, body, startDate, endDate, publisherId, publisherNotificationId);
                logger.info("complete - " + new Date());
            }
            
            allCurrentPublisherNotificationIdInLearn.add(publisherNotificationId);            
        }

        
        logger.info("2. start inserting course annoucement notifications in database - size [" + notificationsToBeInserted.size() + "] " + new Date());
        
        
        int total = notificationsToBeInserted.size();
        int noOfBatch = 10;
        int batchSize = total / 10;
        
        for(int m = 0; m < noOfBatch; m++){
            List<Notification> notificationsToBeInsertedSmallBatch = new ArrayList<>();
            
            int offset = batchSize * m;
            logger.info(m + " from - [" + offset + "] to - [" + (batchSize + offset - 1) + "]" ); 
            for(int i = 0 + offset; i < batchSize + offset; i++){
                Notification n = notificationsToBeInserted.get(i);

                notificationsToBeInsertedSmallBatch.add(n);
            } 
            handleNotificationByBatch(AuditActions.CREATE_NOTIFICATION, notificationsToBeInsertedSmallBatch);
            logger.info(m + " complete - " + new Date());        
        }               
        
        logger.info("2. complete - " + new Date());        



        
        
        
        
        
        
        
        
        

        
/*        
        logger.info("4.----------assessment----------");
        List<GradebookMain> listOfAssessment = learnAssessmentRepository.findGradebookMain();
        logger.info("total number of assessment in Learn - " + listOfAssessment.size());
        
        List<Notification> listOfLearnAssessmentInNB = getListOfNotificationInNBByPublisherIdAndTopic(listOfLearnNotificationsInNB, publisherId, assessmentCategory);
        logger.info("total number of assessment in Notification Backbone - " + listOfLearnAssessmentInNB.size());                
        
        
        for (int i = 0; i < listOfAssessment.size(); i++) {
            GradebookMain assessment = listOfAssessment.get(i);
            
            //if(!assessment.getPk1().equals(new Integer("1245"))){ //for debugging, restrict to a particular assessment for easy viewing
            //    continue;
            //}
            
            List<CourseUsers> courseUsers = learnCourseUserRepository.findByCrsmainPk1(assessment.getCrsmainPk1());
            logger.info("assessment index [" + i + "] check insert/update for all these users ------------------ course id - " + assessment.getCrsmainPk1() + " number of users - " + courseUsers.size());
    
            List<GradebookGrade> grades = learnGradebookGradeRepository.findByGradebookMainPk1(assessment.getPk1());
            
            for (int r = 0; r < courseUsers.size(); r++) {
                Integer userOnThisCoursePk = courseUsers.get(r).getUsersPk1();
            
                String grade = "";
                
                if(grades.size() == 0){
                    grade = "";
                }else{
                    for(int m = 0; m < grades.size(); m++){
                        GradebookGrade gg = grades.get(m);
                        Integer userWithGradePk = gg.getCourseUsersPk1();
                        
                        if(userOnThisCoursePk.equals(userWithGradePk)){//CASE WHEN ( NOT gg.MANUAL_SCORE IS NULL) AND ( NOT gg.MANUAL_GRADE  IS NULL) THEN gg.MANUAL_SCORE ELSE NVL(gg.AVERAGE_SCORE,0)    
                            if( gg.getManualScore() != null && gg.getManualGrade() != null){
                                grade = gg.getManualScore() + "";
                            }else{
                                if(gg.getAverageScore() != null){
                                    grade = gg.getAverageScore() + "";
                                }
                            }
                        }
                        
                    }
                }      

                String category = assessmentCategory;
                String publisherNotificationId = category + assessment.getPk1() + "";               
                String title = assessment.getTitle() + "";
                String body = "Assessment: " + assessment.getTitle() + " Possible Score:(" + assessment.getPossible() + ")";
                if(grade.equals("")){
                    body =  body + " Your assessment hasn't been marked yet";
                }else{
                    body =  body + " Your score is (" + grade + ")";
                }          
                Date startDate = null;
                Date endDate = assessment.getDueDate();                
                String uun = userIdNamePair.get(userOnThisCoursePk + "");//String uun = learnUserRepository.findByPk1(courseUsers.get(r).getUsersPk1()).get(0).getUserId();
            
                Notification notification = constructLearnNotification(null, publisherNotificationId, category, title, body, notificationUrl, startDate, endDate, uun);
                
               
                //String mode = ifSaveLearnNotification(listOfLearnAssessmentInNB, publisherId, publisherNotificationId, uun, notification);
String mode = "ignore";                 
                if(mode.equals("insert")) {                
                    handleNotification(AuditActions.CREATE_NOTIFICATION, notification);  
                    logger.debug(r + "----------insert assessment ----------" + uun + " " + notification);
                }else if (mode.equals("update")) {                                   
                    handleNotification(AuditActions.UPDATE_NOTIFICATION, notification);
                    logger.debug(r + "----------update assessment ----------" + uun + " " + notification);
                }
                allCurrentPublisherNotificationIdInLearn.add(publisherNotificationId);
            }
            logger.info("assessment index [" + i + "], complete for assessment users");
        }        
*/
        
        
        
        
        
        
        
        
       
        logger.info("5.----------delete notification----------");                 
        logger.info("before delete, total number of current notifications in NB - " + listOfLearnNotificationsInNB.size());
        logger.info("before delete, total number of source items(sys, course announce, task, assessment) in Learn - " + allCurrentPublisherNotificationIdInLearn.size());
        
        ArrayList<Notification> allCurrentNotificationsToBeDeleted = new ArrayList<Notification>();
        for(int i = 0; i < listOfLearnNotificationsInNB.size(); i++){            
            Notification existingNotification = listOfLearnNotificationsInNB.get(i);
            if(!allCurrentPublisherNotificationIdInLearn.contains(existingNotification.getPublisherNotificationId())){
                //logger.info("in nb but not in learn, queue for deletion - " + existingNotification);
                allCurrentNotificationsToBeDeleted.add(existingNotification);
            }
        }
         
        logger.info("allCurrentNotificationsToBeDeleted - " + allCurrentNotificationsToBeDeleted.size());        
//        for(int i = 0; i < allCurrentNotificationsToBeDeleted.size(); i++){
//            Notification existingNotification = allCurrentNotificationsToBeDeleted.get(i);
//            logger.info("delete " + i + " " + existingNotification);         
//            handleNotification(AuditActions.DELETE_NOTIFICATION, existingNotification); 
//        }
         handleNotificationByBatch(AuditActions.DELETE_NOTIFICATION, allCurrentNotificationsToBeDeleted);

        logger.info("pullLearnNotifications completed ... " + new Date());
    }
    
    
    
    
    
    @Transactional 
    public void handleNotificationByBatch(String action, List<Notification> notifications){
            try{
                 if(action.equals(AuditActions.CREATE_NOTIFICATION) || action.equals(AuditActions.UPDATE_NOTIFICATION)){                      
                     notificationRepository.save(notifications);
                 }else if(action.equals(AuditActions.DELETE_NOTIFICATION)){                      
                     notificationRepository.delete(notifications);
                 } 
                 //to speed up, disable this logNotification function
                 //logNotification(action, notification);
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
    
    
    
    @Transactional 
    public void handleNotification(String action, Notification notification){
            try{
                 if(action.equals(AuditActions.CREATE_NOTIFICATION) || action.equals(AuditActions.UPDATE_NOTIFICATION)){                      
                      notificationRepository.save(notification);
                 }else if(action.equals(AuditActions.DELETE_NOTIFICATION)){                      
                      notificationRepository.delete(notification.getNotificationId());
                 } 
                 logNotification(action, notification);
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
    
    private void logNotification(String action, Notification notification) {
            //AuditActions.CREATE_NOTIFICATION AuditActions.UPDATE_NOTIFICATION  AuditActions.DELETE_NOTIFICATION
            UserNotificationAudit userNotificationAudit = new UserNotificationAudit();
            userNotificationAudit.setAction(action);
            userNotificationAudit.setAuditDate(new Date());
            userNotificationAudit.setPublisherId(notification.getPublisherId());
            userNotificationAudit.setUun(notification.getUun());
            userNotificationAuditRepository.save(userNotificationAudit);                 
    }  
    
    private void logErrorNotification(String errorCode, Exception e){
            //ErrorCodes.SAVE_ERROR ErrorCodes.DELETE_ERROR
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
