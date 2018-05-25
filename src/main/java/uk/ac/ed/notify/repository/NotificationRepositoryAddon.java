package uk.ac.ed.notify.repository;

import java.util.List;

import uk.ac.ed.notify.entity.Notification;

/*
 * an add on to NotificationRepository that allows saving in batches
 */
public interface NotificationRepositoryAddon {

	List<Notification> bulkSave(List<Notification> entities);
        
        void bulkDelete(int numOfDays);
}