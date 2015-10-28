package uk.ac.ed.notify.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ed.notify.entity.UiRole;

/**
 * Created by rgood on 24/10/15.
 */
@Repository
public interface UiRoleRepository extends CrudRepository<UiRole,String>{
}
