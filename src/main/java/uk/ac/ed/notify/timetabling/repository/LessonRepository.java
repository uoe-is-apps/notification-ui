/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.timetabling.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.ed.notify.timetabling.entity.Lesson;

/**
 *
 * @author hsun1
 */
public interface LessonRepository extends JpaRepository<Lesson, Long>{
    List<Lesson> findAllLessons(Date date);
}
