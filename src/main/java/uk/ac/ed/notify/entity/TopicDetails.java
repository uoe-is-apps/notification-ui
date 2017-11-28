/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rgood on 20/10/2015.
 */
@Entity
@Table(name = "TOPIC_DETAILS")
@NamedQueries({
        @NamedQuery(name = "TopicDetails.findByTopicId", query = "SELECT a FROM TopicDetails a WHERE a.topicId = (?1)")
})
public class TopicDetails {

    @Id
    @Column(name="TOPIC_ID")
    private String topicId;

    @Column(name="TOPIC_DESCRIPTION")
    private String description;

    @Column(name="PUBLISHER_ID")
    private String publisherId;

    @Column(name="STATUS")
    private String status;

    @JsonSerialize(using=DatePartSerializer.class)
    @Column(name="LAST_UPDATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
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
