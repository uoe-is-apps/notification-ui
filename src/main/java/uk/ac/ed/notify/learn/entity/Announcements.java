/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.learn.entity;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author hsun1
 */
@Entity
@Table(name = "announcements", schema="Bblearn")
@NamedQueries({    
    @NamedQuery(name = "Announcements.findCourseAnnouncements", 
     query = 
     //"select u.pk1 as userId, u.userId as uun, m.courseName, m.pk1 as pk1,m.courseId as courseId, m.startDate as startDate, m.endDate as endDate " +     
     //", s.startDate, s.endDate, s.subject, s.crsmainPk1 " +     
     "SELECT v from Users u, CourseUsers cu, CourseMain m, " +     
     "Announcements v " +     
     "where " +     
     "u.pk1 = cu.usersPk1 and m.pk1 = cu.crsmainPk1 and (m.availableInd = 'Y' and m.startDate <= sysdate and sysdate <= m.endDate or m.endDate is null)  " +     
     //"and u.userId='admin.hsun1' " +     
     "and v.crsmainPk1=m.pk1 " +     
     "and v.announcementType='C' " +   
        
     //apply active filter   
     "AND u.availableInd = 'Y' AND u.rowStatus = '0' " + //users
     "AND cu.rowStatus = '0' " +  //CourseUsers        
     //"AND ((m.availableInd = 'Y' and m.endDate is null) or (m.availableInd = 'Y' and sysdate <= m.endDate))  " +  //CourseMain 
        
        
     "AND (v.endDate is null or (v.endDate is not null and v.endDate >= sysdate)) " +
     "AND (v.startDate is null or (v.startDate is not null and v.startDate <= sysdate)) "   
        
     //"order by m.dtcreated desc "
     ),    
    /*
    @NamedQuery(name = "Announcements.findCourseAnnouncements", 
     query = 
     //"select u.pk1 as userId, u.userId as uun, m.courseName, m.pk1 as pk1,m.courseId as courseId, m.startDate as startDate, m.endDate as endDate " +     
     //", s.startDate, s.endDate, s.subject, s.crsmainPk1 " +     
     "SELECT v from Users u, CourseUsers cu, CourseMain m, " +     
     "Announcements v " +     
     "where " +     
     "u.pk1 = cu.usersPk1 and m.pk1 = cu.crsmainPk1 and (m.startDate <= sysdate and sysdate <= m.endDate or m.endDate is null)  " +     
     //"and u.userId='admin.hsun1' " +     
     "and v.crsmainPk1=m.pk1 " +     
     "and v.announcementType='C' " +   
     "order by m.dtcreated desc "
     ),
     */
    @NamedQuery(name = "Announcements.findSystemAnnouncements", 
     query = 
     "select v " +     
     "from  " +     
     "Announcements v " +     
     "where " +     
     "v.announcementType='S' " +     
        
  
     "AND (v.endDate is null or (v.endDate is not null and v.endDate >= sysdate)) " +
     "AND (v.startDate is null or (v.startDate is not null and v.startDate <= sysdate)) "   
                
        
     //"order by v.startDate desc "
     ),
    @NamedQuery(name = "Announcements.findAll", query = "SELECT v FROM Announcements v"),
    @NamedQuery(name = "Announcements.findByPk1", query = "SELECT v FROM Announcements v WHERE v.pk1 = (?1)")     
})
public class Announcements {
    private static final long serialVersionUID = 1L;
     
//pk1               not null number(38)     
//end_date                   date           
//subject                    nvarchar2(333)     
//start_date                 date         
//crsmain_pk1       not null number(38)         
//announcement               nclob          
//users_pk1         not null number(38)     
//announcement_type          char(1)      
    
    @Basic(optional = false)
    @Column(name = "pk1")
    @Id
    private Integer pk1;

    @Basic(optional = false)
    @Column(name = "crsmain_pk1")
    private Integer crsmainPk1;    
    
    @Basic(optional = false)
    @Column(name = "users_pk1")
    private Integer usersPk1;  
    
    @Basic(optional = false)
    @Column(name = "subject")
    private String subject;    

    @Basic(optional = false)
    @Column(name = "announcement")
    private String announcement;        
    
    @Basic(optional = false)
    @Column(name = "announcement_type")
    private String announcementType;      
    
    @Basic(optional = false)
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;  

    @Basic(optional = false)
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;  
   
    
//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinTable(name="USERS", joinColumns = {@JoinColumn (name="users_pk1")},
//            inverseJoinColumns = {@JoinColumn(name="pk1",nullable = false,updatable = false)})
    /*
    @Transient
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    */

    
    
    public Integer getPk1() {
        return pk1;
    }

    public void setPk1(Integer pk1) {
        this.pk1 = pk1;
    }

    public Integer getCrsmainPk1() {
        return crsmainPk1;
    }

    public void setCrsmainPk1(Integer crsmainPk1) {
        this.crsmainPk1 = crsmainPk1;
    }

    public Integer getUsersPk1() {
        return usersPk1;
    }

    public void setUsersPk1(Integer usersPk1) {
        this.usersPk1 = usersPk1;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getAnnouncementType() {
        return announcementType;
    }

    public void setAnnouncementType(String announcementType) {
        this.announcementType = announcementType;
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

    @Override
    public String toString() {
        return "Announcements{" + "pk1=" + pk1 + ", crsmainPk1=" + crsmainPk1 + ", usersPk1=" + usersPk1 + ", subject=" + subject + ", announcement=" + announcement + ", announcementType=" + announcementType + ", startDate=" + startDate + ", endDate=" + endDate + '}';
    }



  
    
    
}
