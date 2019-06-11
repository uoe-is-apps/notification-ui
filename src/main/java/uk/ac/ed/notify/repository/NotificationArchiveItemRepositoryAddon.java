package uk.ac.ed.notify.repository;

import java.util.List;

import uk.ac.ed.notify.entity.NotificationArchiveItem;

/*
 * an add on to NotificationArchiveItemRepository that allows saving in batches
 */
public interface NotificationArchiveItemRepositoryAddon {

	List<NotificationArchiveItem> bulkSave(List<NotificationArchiveItem> entities);

}