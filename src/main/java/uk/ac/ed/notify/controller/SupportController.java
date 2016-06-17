package uk.ac.ed.notify.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.ed.notify.entity.PublisherDetails;
import uk.ac.ed.notify.entity.QuartzTrigger;
import uk.ac.ed.notify.entity.SubscriberDetails;
import uk.ac.ed.notify.entity.TopicSubscription;
import uk.ac.ed.notify.repository.PublisherDetailsRepository;
import uk.ac.ed.notify.repository.QuartzTriggerRepository;
import uk.ac.ed.notify.repository.SubscriberDetailsRepository;
import uk.ac.ed.notify.repository.TopicSubscriptionRepository;

@RestController
public class SupportController {
	
	@Autowired
	public QuartzTriggerRepository quartzTriggerRepository;
	
	@Autowired
	public PublisherDetailsRepository publisherDetailsRepository;
	
	@Autowired
	public SubscriberDetailsRepository subscriberDetailsRepository;
	
	@Autowired
	public TopicSubscriptionRepository topicSubscriptionRepository;
	
	@RequestMapping(value="/scheduled-tasks", method = RequestMethod.GET)
	public List<QuartzTrigger> listScheduledTasks(){
		
		return quartzTriggerRepository.findAll();
	}
	
	@RequestMapping(value="/publishers", method = RequestMethod.GET)
	public List<PublisherDetails> getPublisherDetails() {
		
		return publisherDetailsRepository.findAll();
	}
	
	@RequestMapping(value="/subscribers", method = RequestMethod.GET)
	public List<SubscriberDetails> getSubscriberDetails() {
		
		return subscriberDetailsRepository.findAll();
	}
	
	@RequestMapping(value="/topic-subscriptions/{subscription-id}", method = RequestMethod.DELETE)
	public void deleteTopicSubscription(@PathVariable("subscription-id") String id){
		
		topicSubscriptionRepository.delete(id);
	}
	
	@RequestMapping(value="/topic-subscriptions", method = RequestMethod.GET)
	public List<TopicSubscription> getTopicSubscriptions() {
		
		return topicSubscriptionRepository.findAll();
	}
	
	@RequestMapping(value="/topic-subscriptions", method = RequestMethod.POST)
    public void saveTopicSubscription(@RequestBody TopicSubscription topicSubscription) {
		
		topicSubscriptionRepository.save(topicSubscription);
	}
	
}
