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
import uk.ac.ed.notify.entity.NotificationUser;
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
    //TODO add batch save capability
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
                /*
                 * notification can be identified by a unique combination of publisherId and publisherNotificationId 
                 * there will be only one such combination
                 */
                Notification existingNotification = notificationRepository.findByPublisherIdAndPublisherNotificationId(notification.getPublisherId(), notification.getPublisherNotificationId());
                if (existingNotification == null) {
                    logger.info("notification does not exist in db, insert instead");
                    notification.setNotificationId(null);
                    handleNotification(AuditActions.CREATE_NOTIFICATION, notification);
                } else {         
                        notification.setNotificationId(existingNotification.getNotificationId());
                        /*
                         * where notification exists in database and update is to be done
                         * set NotificationUser id.notificationId with notification's ID
                         */
                        List<NotificationUser> users = notification.getNotificationUsers();
                        for(int i = 0; i < users.size(); i++){
                        	users.get(i).getId().setNotificationId(existingNotification.getNotificationId());
                        }
                        notification.setNotificationUsers(users);
                        logger.info("existing notification found, update");
                        handleNotification(AuditActions.UPDATE_NOTIFICATION, notification);                                       
                }
            } else if (action.equalsIgnoreCase("delete")) {
                logger.info("action: delete");
                Notification existingNotification = notificationRepository.findByPublisherIdAndPublisherNotificationId(notification.getPublisherId(), notification.getPublisherNotificationId());
                if (existingNotification == null) {
                    logger.info("notification not exist in db, ignore");
                    notification.setNotificationId(null);
                    handleNotification(AuditActions.CREATE_NOTIFICATION, notification);
                } else {
                        notification.setNotificationId(existingNotification.getNotificationId());
                        logger.info("existing notification found, delete");
                        handleNotification(AuditActions.DELETE_NOTIFICATION, notification);
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
           // userNotificationAudit.setUun(notification.getUun());
           // userNotificationAuditRepository.save(userNotificationAudit);                 
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
