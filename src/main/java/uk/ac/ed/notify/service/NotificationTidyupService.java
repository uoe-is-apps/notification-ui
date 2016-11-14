/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.ac.ed.notify.entity.Notification;
import uk.ac.ed.notify.repository.NotificationRepository;

@Service
public class NotificationTidyupService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationTidyupService.class);
         
    @Autowired
    NotificationRepository notificationRepository;    
         
    @Value("${notification.purge}")
    int purge;
       
    public List<Notification> findDeletableNotification(){
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, -1* purge);
        Date earlyDate = cal.getTime();        
        java.sql.Date sqlDate = new java.sql.Date(earlyDate.getTime());    
        
        List<Notification> list = notificationRepository.findDeletableNotification(sqlDate);            
        return list;
    }
    
    public void tidyupNotification(){
        List<Notification> list = findDeletableNotification();   
        logger.info("tidyupNotification, found [" + list.size() + "] notifications which end date is earlier than (sysdate - " + purge + ") date, these will be purged");        
         
        //debug print
        for(int i = 0; i < list.size(); i++){
            logger.info(i + " topic:" + list.get(i).getTopic() + " end date:" + list.get(i).getEndDate() + " title" + list.get(i).getTitle() + " will be deleted");
        }        
        
        notificationRepository.delete(list);
    }
 
}
