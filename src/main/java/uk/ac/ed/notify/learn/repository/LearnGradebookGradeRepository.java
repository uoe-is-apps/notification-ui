/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.learn.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.ed.notify.learn.entity.GradebookGrade;

/**
 *
 * @author hsun1
 */
public interface LearnGradebookGradeRepository extends JpaRepository<GradebookGrade, Long>{
    
    List<GradebookGrade> findByGradebookMainPk1(Integer gradebookMainPk1);
    
}