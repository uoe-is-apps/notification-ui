/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ed.notify.entity.AuditActions;
import uk.ac.ed.notify.entity.ErrorCodes;
import uk.ac.ed.notify.entity.Notification;
import uk.ac.ed.notify.entity.NotificationError;
import uk.ac.ed.notify.entity.PublisherDetails;
import uk.ac.ed.notify.entity.UserNotificationAudit;
import uk.ac.ed.notify.repository.NotificationErrorRepository;
import uk.ac.ed.notify.repository.NotificationRepository;
import uk.ac.ed.notify.repository.PublisherDetailsRepository;
import uk.ac.ed.notify.repository.UserNotificationAuditRepository;

/**
 *
 * @author hsun1
 */
@Service
public class EmailNotificationHandlingService {
    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationHandlingService.class);           
    
    @Autowired
    PublisherDetailsRepository publisherDetailsRepository;
                            
    @Autowired
    NotificationRepository notificationRepository;
                
    @Autowired
    NotificationErrorRepository notificationErrorRepository;    
            
    @Autowired
    UserNotificationAuditRepository userNotificationAuditRepository;                
    
    public void processSingleNotification(Notification notification){
        logger.debug("handle notification..." + notification);
                
        String action = notification.getAction();
        PublisherDetails publisher = publisherDetailsRepository.findOne(notification.getPublisherId());
        if (publisher != null 
                && publisher.getKey() != null && notification.getPublisherKey() != null && publisher.getKey().equals(notification.getPublisherKey())
                && action != null && (action.equalsIgnoreCase("insert") || action.equalsIgnoreCase("update") || action.equalsIgnoreCase("delete"))
                ) {
            logger.info("publisher key verified");

            
            if (action.equalsIgnoreCase("insert")) {
                logger.info("action: insert");
                notification.setNotificationId(null);
                handleNotification(AuditActions.CREATE_NOTIFICATION, notification);
            } else if (action.equalsIgnoreCase("update")) {
                logger.info("action: update");
                List<Notification> existingNotifications = notificationRepository.findByPublisherIdAndPublisherNotificationIdAndUun(notification.getPublisherId(), notification.getPublisherNotificationId(), notification.getUun());
                if (existingNotifications.size() == 0) {
                    logger.info("notification not exist in db, insert instead");
                    notification.setNotificationId(null);
                    handleNotification(AuditActions.CREATE_NOTIFICATION, notification);
                } else {         
                    for(int i = 0; i < existingNotifications.size(); i++){
                        notification.setNotificationId(existingNotifications.get(i).getNotificationId());
                        logger.info("index - " + i + " - existing notification found, update");
                        handleNotification(AuditActions.UPDATE_NOTIFICATION, notification);
                    }                                        
                }
            } else if (action.equalsIgnoreCase("delete")) {
                logger.info("action: delete");
                List<Notification> existingNotifications = notificationRepository.findByPublisherIdAndPublisherNotificationIdAndUun(notification.getPublisherId(), notification.getPublisherNotificationId(), notification.getUun());
                if (existingNotifications.size() == 0) {
                    logger.info("notification not exist in db, ignore");
                    notification.setNotificationId(null);
                    handleNotification(AuditActions.CREATE_NOTIFICATION, notification);
                } else {
                    for(int i = 0; i < existingNotifications.size(); i++){
                        notification.setNotificationId(existingNotifications.get(i).getNotificationId());
                        logger.info("index - " + i + " - existing notification found, delete");
                        handleNotification(AuditActions.DELETE_NOTIFICATION, notification);
                    }
                }
            }
        } else {
            logger.info("notification failed to be processed due to failure in processing json packet or invalid publisher key");
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
    
    public void logNotification(String action, Notification notification) {
            //AuditActions.CREATE_NOTIFICATION AuditActions.UPDATE_NOTIFICATION  AuditActions.DELETE_NOTIFICATION
            UserNotificationAudit userNotificationAudit = new UserNotificationAudit();
            userNotificationAudit.setAction(action);
            userNotificationAudit.setAuditDate(new Date());
            userNotificationAudit.setPublisherId(notification.getPublisherId());
            userNotificationAudit.setUun(notification.getUun());
            userNotificationAuditRepository.save(userNotificationAudit);                 
    }  
    
    public void logErrorNotification(String errorCode, Exception e){
            //ErrorCodes.SAVE_ERROR ErrorCodes.DELETE_ERROR
            NotificationError notificationError = new NotificationError();
            notificationError.setErrorCode(errorCode);
            notificationError.setErrorDescription(e.getMessage());
            notificationError.setErrorDate(new Date());
            notificationErrorRepository.save(notificationError);        
    }    
    

}
