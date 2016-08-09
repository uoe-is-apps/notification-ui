package uk.ac.ed.notify.repository;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ed.notify.entity.Notification;

import java.util.List;


/**
 * Created by rgood on 18/09/2015.
 */
public interface NotificationRepository extends CrudRepository<Notification,String>, NotificationRepositoryAddon {

    Notification findByPublisherIdAndPublisherNotificationId (String publisherId, String publisherNotificationId);
    
    List<Notification> findByPublisherId (String publisherId);

    List<Notification> findByPublisherIdAndTopic (String publisherId, String topic);
}
