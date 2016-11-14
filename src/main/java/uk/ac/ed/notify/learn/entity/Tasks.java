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
@Table(name = "tasks", schema="Bblearn")
@NamedQueries({

    @NamedQuery(name = "Tasks.findTasks", 
     query = 

     "SELECT v from Users u, CourseUsers cu, CourseMain m, " +    
     "Tasks v " +     
     "where " +     
     "u.pk1 = cu.usersPk1 and m.pk1 = cu.crsmainPk1 and (m.startDate <= sysdate and sysdate <= m.endDate or m.endDate is null) " +     
     //"and u.userId='admin.hsun1' " +     
     "and v.crsmainPk1=m.pk1 " +     
       
     //apply active filter   
     "AND u.availableInd = 'Y' AND u.rowStatus = '0' " + //users
     "AND cu.rowStatus = '0' " +  //CourseUsers        
     "AND ((m.availableInd = 'Y' and m.endDate is null) or (m.availableInd = 'Y' and sysdate <= m.endDate))  " +  //CourseMain 

        
        
     "AND (   (v.dueDate is null) or (sysdate <= v.dueDate)   )  " +  //Tasks         
        
        
        
     "order by m.dtcreated desc "
     ),    

    @NamedQuery(name = "Tasks.findAll", query = "SELECT v FROM Tasks v"),
    @NamedQuery(name = "Tasks.findByPk1", query = "SELECT v FROM Tasks v WHERE v.pk1 = (?1)")
   
})
public class Tasks {
    private static final long serialVersionUID = 1L;
//    pk1
//    duedate
//    priority
//    subject
//    task_type = C
//    crsmain_pk1
            

    @Basic(optional = false)
    @Column(name = "pk1")
    @Id
    private Integer pk1;

    @Basic(optional = false)
    @Column(name = "crsmain_pk1")
    private Integer crsmainPk1;        
    
    @Basic(optional = false)
    @Column(name = "duedate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;  
    
    @Basic(optional = false)
    @Column(name = "priority")
    private String priority;       
    
    @Basic(optional = false)
    @Column(name = "subject")
    private String subject;  
    
    @Basic(optional = false)
    @Column(name = "description")
    private String description;          
    
    @Basic(optional = false)
    @Column(name = "task_type")
    private String taskType;       

    @Basic(optional = false)
    @Column(name = "users_pk1")
    private Integer usersPk1;    

    public Integer getUsersPk1() {
        return usersPk1;
    }

    public void setUsersPk1(Integer usersPk1) {
        this.usersPk1 = usersPk1;
    }
        
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    
    
    
               
            
}
