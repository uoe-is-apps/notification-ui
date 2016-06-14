package uk.ac.ed.notify.repository;

import java.util.List;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.CrudRepository;

import uk.ac.ed.notify.entity.QuartzTrigger;

@RepositoryRestResource(exported = false)
public interface QuartzTriggerRepository extends CrudRepository<QuartzTrigger,String>{

	List<QuartzTrigger> findAll();
}
