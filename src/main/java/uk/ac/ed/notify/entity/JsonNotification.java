package uk.ac.ed.notify.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import java.util.List;

/**
 * Created by rgood on 18/09/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonNotification {

        private String notificationId;


        private String publisherId;


        private String publisherNotificationId;


        private String topic;


        private String title;


        private String body;


        private String url;

        @JsonSerialize(using=DatePartSerializer.class)
        private Date startDate;

        @JsonSerialize(using=DatePartSerializer.class)
        private Date endDate;

        @JsonSerialize(using=DatePartSerializer.class)
        private Date lastUpdated;
        
        
        private List<NotificationUser> notificationUsers; 
        

        public String getNotificationId() {
                return notificationId;
        }

        public void setNotificationId(String notificationId) {
                this.notificationId = notificationId;
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

                this.topic = topic;
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {

                this.title = title;
        }

        public String getBody() {
                return body;
        }

        public void setBody(String body) {

                this.body = body;
        }

        public String getUrl() { return url; }

        public void setUrl(String url) {

                this.url = url;
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

        public Date getLastUpdated() {
                return lastUpdated;
        }

        public void setLastUpdated(Date lastUpdated) {
                this.lastUpdated = lastUpdated;
        }

		public List<NotificationUser> getNotificationUsers() {
			return notificationUsers;
		}

		public void setNotificationUsers(List<NotificationUser> notificationUsers) {
			this.notificationUsers = notificationUsers;
		}
}
