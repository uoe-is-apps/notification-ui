package uk.ac.ed.notify.repository;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ed.notify.entity.UiUserRole;

import java.util.List;

/**
 * Created by rgood on 24/10/15.
 */

public interface UiUserRoleRepository extends CrudRepository<UiUserRole,String> {

    public List<UiUserRole> findByUun(String uun);
}
