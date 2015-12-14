package uk.ac.ed.notify.repository;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ed.notify.entity.NotificationError;

/**
 * Created by rgood on 20/10/2015.
 */
public interface NotificationErrorRepository extends CrudRepository<NotificationError,String> {
}
