/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.timetabling.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author hsun1
 */

@Entity
@Table(name = "CS_StudentCourseGroup") //, schema="dbo"
@NamedQueries({
    @NamedQuery(name = "StudentCourseGroup.findAllLessonCourseGroup", query = "SELECT v FROM LessonCourseGroup v "), //WHERE v.availableInd = 'Y' AND v.rowStatus = '0'
    @NamedQuery(name = "StudentCourseGroup.findLessonCourseGroupByTimetableid", query = "SELECT v FROM LessonCourseGroup v WHERE v.timetableId = (?1)")      
    ,
    @NamedQuery(name = "StudentCourseGroup.findStudentsByLessonTimetableId", 
     query = 

        
     //"select lession2cg.timetableid,  lession2cg.coursegroup,  student2cg.timetableId from " + 

     "select student2cg from " + 
     "LessonCourseGroup lession2cg, " + 
     "StudentCourseGroup student2cg " + 

     "where " + 
     "lession2cg.courseGroup=student2cg.courseGroup " + 
     "and " + 
     "lession2cg.timetableId = (?1) "
   
        
     /*   
     "SELECT v from Users u, CourseUsers cu, CourseMain m, " +    
     "Tasks v " +     
     "where " +     
     "u.pk1 = cu.usersPk1 and m.pk1 = cu.crsmainPk1 and (m.startDate <= sysdate and sysdate <= m.endDate or m.endDate is null) " +      
     "and v.crsmainPk1=m.pk1 " +     
       
     "AND u.availableInd = 'Y' AND u.rowStatus = '0' " + 
     "AND cu.rowStatus = '0' " +   
     "AND ((m.availableInd = 'Y' and m.endDate is null) or (m.availableInd = 'Y' and sysdate <= m.endDate))  " +  

     "order by m.dtcreated desc "
     */

     )   
        
        
        
})
public class StudentCourseGroup  implements Serializable{
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Column(name = "timetableId")
    @Id
    private String timetableId;  
            
    @Basic(optional = false)
    @Column(name = "courseGroup")
    private String courseGroup;  

    public String getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(String timetableId) {
        this.timetableId = timetableId;
    }

    public String getCourseGroup() {
        return courseGroup;
    }

    public void setCourseGroup(String courseGroup) {
        this.courseGroup = courseGroup;
    }
}
