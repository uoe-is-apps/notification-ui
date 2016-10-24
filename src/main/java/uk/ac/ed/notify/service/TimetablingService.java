/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.ac.ed.notify.entity.AuditActions;
import uk.ac.ed.notify.entity.Notification;
import uk.ac.ed.notify.entity.NotificationUser;
import uk.ac.ed.notify.entity.NotificationUserPK;
import uk.ac.ed.notify.repository.NotificationRepository;
import uk.ac.ed.notify.timetabling.entity.Lesson;
import uk.ac.ed.notify.timetabling.entity.Room;
import uk.ac.ed.notify.timetabling.entity.StudentCourseGroup;
import uk.ac.ed.notify.timetabling.repository.LessonRepository;
import uk.ac.ed.notify.timetabling.repository.RoomRepository;
import uk.ac.ed.notify.timetabling.repository.StudentCourseGroupRepository;

@Service
public class TimetablingService {    
    
    private static final Logger logger = LoggerFactory.getLogger(TimetablingService.class);
    
    private final String publisherId = "timetable";
    private final String topic = "timetable";
    
    @Value("${timetabling.numOfDaysAhead}")
    private Integer numOfDaysAhead;
    
    @Autowired
    NotificationRepository notificationRepository;    
    
    @Autowired
    LessonRepository lessonRepository;
    
    @Autowired
    RoomRepository roomRepository;
    
    @Autowired
    StudentCourseGroupRepository studentCourseGroupRepository;
    

    public void pullTimetablingNotifications() {        
            List<Notification> existingTimetableNotificationsList = notificationRepository.findByPublisherId(publisherId);
            logger.info("Total number of Timetable notifications in Notification Backbone - " + existingTimetableNotificationsList.size());               
                        
            List<String> processedTimetableNotificationsList = new ArrayList<String>();

            Map<String,List<Notification>> actionsCache = new HashMap<String, List<Notification>>();
            actionsCache.put(AuditActions.CREATE_NOTIFICATION, new ArrayList<Notification>());
            actionsCache.put(AuditActions.UPDATE_NOTIFICATION, new ArrayList<Notification>());
                    
            List<Room> allRooms = roomRepository.findAllRooms();

            Notification existingNotification = null; 
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, numOfDaysAhead); 

            List<Lesson> listOfLesson = lessonRepository.findAllLessons(cal.getTime());
            
            for(int i = 0; i < listOfLesson.size(); i++){
                 
                 Lesson lesson = listOfLesson.get(i);
                 
                 String publisherNotificationId = topic + lesson.getTimetableId();        

                 String title = lesson.getSubject();
                 String body = lesson.getSubject();
                 String url = "";
                    
                 String acitivityLocationId = lesson.getRoomId();
                 String room = findRoom(allRooms, acitivityLocationId);
                    
                 Date activityStartDate = parseDate(lesson.getStartDate(), lesson.getStartTime());
                 Date activityEndDate = parseDate(lesson.getStartDate(), lesson.getEndTime());   
                 Date startDate = activityStartDate;
                 Date endDate = activityStartDate;                       
                 List<String> uuns = new ArrayList<String>();
                    
                 Notification notification = assembleNotification(                         
                    publisherId,
                    publisherNotificationId,
                    topic,
                    title,
                    body,
                    room,
                    activityStartDate,
                    activityEndDate,
                    url,
                    startDate,
                    endDate,
                    uuns);
                 
                 
                 existingNotification = getNotificationByPublisherIdAndPublisherNotificationId(existingTimetableNotificationsList, publisherId, publisherNotificationId);
                 String action = getAction(existingNotification, notification);
                 
                 logger.info(i + "----------Timetable notification identified by " + publisherNotificationId + " to be handled as " + action);
                 
                 if(action.equals(AuditActions.UPDATE_NOTIFICATION)){
                     //this is update, add users
                     String timetableId = lesson.getTimetableId();
                     List<StudentCourseGroup> students = studentCourseGroupRepository.findStudentsByLessonTimetableId(timetableId);
                     logger.info("Found student - " + students.size());
                     
                     ArrayList<NotificationUser> userList = new ArrayList<NotificationUser>();
                     for(int r = 0; r < students.size(); r++){
                         NotificationUser user = new NotificationUser();
                         
                         NotificationUserPK nupk = new NotificationUserPK();
                         nupk.setNotificationId(existingNotification.getNotificationId());
                         nupk.setUun(students.get(r).getTimetableId());
                         user.setId(nupk);
                         user.setNotification(existingNotification);
                         
                         logger.info("Add student - " + r + " " + nupk.getUun());                         
                         userList.add(user);
                     }
               
                     body = buildNotificationBody(room, activityStartDate, activityEndDate);
                     existingNotification.setBody(body);

                     existingNotification.setNotificationUsers(userList);    
                     
                     actionsCache.get(action).add(existingNotification);
                     processedTimetableNotificationsList.add(publisherNotificationId);    
                 }else if(action.equals(AuditActions.CREATE_NOTIFICATION)){
                     actionsCache.get(action).add(notification);
                     processedTimetableNotificationsList.add(publisherNotificationId);    
                 }                           
             }
            
             logger.info("Number of notifications to create " + actionsCache.get(AuditActions.CREATE_NOTIFICATION).size());
             logger.info("Number of notifications to update " +  actionsCache.get(AuditActions.UPDATE_NOTIFICATION).size());

             handleNotificationByBatch(AuditActions.CREATE_NOTIFICATION, actionsCache.get(AuditActions.CREATE_NOTIFICATION));
             handleNotificationByBatch(AuditActions.UPDATE_NOTIFICATION, actionsCache.get(AuditActions.UPDATE_NOTIFICATION));
   
            //------------------------------------------------------------------
            
            /*
            logger.info("5.----------delete notification----------");                 
            logger.info("before delete, total number of current notifications in NB - " + existingTimetableNotificationsList.size());
            logger.info("before delete, total number of source items(sys, course announce, task, assessment) in Timetable - " + processedTimetableNotificationsList.size());

            ArrayList<Notification> allCurrentNotificationsToBeDeleted = new ArrayList<Notification>();

            for(int i = 0; i < existingTimetableNotificationsList.size(); i++){   

                Notification notificationNB = existingTimetableNotificationsList.get(i);
                if(!processedTimetableNotificationsList.contains(notificationNB.getPublisherNotificationId())){
                    allCurrentNotificationsToBeDeleted.add(notificationNB);
                }
            }

            logger.info("allCurrentNotificationsToBeDeleted - " + allCurrentNotificationsToBeDeleted.size());        

            handleNotificationByBatch(AuditActions.DELETE_NOTIFICATION, allCurrentNotificationsToBeDeleted);

            logger.info("pullTimetableNotifications completed");     
            */
            
    }
    
    private Notification getNotificationByPublisherIdAndPublisherNotificationId(
            List<Notification> listOfTimetableNotificationsInNB, 
            String publisherId, String publisherNotificationId){
        
    	    Notification systemNotification = null;
    	    
            for(int m = 0; m < listOfTimetableNotificationsInNB.size(); m++){
            	systemNotification = listOfTimetableNotificationsInNB.get(m);
                if(systemNotification.getPublisherId().equals(publisherId) && systemNotification.getPublisherNotificationId().equals(publisherNotificationId)){
                    return systemNotification;
                }
            }
            return null;
    }
        
    private String findRoom(List<Room> allRooms, String roomId){
        for(int i = 0; i < allRooms.size(); i++){
            if(allRooms.get(i).getTimetableId().equals(roomId)){
                return allRooms.get(i).getRoomName();
            }
        }
        
        return "room-not-set";
    }
    
    
    private void handleNotificationByBatch(String action, List<Notification> notifications) {        
            for(int i = 0; i < notifications.size(); i++){
                logger.info(i + " -- " + action + " -- " + notifications.get(i).toString());
            }
        
            try{
            	if (notifications != null && !notifications.isEmpty()) {            		
                    if(action.equals(AuditActions.CREATE_NOTIFICATION)){                     
                        notificationRepository.save(notifications);                        
                    }else if(action.equals(AuditActions.UPDATE_NOTIFICATION)){                                             
                        notificationRepository.save(notifications);
                    }else if(action.equals(AuditActions.DELETE_NOTIFICATION)){                      
                        notificationRepository.delete(notifications);
                    } 
            	}
            }catch(Exception e){
                logger.info("handleNotificationByBatch error - " + e.toString());
            }
    }         

    private boolean equalsNotification(Notification thisNotification, Notification otherNotification) {  
        //no need to compare activity title, only activity's location, start time, end time change will trigger a notificaiton
        
        //if (!Objects.equals(thisNotification.getTitle(), otherNotification.getTitle())) {
        //    logger.info("title changed - " + thisNotification.getTitle() + " != " + otherNotification.getTitle());             
        //    return false;
        //}
        
        //comparing notification body, it contains location/start/end time info
        if (!Objects.equals(thisNotification.getBody(), otherNotification.getBody())) {
            logger.info("body changed - " + thisNotification.getBody() + " != " + otherNotification.getBody());              
            return false;
        }
        
        return true;
    }

    private String buildNotificationBody(String acitivityLocation, Date activityStartDate, Date activityEndDate){
        return "This activity has been updated, here is the latest location/start time/end time information. Location: " + acitivityLocation + " Start Date/Time:" + activityStartDate + " End Date/Time:" + activityEndDate;        
    }    
        
    private Notification assembleNotification(
    	    String publisherId,
            String publisherNotificationId,
            String topic,
            String title,
            String body,
            
            String acitivityLocationId,
            Date activityStartDate,
            Date activityEndDate,
            
            String url,
            Date startDate,
            Date endDate,
            List<String> uuns) {

        Notification notification = new Notification();
        
        title = (title != null) ? title : "";
        notification.setTitle(title);
        
        body = buildNotificationBody(acitivityLocationId, activityStartDate, activityEndDate);
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
            user.setId(new NotificationUserPK(null, uun));
            user.setNotification(notification);
            notificationUsers.add(user);
        }        
        notification.setNotificationUsers(notificationUsers);                
        return notification;
    }    

    
    private String getAction(Notification existingNotification, Notification notification) {    	
    	if (existingNotification == null) {
            logger.info(notification.getTitle() + " create");
            return AuditActions.CREATE_NOTIFICATION;
        }else if(!equalsNotification(existingNotification, notification)){
            logger.info(notification.getTitle() + " update");
            return AuditActions.UPDATE_NOTIFICATION;
    	}else{
            logger.info(notification.getTitle() + " unchanged, ignore");
            return "IGNORE_NOTIFICATION";
        }
    }    
    
                
    private Date parseDate(Date date, String time){
        try{
            String d = new SimpleDateFormat("yyyy-MM-dd").format(date) + " " + time + ":00";
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(d);
        }catch(Exception e){
            return null;
        }
    }    
}
