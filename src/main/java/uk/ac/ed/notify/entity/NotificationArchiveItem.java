package uk.ac.ed.notify.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by rgood on 18/09/2015.
 */
@Entity
@Table(name = "notification_archive_items")

public class NotificationArchiveItem {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid",
            strategy = "uuid")
    @Column(name = "NOTIFICATION_ID")
    private String notificationId;

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

}
