/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.learn.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.ed.notify.learn.entity.GradebookMain;

/**
 *
 * @author hsun1
 */

//DTI020-109 Remove Learn Integration
public interface LearnAssessmentRepository{}

/*
public interface LearnAssessmentRepository extends JpaRepository<GradebookMain, Long>{
     List<GradebookMain> findGradebookMain();      
}
*/