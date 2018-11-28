package uk.ac.ed.notify.repository;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ed.notify.entity.UserNotificationAudit;

import java.util.List;

/**
 * Created by rgood on 20/10/2015.
 */
public interface UserNotificationAuditRepository extends CrudRepository<UserNotificationAudit,String> {

    List<UserNotificationAudit> findByNotificationIdAndAction(String notificationId, String action);

}
