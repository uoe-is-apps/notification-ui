/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.timetabling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.ed.notify.timetabling.entity.LessonCourseGroup;

/**
 *
 * @author hsun1
 */
public interface LessonCourseGroupRepository extends JpaRepository<LessonCourseGroup, Long>{
    
}
