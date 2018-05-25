/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.learn.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author hsun1
 */
@Entity
@Table(name = "course_main", schema="Bblearn")
@NamedQueries({
    @NamedQuery(name = "CourseMain.findAll", query = "SELECT v FROM CourseMain v"),
    @NamedQuery(name = "CourseMain.findByPk1", query = "SELECT v FROM CourseMain v WHERE v.pk1 = (?1)")   
})
public class CourseMain {
    private static final long serialVersionUID = 1L;
    
//pk1                  not null number(38)     
//course_name          not null nvarchar2(333) 
//course_id            not null varchar2(100)    
//batch_uid                     nvarchar2(256) 
//start_date                    date      
//end_date                    date    

    @Basic(optional = false)
    @Column(name = "pk1")
    @Id
    private Integer pk1;

    @Basic(optional = false)
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;  

    @Basic(optional = false)
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;  
    
    @Basic(optional = false)
    @Column(name = "course_name")
    private String courseName;    
    
    @Basic(optional = false)
    @Column(name = "course_id")
    private String courseId;    
    
    @Basic(optional = false)
    @Column(name = "batch_uid")
    private String batchUid;      

    @Basic(optional = false)
    @Column(name = "dtcreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcreated; 

    @Basic(optional = false)
    @Column(name = "available_ind")
    private String availableInd;  
    

    public String getAvailableInd() {
        return availableInd;
    }

    public void setAvailableInd(String availableInd) {
        this.availableInd = availableInd;
    }

    public Date getDtcreated() {
        return dtcreated;
    }

    public void setDtcreated(Date dtcreated) {
        this.dtcreated = dtcreated;
    }
    
    public Integer getPk1() {
        return pk1;
    }

    public void setPk1(Integer pk1) {
        this.pk1 = pk1;
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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getBatchUid() {
        return batchUid;
    }

    public void setBatchUid(String batchUid) {
        this.batchUid = batchUid;
    }
    
    
   


}
