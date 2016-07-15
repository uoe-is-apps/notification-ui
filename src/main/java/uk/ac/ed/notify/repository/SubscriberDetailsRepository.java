package uk.ac.ed.notify.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import uk.ac.ed.notify.entity.SubscriberDetails;

@RepositoryRestResource(exported = false)
public interface SubscriberDetailsRepository extends CrudRepository<SubscriberDetails, String>{
	
	List<SubscriberDetails> findAll();
	
	@Query("select sd from SubscriberDetails sd where exists (select ts from TopicSubscription ts where ts.subscriberId = sd.subscriberId and ts.topic = (?1))")
	List<SubscriberDetails> findByTopicSubscription(String topic);
}
