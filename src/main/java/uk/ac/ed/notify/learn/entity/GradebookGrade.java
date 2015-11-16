/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.learn.entity;

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
@Table(name = "gradebook_grade", schema="Bblearn")
@NamedQueries({


    @NamedQuery(name = "GradebookGrade.findAll", query = "SELECT v FROM GradebookGrade v"),
    @NamedQuery(name = "GradebookGrade.findByGradebookMainPk1", query = "SELECT v FROM GradebookGrade v WHERE v.gradebookMainPk1 = (?1)")

 
        
})
public class GradebookGrade implements Serializable{
    private static final long serialVersionUID = 1L;
//    manual_score NUMBER(15,5)   
//    manual_grade NVARCHAR2(32)  
//    average_score NUMBER(15,5)   
//    last_override_date DATE
//    gradebook_main_pk1 NUMBER(38)     
    
    @Basic(optional = false)
    @Column(name = "pk1")
    @Id
    private Integer pk1;

    @Basic(optional = false)
    @Column(name = "course_users_pk1")
    private Integer courseUsersPk1;    
    
    @Basic(optional = false)
    @Column(name = "gradebook_main_pk1")
    private Integer gradebookMainPk1;

    @Basic(optional = false)
    @Column(name = "last_override_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastOverrideDate;  

    @Basic(optional = false)
    @Column(name = "manual_grade")
    private String manualGrade;    
      
    @Basic(optional = false)
    @Column(name = "average_score")
    private Integer averageScore;
    
    @Basic(optional = false)
    @Column(name = "manual_score")
    private Integer manualScore;   

    public Integer getPk1() {
        return pk1;
    }

    public void setPk1(Integer pk1) {
        this.pk1 = pk1;
    }

    public Integer getGradebookMainPk1() {
        return gradebookMainPk1;
    }

    public void setGradebookMainPk1(Integer gradebookMainPk1) {
        this.gradebookMainPk1 = gradebookMainPk1;
    }

    public Date getLastOverrideDate() {
        return lastOverrideDate;
    }

    public void setLastOverrideDate(Date lastOverrideDate) {
        this.lastOverrideDate = lastOverrideDate;
    }

    public String getManualGrade() {
        return manualGrade;
    }

    public void setManualGrade(String manualGrade) {
        this.manualGrade = manualGrade;
    }

    public Integer getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Integer averageScore) {
        this.averageScore = averageScore;
    }

    public Integer getManualScore() {
        return manualScore;
    }

    public void setManualScore(Integer manualScore) {
        this.manualScore = manualScore;
    }

    public Integer getCourseUsersPk1() {
        return courseUsersPk1;
    }

    public void setCourseUsersPk1(Integer courseUsersPk1) {
        this.courseUsersPk1 = courseUsersPk1;
    }

    @Override
    public String toString() {
        return "GradebookGrade{" + "pk1=" + pk1 + ", courseUsersPk1=" + courseUsersPk1 + ", gradebookMainPk1=" + gradebookMainPk1 + ", lastOverrideDate=" + lastOverrideDate + ", manualGrade=" + manualGrade + ", averageScore=" + averageScore + ", manualScore=" + manualScore + '}';
    }
    
    
    
}

