package uk.ac.ed.notify.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.ed.notify.entity.SubscriberDetails;
import uk.ac.ed.notify.repository.SubscriberDetailsRepository;

@RestController
public class SubscriptionDetailsController {
	
	@Autowired
	SubscriberDetailsRepository subscriberDetailsRepository;
	
	@RequestMapping(value="/topic/{topic}",method = RequestMethod.GET)
	public List<SubscriberDetails> getSubscriptionsByTopic(@PathVariable("topic") String topic){
		
		return subscriberDetailsRepository.findByTopicSubscription(topic);
	}
}
