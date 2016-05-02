/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.learn.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.ed.notify.learn.entity.Users;

/**
 *
 * @author hsun1
 */
public interface LearnUserRepository extends JpaRepository<Users, Long>{
    
    List<Users> findByPk1(Integer uun);
    
    List<Users> findAllActiveUsers();
    
    
}