/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author hsun1
 */
@Entity
@Table(name = "OFFICE365_SUBSCRIPTION")
public class Office365Subscription {

    @Id
    @Column(name = "SUBSCRIPTION_ID")
    private String subscriptionId;
    @JsonSerialize(using = DatePartSerializer.class)
    @Column(name = "SUBSCRIPTION_RENEW")
    @Temporal(TemporalType.TIMESTAMP)
    private Date subscriptionRenew;
    @JsonSerialize(using = DatePartSerializer.class)
    @Column(name = "SUBSCRIPTION_EXPIRY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date subscriptionExpiry;

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Date getSubscriptionRenew() {
        return subscriptionRenew;
    }

    public void setSubscriptionRenew(Date subscriptionRenew) {
        this.subscriptionRenew = subscriptionRenew;
    }

    public Date getSubscriptionExpiry() {
        return subscriptionExpiry;
    }

    public void setSubscriptionExpiry(Date subscriptionExpiry) {
        this.subscriptionExpiry = subscriptionExpiry;
    }
}
