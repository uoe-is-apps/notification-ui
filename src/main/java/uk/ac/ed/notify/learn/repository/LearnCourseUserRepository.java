/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.learn.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.ed.notify.learn.entity.CourseUsers;

/**
 *
 * @author hsun1
 */
public interface LearnCourseUserRepository extends JpaRepository<CourseUsers, Long>{
    
       List<CourseUsers> findByCrsmainPk1(Integer pk1);
    
}
