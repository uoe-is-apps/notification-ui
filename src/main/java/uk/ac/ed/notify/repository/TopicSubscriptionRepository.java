package uk.ac.ed.notify.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import uk.ac.ed.notify.entity.TopicSubscription;

public interface TopicSubscriptionRepository extends CrudRepository<TopicSubscription, String> {
	
	List<TopicSubscription> findAll();
        List<TopicSubscription> findBySubscriberIdAndTopic(String subscriberId, String topic);

}
