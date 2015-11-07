package uk.ac.ed.notify.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rgood on 20/10/2015.
 */
@Entity
@Table(name="USER_NOTIFICATION_AUDIT", schema="NOTIFY")
public class UserNotificationAudit {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",
            strategy = "uuid")
    @Column(name="AUDIT_ID")
    private String auditId;

    @Column(name="AUDIT_DESCRIPTION")
    private String auditDescription;

    @Column(name="UUN")
    private String uun;

    @Column(name="PUBLISHER_ID")
    private String publisherId;

    @Column(name="ACTION")
    private String action;

    @JsonSerialize(using=DatePartSerializer.class)
    @Column(name="AUDIT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date auditDate;

    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }

    public String getUun() {
        return uun;
    }

    public void setUun(String uun) {
        this.uun = uun;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditDescription() {
        return auditDescription;
    }

    public void setAuditDescription(String auditDescription) {
        this.auditDescription = auditDescription;
    }
}
