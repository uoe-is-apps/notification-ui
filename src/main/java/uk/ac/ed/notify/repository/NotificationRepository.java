package uk.ac.ed.notify.repository;

import java.util.Date;
import org.springframework.data.repository.CrudRepository;
import uk.ac.ed.notify.entity.Notification;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by rgood on 18/09/2015.
 */

public interface NotificationRepository extends CrudRepository<Notification,String>{

    public List<Notification> findByPublisherIdAndPublisherNotificationId (String publisherId, String publisherNotificationId);
    
    public List<Notification> findByPublisherIdAndPublisherNotificationIdAndUun (String publisherId, String publisherNotificationId, String uun);
    
    public List<Notification> findByPublisherId (String publisherId);

    public List<Notification> findByPublisherIdAndTopic (String publisherId, String topic);
    
    @Transactional
    @Modifying
    @Query("UPDATE Notification a SET a.title=(?1), a.body=(?2), a.startDate=(?3), a.endDate = (?4) "
            + " WHERE a.publisherId = (?5) and a.publisherNotificationId  = (?6)")
    public void updateByPublisherIdAndPublisherNotificationId (String title, String body, Date startDate, Date endDate, String publisherId, String publisherNotificationId);
    
}
