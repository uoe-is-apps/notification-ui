package uk.ac.ed.notify.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import uk.ac.ed.notify.entity.QuartzTrigger;

public interface QuartzTriggerRepository extends CrudRepository<QuartzTrigger,String>{

	List<QuartzTrigger> findAll();
}
