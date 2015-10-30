package uk.ac.ed.notify.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ed.notify.entity.UiUserRole;

import java.util.List;

/**
 * Created by rgood on 24/10/15.
 */
@Repository
public interface UiUserRoleRepository extends CrudRepository<UiUserRole,String> {

    public List<UiUserRole> findByUun(String uun);
}
