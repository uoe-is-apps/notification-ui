package uk.ac.ed.notify.repository;

import org.springframework.transaction.annotation.Transactional;
import uk.ac.ed.notify.entity.NotificationArchiveItem;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Transactional(value="notifyTransactionManager")
public class NotificationArchiveItemRepositoryImpl implements NotificationArchiveItemRepositoryAddon  {

	@PersistenceContext(unitName="notifyPersistenceUnit")
	private EntityManager entityManager;

	@Override
	public List<NotificationArchiveItem> bulkSave(List<NotificationArchiveItem> entities) {

		final List<NotificationArchiveItem> savedEntities = new ArrayList<>(entities.size());

		for (NotificationArchiveItem nai : entities) {
			savedEntities.add(save(nai));
		}
		return savedEntities;
	}
	
	/**
	 *
	 * @param notification_archive_item to be saved or updated
	 * @return notification_archive_item
	 *
	 * notification is saved
	 */
	private NotificationArchiveItem save(NotificationArchiveItem notification_archive_item) {
		if (notification_archive_item.getNotificationId() == null) {
			entityManager.persist(notification_archive_item);
			return notification_archive_item;
		}
		else {
			return entityManager.merge(notification_archive_item);
		}
	}
}
