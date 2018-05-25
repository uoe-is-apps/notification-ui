/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.repository;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ed.notify.entity.Office365Subscription;

/**
 *
 * @author hsun1
 */
public interface Office365Repository extends CrudRepository<Office365Subscription,String>{
}
