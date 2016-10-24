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
@Table(name = "CS_LessonCourseGroup") //, schema="dbo"
@NamedQueries({
    @NamedQuery(name = "LessonCourseGroup.findAllLessonCourseGroup", query = "SELECT v FROM LessonCourseGroup v "), //WHERE v.availableInd = 'Y' AND v.rowStatus = '0'
    @NamedQuery(name = "LessonCourseGroup.findLessonCourseGroupByTimetableid", query = "SELECT v FROM LessonCourseGroup v WHERE v.timetableId = (?1)")      
})
public class LessonCourseGroup  implements Serializable{
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

/*
-- works 1

select lession2cg.timetableid,  lession2cg.coursegroup,  student2cg.timetableId from 

[dbo].[CS_LessonCourseGroup] lession2cg,
[dbo].[CS_StudentCourseGroup] student2cg

where 
lession2cg.coursegroup=student2cg.coursegroup 


-- works 2

select * from [dbo].[CS_Lesson] where timetableId='ARCH08004_SS1_SEM2-#SPLUS5A710F 001-10-1-9';

 
 */