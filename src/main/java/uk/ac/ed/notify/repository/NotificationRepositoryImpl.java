package uk.ac.ed.notify.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.ed.notify.entity.Notification;

@Transactional(value="notifyTransactionManager")
public class NotificationRepositoryImpl implements NotificationRepositoryAddon {

	@PersistenceContext(unitName="notifyPersistenceUnit")
	private EntityManager entityManager;
	 
	@Value("${hibernate.jdbc.batch_size:25}")
	private int batchSize;

	@Override
	public List<Notification> bulkSave(List<Notification> entities) {
		final List<Notification> savedEntities = new ArrayList<Notification>(entities.size());
		int count = 0;
		
		for (Notification notification : entities) {
			savedEntities.add(save(notification));
			count++;
			if (count % batchSize == 0) {
				
				entityManager.flush();
				entityManager.clear();
			}
		}
		return savedEntities;
	}
	
	/**
	 * 
	 * @param notification to be saved or updated
	 * @return notification
	 * 
	 * notification is saved 
	 */
	private Notification save(Notification notification) {
		if (notification.getNotificationId() == null) {
			entityManager.persist(notification);
			return notification;
		}
		else {
			return entityManager.merge(notification);
		}
	}
}
