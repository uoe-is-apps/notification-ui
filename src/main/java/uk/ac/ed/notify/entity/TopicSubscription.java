package uk.ac.ed.notify.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

/**
 * Created by rgood on 20/10/2015.
 */
@Entity
@Table(name="TOPIC_SUBSCRIPTIONS")
public class TopicSubscription {

    @Id
    @Column(name="SUBSCRIPTION_ID")
    @GeneratedValue(generator = "sysguid")
    @GenericGenerator(name = "sysguid", strategy = "guid")
    private String subscriptionId;

    @Column(name="SUBSCRIBER_ID")
    private String subscriberId;

    @Column(name="TOPIC")
    private String topic;

    @Column(name="STATUS")
    private String status;

    @JsonSerialize(using=DatePartSerializer.class)
    @Column(name="LAST_UPDATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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
