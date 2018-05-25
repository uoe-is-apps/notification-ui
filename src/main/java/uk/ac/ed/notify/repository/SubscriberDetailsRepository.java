package uk.ac.ed.notify.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import uk.ac.ed.notify.entity.SubscriberDetails;

public interface SubscriberDetailsRepository extends CrudRepository<SubscriberDetails, String>{
	
	List<SubscriberDetails> findAll();
	
	@Query("select sd from SubscriberDetails sd where exists (select ts from TopicSubscription ts where ts.subscriberId = sd.subscriberId and ts.topic = (?1))")
	List<SubscriberDetails> findByTopicSubscription(String topic);
}
