package uk.ac.ed.notify.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Embeddable
public class NotificationUserPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="notification_id")
	@JsonIgnore
	private String notificationId;
	
	@Column(name = "uun")
	private String uun;
	
	public NotificationUserPK() {}
	
	public NotificationUserPK(String notificationId, String uun){
		this.notificationId = notificationId;
		this.uun = uun;
	}
    @JsonIgnore
	public String getNotificationId() {
		return notificationId;
	}

	public String getUun() {
		return uun;
	}
    @JsonProperty
	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public void setUun(String uun) {
		this.uun = uun;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof NotificationUserPK)) {
			return false;
		}
		final NotificationUserPK pk = (NotificationUserPK) obj;
		if (!pk.getUun().equals(this.getUun())) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int result;
		result = getUun().hashCode();
		result += getNotificationId().hashCode();
		return result;
	}
}