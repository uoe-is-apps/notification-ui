package uk.ac.ed.notify.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uk.ac.ed.notify.entity.SubscriberDetails;

@RepositoryRestResource(exported = false)
public interface SubscriberDetailsRepository extends CrudRepository<SubscriberDetails, String>{
	
	List<SubscriberDetails> findAll();

}
