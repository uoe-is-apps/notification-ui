package uk.ac.ed.notify.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rgood on 20/10/2015.
 */
@Entity
@Table(name="NOTIFICATION_ERRORS")
public class NotificationError {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",
            strategy = "uuid")
    @Column(name="ERROR_ID")
    private String errorId;

    @Column(name="ERROR_CODE")
    private String errorCode;

    @Column(name="ERROR_DESCRIPTION")
    private String errorDescription;

    @JsonSerialize(using=DatePartSerializer.class)
    @Column(name="ERROR_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date errorDate;

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public Date getErrorDate() {
        return errorDate;
    }

    public void setErrorDate(Date errorDate) {
        this.errorDate = errorDate;
    }
}
