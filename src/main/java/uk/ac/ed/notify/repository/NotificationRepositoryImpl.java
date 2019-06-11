package uk.ac.ed.notify.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.ed.notify.entity.Notification;
import uk.ac.ed.notify.entity.NotificationArchiveItem;

@Transactional(value="notifyTransactionManager")
public class NotificationRepositoryImpl implements NotificationRepositoryAddon {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@PersistenceContext(unitName="notifyPersistenceUnit")
	private EntityManager entityManager;
	 
	@Value("${hibernate.jdbc.batch_size:25}")
	private int batchSize;

	@Autowired
	NotificationArchiveItemRepository notificationArchiveItemRepository;

	public void bulkPurgeArchive(int archiveNumOfDays, int purgeNumOfDays, int archiveRecordLimit){
        logger.info("bulkPurgeArchive start...");

		entityManager.createNativeQuery("TRUNCATE TABLE notification_archive_items").executeUpdate();
		logger.info("Truncate notification_archive_items");

		// @todo begin transaction

		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // Now use today date.
		c.add(Calendar.DATE, -archiveRecordLimit); // Adding 5 days
		Timestamp to = new Timestamp(c.getTime().getTime());

		Query query = entityManager.createQuery("SELECT n "
				+ " FROM notifications n"
				+ " WHERE n.end_date < :timestampTo");
		query.setParameter("timestampTo", to);
		query.setMaxResults(archiveRecordLimit);

		@SuppressWarnings("unchecked")
		List<NotificationArchiveItem> nail = (List<NotificationArchiveItem>)query.getResultList();

		notificationArchiveItemRepository.bulkSave(nail);
		logger.info("Set up records to archive - " + nail.size());

		int resultNotifications = entityManager.createNativeQuery(
			"INSERT INTO notifications_archive "
				+ "(notification_id,  publisher_id, topic, publisher_notification_id, title, notification_body, "
				+ "notification_url, start_date, end_date, last_updated, notification_group, notification_group_name)"
				+ " SELECT notification_id,  publisher_id, topic, publisher_notification_id, title, notification_body, "
				+ "notification_url, start_date, end_date, last_updated, notification_group, notification_group_name "
				+ " FROM notifications WHERE notification_id in "
				+ " (SELECT notification_id FROM notification_archive_items)"
		)
				.executeUpdate();
		logger.info("Move notifications to archive - " + resultNotifications);

		int resultNotificationUsers = entityManager.createNativeQuery(
			"INSERT INTO notification_users_archive (notification_id, uun, last_updated)"
				+ " SELECT notification_id, uun, last_updated FROM notification_users WHERE notification_id in "
				+ " (SELECT notification_id FROM notification_archive_items)"
		)
				.executeUpdate();
		logger.info("Move notification_users to archive - " + resultNotificationUsers);

		resultNotificationUsers = entityManager.createNativeQuery(
			"DELETE FROM notification_users WHERE notification_id in "
				+ " (SELECT notification_id FROM notification_archive_items)"
		)
				.executeUpdate();
		logger.info("Deleted from notification_users - " + resultNotificationUsers);

		resultNotifications = entityManager.createNativeQuery(
			"DELETE FROM notifications WHERE notification_id in "
				+ " (SELECT notification_id FROM notification_archive_items)"
		)
				.executeUpdate();
		logger.info("Deleted from notifications - " + resultNotifications);

		// @todo Commit TRansaction

		entityManager.createNativeQuery("TRUNCATE TABLE notification_archive_items").executeUpdate();
		logger.info("Truncate notification_archive_items");

		// @todo Begin Transaction

		int resultNotificationList = entityManager.createNativeQuery(
			"INSERT INTO notification_archive_items (notification_id) "
				+ " SELECT notification_id FROM notifications_archive WHERE end_date < (sysdate-" + archiveRecordLimit + ") LIMIT 1000"
		)
				.executeUpdate();
		logger.info("Set up archive records to delete - " + resultNotificationList);

		resultNotificationUsers = entityManager.createNativeQuery(
			"DELETE FROM notification_users_archive WHERE notification_id in "
				+ " (SELECT notification_id FROM notification_archive_items)"
		)
				.executeUpdate();
		logger.info("Deleted from notification_users_archive - " + resultNotificationUsers);

		resultNotifications = entityManager.createNativeQuery(
			"DELETE FROM notifications_archive WHERE notification_id in "
				+ " (SELECT notification_id FROM notification_archive_items)"
		)
				.executeUpdate();
		logger.info("Deleted from notifications_archive - " + resultNotifications);

		// @todo Commit TRansaction

		entityManager.createNativeQuery("TRUNCATE TABLE notification_archive_items").executeUpdate();
		logger.info("Truncate notification_archive_items");

		logger.info("bulkPurgeArchive complete...");

    }
        
        
	@Override
	public List<Notification> bulkSave(List<Notification> entities) {
		final List<Notification> savedEntities = new ArrayList<>(entities.size());
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
