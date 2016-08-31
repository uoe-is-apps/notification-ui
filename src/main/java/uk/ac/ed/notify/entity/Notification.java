package uk.ac.ed.notify.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.hibernate.annotations.GenericGenerator;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by rgood on 18/09/2015.
 */
@Entity
@Table(name = "NOTIFICATIONS")
@NamedQueries({
        @NamedQuery(name = "Notification.findByPublisherIdAndTopic", query = "SELECT a FROM Notification a WHERE a.publisherId = (?1) and a.topic = (?2)"),
        @NamedQuery(name = "Notification.findByPublisherId", query = "SELECT a FROM Notification a WHERE a.publisherId = (?1)"),
        @NamedQuery(name = "Notification.findByPublisherIdAndPublisherNotificationId", query = "SELECT a FROM Notification a WHERE a.publisherId = (?1) and a.publisherNotificationId  = (?2)")
})
public class Notification {


    private static final Logger logger = LoggerFactory.getLogger(Notification.class);

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid",
            strategy = "uuid")
    @Column(name = "NOTIFICATION_ID")
    private String notificationId;

    @Column(name = "PUBLISHER_ID")
    private String publisherId;

    @Column(name = "PUBLISHER_NOTIFICATION_ID")
    private String publisherNotificationId;

    @Column(name = "TOPIC")
    private String topic;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "NOTIFICATION_BODY")
    private String body;

    @Column(name = "NOTIFICATION_URL")
    private String url;

    @JsonSerialize(using = DatePartSerializer.class)
    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @JsonSerialize(using = DatePartSerializer.class)
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @JsonSerialize(using = DatePartSerializer.class)
    @Column(name = "LAST_UPDATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @Transient 
    private String publisherKey;
    
    @Transient 
    private String action;      

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationUser> notificationUsers = new ArrayList<NotificationUser>();
    
    @PrePersist
    @PreUpdate
    public void onSave() {
    	this.setLastUpdated(new Date());
    }
    
    public String getPublisherKey() {
        return publisherKey;
    }

    public void setPublisherKey(String publisherKey) {
        this.publisherKey = publisherKey;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
        
    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherNotificationId() {
        return publisherNotificationId;
    }

    public void setPublisherNotificationId(String publisherNotificationId) {
        this.publisherNotificationId = publisherNotificationId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {

        String cleaned = Jsoup.clean(topic, Whitelist.basic());
        if (!cleaned.equals(topic)) {
            logger.debug("notification topic for "+notificationId+"cleaned, was ("+topic);
        }
        this.topic = cleaned;
    }

    public String getTitle() {
        if(title == null){
            return "";
        }
        
        String cleaned = Jsoup.clean(title, Whitelist.basic());
        if (!cleaned.equals(title)) {
            logger.debug("notification title for "+notificationId+"cleaned, was ("+title);
        }
        this.title = cleaned;        
        
        return title;
    }

    public void setTitle(String title) {
        String cleaned = Jsoup.clean(title, Whitelist.basic());
        if (!cleaned.equals(title)) {
            logger.debug("notification title for "+notificationId+"cleaned, was ("+title);
        }
        this.title = cleaned;
    }

    public String getBody() {
        if(body == null){
            return "";
        }
        
        String cleaned = Jsoup.clean(body, Whitelist.basic());
        if (!cleaned.equals(body)) {
            logger.debug("notification body for "+notificationId+"cleaned, was ("+body + ") after clean - " + cleaned);
        }
        body = cleaned;        
        
        return body;
    }

    public void setBody(String body) {
        String cleaned = Jsoup.clean(body, Whitelist.basic());
        if (!cleaned.equals(body)) {
            logger.debug("notification body for "+notificationId+"cleaned, was ("+body + ") after clean - " + cleaned);
        }
        this.body = cleaned;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {

        String cleaned = Jsoup.clean(url, Whitelist.basic());
        if (!cleaned.equals(url)) {
            logger.debug("notification url for "+notificationId+"cleaned, was ("+url);
        }
        this.url = cleaned;
    }
    
    public List<NotificationUser> getNotificationUsers() {
		return notificationUsers;
	}

	public void setNotificationUsers(List<NotificationUser> notificationUsers) {
		this.notificationUsers = notificationUsers;
	}

	@Override
    public String toString() {
        return "Notification{" + "notificationId=" + notificationId + ", publisherId=" + publisherId + ", publisherNotificationId=" + publisherNotificationId + ", topic=" + topic + ", title=" + title + ", body=" + body + ", url=" + url + ", startDate=" + startDate + ", endDate=" + endDate + ", action=" + action + ", publisherKey=" + publisherKey + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Notification other = (Notification) obj;
        if (!Objects.equals(this.publisherId, other.publisherId)) {
            return false;
        }
        if (!Objects.equals(this.publisherNotificationId, other.publisherNotificationId)) {
            return false;
        }
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }        
        if (!Objects.equals(this.topic, other.topic)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.body, other.body)) {
            return false;
        }
        if (!Objects.equals(this.startDate, other.startDate)) {
            return false;
        }
        if (!Objects.equals(this.endDate, other.endDate)) {
            return false;
        }
        return true;
    }


}
