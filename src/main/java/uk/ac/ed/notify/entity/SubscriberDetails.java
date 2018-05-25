package uk.ac.ed.notify.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rgood on 20/10/2015.
 */
@Entity
@Table(name="SUBSCRIBER_DETAILS")
public class SubscriberDetails {

    @Id
    @Column(name="SUBSCRIBER_ID")
    private String subscriberId;

    @Column(name = "SUBSCRIBER_DESCRIPTION")
    private String description;

    @Column(name = "SUBSCRIBER_TYPE")
    private String type;

    @Column(name = "STATUS")
    private String status;

    @JsonSerialize(using=DatePartSerializer.class)
    @Column(name="LAST_UPDATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
