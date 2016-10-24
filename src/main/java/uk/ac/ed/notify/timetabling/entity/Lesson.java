/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.timetabling.entity;

import java.io.Serializable;
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
@Table(name = "cs_lesson") 
@NamedQueries({
    @NamedQuery(name = "Lesson.findAllLessons", query = "SELECT v FROM Lesson v WHERE v.startDate < (?1) "), //current_date()
    @NamedQuery(name = "Lesson.findLessonByTimetableid", query = "SELECT v FROM Lesson v WHERE v.timetableId = (?1)")          
     /*   
     ,
     @NamedQuery(name = "Lesson.findAllLessons", 
     query = 
     "select student2cg from " + 
     "Lesson lesson, " + 
     "Room room " + 
     "where " + 
     "lession2cg.courseGroup=student2cg.courseGroup " + 
     "and " + 
     "lession2cg.timetableId = (?1) "
     )      
     */         
})

public class Lesson  implements Serializable{
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Column(name = "timetableId")
    @Id
    private String timetableId;  

    @Basic(optional = false)
    @Column(name = "startDate")
    @Temporal(TemporalType.DATE)    
    private Date startDate;
    
    @Basic(optional = false)
    @Column(name = "startTime")
    private String startTime;
    
    @Basic(optional = false)
    @Column(name = "endTime")  
    private String endTime;
        
    @Basic(optional = false)
    @Column(name = "roomId")    
    private String roomId;
    
    @Basic(optional = false)
    @Column(name = "subject")    
    private String subject;
    
    @Basic(optional = false)
    @Column(name = "course")    
    private String course;
    
    @Basic(optional = false)
    @Column(name = "activityType")    
    private String activityType;

    public String getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(String timetableId) {
        this.timetableId = timetableId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
    
    
    
    
}
