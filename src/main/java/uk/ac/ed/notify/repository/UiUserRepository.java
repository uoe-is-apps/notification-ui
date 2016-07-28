package uk.ac.ed.notify.repository;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ed.notify.entity.UiUser;

/**
 * Created by rgood on 24/10/15.
 */
public interface UiUserRepository extends CrudRepository<UiUser,String>{
}
