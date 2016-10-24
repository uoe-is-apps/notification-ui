/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.timetabling.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.ed.notify.timetabling.entity.StudentCourseGroup;

/**
 *
 * @author hsun1
 */
public interface StudentCourseGroupRepository extends JpaRepository<StudentCourseGroup, Long>{
    List<StudentCourseGroup> findStudentsByLessonTimetableId(String lessonTimetableId);
}
