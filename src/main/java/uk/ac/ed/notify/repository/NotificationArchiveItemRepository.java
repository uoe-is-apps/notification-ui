package uk.ac.ed.notify.repository;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ed.notify.entity.NotificationArchiveItem;

public interface NotificationArchiveItemRepository extends CrudRepository<NotificationArchiveItem,String>, NotificationArchiveItemRepositoryAddon {

}
