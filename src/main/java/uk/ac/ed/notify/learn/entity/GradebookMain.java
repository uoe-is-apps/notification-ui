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

/*
select gm.PK1 AS gradebook_id,
    gm.course_contents_pk1,
    gm.TITLE,
    CASE
      WHEN ( NOT gg.MANUAL_SCORE IS NULL)
      AND ( NOT gg.MANUAL_GRADE  IS NULL)
      THEN gg.MANUAL_SCORE
      ELSE NVL(gg.AVERAGE_SCORE,0)
    END SCORE,
   gm.due_date,
    gm.POSSIBLE,
	
    greatest( nvl(att.DATE_MODIFIED, 
	to_date('1970-01-01','yyyy-mm-dd')),nvl(gg.last_override_date, to_date('1970-01-01','yyyy-mm-dd'))) last_override_date,
	
    DECODE(cc.end_date,NULL,'N','Y') ended
	 
	
from 


gradebook_main gm, 
course_main cm, 
course_contents cc, 
course_users cu, 
users u,
gradebook_grade gg,
attempt att
								
where gm.course_contents_pk1 = cc.pk1 
and cu.users_pk1 = u.pk1 
and cu.crsmain_pk1=cm.pk1 
and gm.crsmain_pk1=cm.pk1
and gm.visible_ind = 'Y'
and u.row_status = 0
and cu.row_status=0
and gg.GRADEBOOK_MAIN_PK1(+)=gm.pk1
and att.gradebook_grade_pk1(+)=gg.pk1
and u.user_id='"+uun+"' 
 */
@Entity
@Table(name = "gradebook_main", schema="BBLEARN")
@NamedQueries({
 
/*        
" select gm.pk1 as gradebookId, " +
"     gm.courseContentsPk1, " +
"     gm.title, " +
"     case " +
"       when ( not gg.manualScore is null) " +
"       and ( not gg.manualGrade  is null) " +
"       then gg.manualScore " +
"       else nvl(gg.averageScore,0) " +
"     end score, " +
"     gm.dueDate, " +
"     gm.possible, " +
	
"     greatest( nvl(att.dateModified,  " +
"     to_date('1970-01-01','yyyy-mm-dd')),nvl(gg.lastOverrideDate, to_date('1970-01-01','yyyy-mm-dd'))) lastOverrideDate, " +
	
"     decode(cc.endDate,null,'n','y') ended " +
*/
       
    @NamedQuery(name = "GradebookMain.findGradebookMain", 
     query = 

" select distinct gm " +           
" from  " +

//" GradebookMain gm,  " +
" CourseMain cm,  " +
" CourseContents cc,  " +
" CourseUsers cu,  " +
" Users u, " +
" GradebookGrade gg, " +
//" Attempt att " +
   
//v1      
" GradebookMain gm  " +   
        
//v2
//" ,GradebookMain gm  " +
//" right  join  " +
//" GradebookGrade gg on gg.gradebookMainPk1 =gm.pk1  " +
    
" where gm.courseContentsPk1 = cc.pk1  " +
        
        
 
     //apply active filter   
     "AND u.availableInd = 'Y' AND u.rowStatus = '0' " + //users
     "AND cu.rowStatus = '0' " +  //CourseUsers        
     "AND ((cm.availableInd = 'Y' and cm.endDate is null) or (cm.availableInd = 'Y' and sysdate <= cm.endDate))  " +  //CourseMain 
                   
        
        
" and cu.usersPk1 = u.pk1  " +
" and cu.crsmainPk1=cm.pk1  " +
" and gm.crsmainPk1=cm.pk1 " +   
" and gm.visibleInd = 'Y' " +
" and u.rowStatus = 0 " +
" and cu.rowStatus=0 "
////// + " and gg.gradebookMainPk1=gm.pk1 "
//" and att.gradebookGradePk1=gg.pk1 " +      
//+ " and u.userId='admin.hsun1' "
/*
//" and gg.gradebookMainPk1(+)=gm.pk1 " +
//" and att.gradebookGradePk1(+)=gg.pk1 " +      
"// and u.userId='admin.hsun1' " 
 */                
  ),
    @NamedQuery(name = "GradebookMain.findAll", query = "SELECT gm FROM GradebookMain gm"),
    @NamedQuery(name = "GradebookMain.findByPk1", query = "SELECT gm FROM GradebookMain gm WHERE gm.pk1 = (?1)")  
}) 
public class GradebookMain implements Serializable{
    private static final long serialVersionUID = 1L;
//    gm.PK1 AS gradebook_id,
//    gm.course_contents_pk1,
//    gm.TITLE,
//   gm.due_date,
//    gm.POSSIBLE, 
//PK1                       NOT NULL NUMBER(38) 
// COURSE_CONTENTS_PK1                NUMBER(38)        
//  TITLE                              NVARCHAR2(333) 
//        DUE_DATE                           DATE  
//        POSSIBLE                           FLOAT(126)
//        visibleInd

    @Basic(optional = false)
    @Column(name = "crsmain_pk1")
    private Integer crsmainPk1;        
    
    @Basic(optional = false)
    @Column(name = "pk1")
    @Id
    private Integer pk1;

    @Basic(optional = false)
    @Column(name = "course_contents_pk1")
    private Integer courseContentsPk1;
    
    @Basic(optional = false)
    @Column(name = "title")
    private String title;       
    
    @Basic(optional = false)
    @Column(name = "due_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;  

    @Basic(optional = false)
    @Column(name = "possible")
    private Float possible;

    @Basic(optional = false)
    @Column(name = "visible_ind")
    private String visibleInd;     
    
    public Integer getCrsmainPk1() {
        return crsmainPk1;
    }

    public void setCrsmainPk1(Integer crsmainPk1) {
        this.crsmainPk1 = crsmainPk1;
    }

    public String getVisibleInd() {
        return visibleInd;
    }

    public void setVisibleInd(String visibleInd) {
        this.visibleInd = visibleInd;
    }

    
    
    public Integer getPk1() {
        return pk1;
    }

    public void setPk1(Integer pk1) {
        this.pk1 = pk1;
    }

    public Integer getCourseContentsPk1() {
        return courseContentsPk1;
    }

    public void setCourseContentsPk1(Integer courseContentsPk1) {
        this.courseContentsPk1 = courseContentsPk1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Float getPossible() {
        return possible;
    }

    public void setPossible(Float possible) {
        this.possible = possible;
    }

    @Override
    public String toString() {
        return "GradebookMain{" + "crsmainPk1=" + crsmainPk1 + ", pk1=" + pk1 + ", courseContentsPk1=" + courseContentsPk1 + ", title=" + title + ", dueDate=" + dueDate + ", possible=" + possible + ", visibleInd=" + visibleInd + '}';
    }
    
    
    
    
}
