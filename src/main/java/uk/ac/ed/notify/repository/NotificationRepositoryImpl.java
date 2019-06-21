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

@Transactional(value="notifyTransactionManager")
public class NotificationRepositoryImpl implements NotificationRepositoryAddon {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@PersistenceContext(unitName="notifyPersistenceUnit")
	private EntityManager entityManager;
	 
	@Value("${hibernate.jdbc.batch_size:25}")
	private int batchSize;


	public void bulkPurgeArchive(int archiveNumOfDays, int purgeNumOfDays, int archiveRecordLimit){
		int resultNotificationUsers;
		int resultNotifications;

		logger.info("bulkPurgeArchive start... archive after " + archiveNumOfDays + " days : purge after " +
				purgeNumOfDays + " days : " + archiveRecordLimit + " notifications per cycle");

		// do the purge first.

		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // Now use today's date.
		c.add(Calendar.DATE, -purgeNumOfDays);
		Timestamp to = new Timestamp(c.getTime().getTime());

		Query query = entityManager.createNativeQuery("SELECT na.notification_id "
				+ " FROM notifications_archive na"
				+ " WHERE na.end_date < :timestampTo");
		query.setParameter("timestampTo", to);
		if (archiveRecordLimit > 0) {
			query.setMaxResults(archiveRecordLimit);
		}

		@SuppressWarnings("unchecked")
		List<String> nail = (List<String>)query.getResultList();

		logger.info("Set up notifications to purge from archive - " + nail.size());

		if (nail.size() > 0) {
			query = entityManager.createNativeQuery(
					"DELETE FROM notification_users_archive WHERE notification_id in :idList"
			);
			query.setParameter("idList", nail);
			resultNotificationUsers = query.executeUpdate();
			logger.info("Purged from notification_users_archive - " + resultNotificationUsers);

			query = entityManager.createNativeQuery(
					"DELETE FROM notifications_archive WHERE notification_id in :idList"
			);
			query.setParameter("idList", nail);
			resultNotifications = query.executeUpdate();
			logger.info("Purged from notifications_archive - " + resultNotifications);
		}


		// Then move records from notification(_user)s to equivalent archive tables.

		c = Calendar.getInstance();
		c.setTime(new Date()); // Again use today's date.
		c.add(Calendar.DATE, -archiveNumOfDays);
		to = new Timestamp(c.getTime().getTime());

		query = entityManager.createNativeQuery("SELECT n.notification_id "
				+ " FROM notifications n"
				+ " WHERE n.end_date < :timestampTo");
		query.setParameter("timestampTo", to);
		if (archiveRecordLimit > 0) {
			query.setMaxResults(archiveRecordLimit);
		}

		nail = (List<String>)query.getResultList();

		logger.info("Set up notifications to archive - " + nail.size());

		if (nail.size() > 0) {

			query = entityManager.createNativeQuery(
					"INSERT INTO notifications_archive "
							+ "(notification_id, publisher_id, topic, publisher_notification_id, title, notification_body, "
							+ " notification_url, start_date, end_date, last_updated, notification_group, notification_group_name)"
							+ " SELECT notification_id, publisher_id, topic, publisher_notification_id, title, notification_body, "
							+ " notification_url, start_date, end_date, last_updated, notification_group, notification_group_name "
							+ " FROM notifications WHERE notification_id  in :idList ");
			query.setParameter("idList", nail);
			resultNotifications = query.executeUpdate();
			logger.info("Copied notifications - " + resultNotifications);

			query = entityManager.createNativeQuery(
					"INSERT INTO notification_users_archive "
							+ "(notification_id, uun, last_updated)"
							+ " SELECT notification_id, uun, last_updated "
							+ " FROM notification_users WHERE notification_id in :idList ");
			query.setParameter("idList", nail);
			resultNotifications = query.executeUpdate();
			logger.info("Copied notification_users - " + resultNotifications);

			query = entityManager.createNativeQuery(
					"DELETE FROM notification_users WHERE notification_id in :idList"
			);
			query.setParameter("idList", nail);
			resultNotificationUsers = query.executeUpdate();
			logger.info("Removed from notification_users - " + resultNotificationUsers);

			query = entityManager.createNativeQuery(
					"DELETE FROM notifications WHERE notification_id in :idList"
			);
			query.setParameter("idList", nail);
			resultNotifications = query.executeUpdate();
			logger.info("Removed from notifications - " + resultNotifications);
		}

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
