package uk.ac.ed.notify.controller;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.ed.notify.entity.PublisherDetails;
import uk.ac.ed.notify.entity.QuartzTrigger;
import uk.ac.ed.notify.entity.SubscriberDetails;
import uk.ac.ed.notify.entity.TopicDetails;
import uk.ac.ed.notify.entity.TopicSubscription;
import uk.ac.ed.notify.repository.PublisherDetailsRepository;
import uk.ac.ed.notify.repository.QuartzTriggerRepository;
import uk.ac.ed.notify.repository.SubscriberDetailsRepository;
import uk.ac.ed.notify.repository.TopicDetailsRepository;
import uk.ac.ed.notify.repository.TopicSubscriptionRepository;

@RestController
public class SupportController {

        protected final Log logger = LogFactory.getLog(this.getClass());
    
	@Autowired
	public QuartzTriggerRepository quartzTriggerRepository;
	
	@Autowired
	public PublisherDetailsRepository publisherDetailsRepository;
	
	@Autowired
	public TopicDetailsRepository topicDetailsRepository;	                
        
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
	
	@RequestMapping(value="/topics", method = RequestMethod.GET)
	public List<TopicDetails> getTopicDetails() {		
		return topicDetailsRepository.findAll();
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
            List<TopicSubscription> list = topicSubscriptionRepository.findBySubscriberIdAndTopic(topicSubscription.getSubscriberId(), topicSubscription.getTopic());
            if(list.size() == 0){
                topicSubscriptionRepository.save(topicSubscription);
            }
	}
        
        
        @Autowired    
        private SchedulerFactoryBean schedulerFactoryBean;        
        
	@RequestMapping(value="/start-job/{id}", method = RequestMethod.POST)
        public void startJob(@PathVariable("id") String id) {
              try {
                  schedulerFactoryBean.getScheduler().resumeJob(new JobKey(id,"DEFAULT"));
                    } catch (Exception ex) {
                  logger.error("startJob - " + id + " - " + ex.toString());  
              }
              logger.info("startJob - " + id + " - " + schedulerFactoryBean.isRunning());
	}        

        
	@RequestMapping(value="/stop-job/{id}", method = RequestMethod.POST)
        public void stopJob(@PathVariable("id") String id) throws SchedulerException {
              try {
                 schedulerFactoryBean.getScheduler().pauseJob(new JobKey(id,"DEFAULT"));
              } catch (Exception ex) {
                  logger.error("stopJob - " + id + " - " + ex.toString());  
              }                            
              logger.info("stopJob - " + id + " - " + schedulerFactoryBean.isRunning());  
        }                    
                   
                
	@RequestMapping(value="/reschedule-job/{id}/{interval}", method = RequestMethod.POST)
        public void rescheduleJob(@PathVariable("id") String id, @PathVariable("interval") String newInterval) {
              logger.info("rescheduleJob - " + id + " - " + newInterval);  
            
              int interval = -1;              
              try{
                interval = Integer.parseInt(newInterval);
              }catch(Exception e){
                logger.info("invalid interval, ignore reschedule");  
                return;
              }
              
              
              try {
                List<? extends Trigger> triggerList =
                  schedulerFactoryBean.getScheduler().getTriggersOfJob(new JobKey(id,"DEFAULT"));

                 logger.info("found group   name - " + schedulerFactoryBean.getScheduler().getJobGroupNames() );
                 logger.info("found trigger name - " + schedulerFactoryBean.getScheduler().getTriggerGroupNames());

                 SimpleTrigger trigger = TriggerBuilder.newTrigger().withIdentity(id, "DEFAULT")
                  .startAt(new Date(Calendar.getInstance().getTimeInMillis()+ 1000))
                          .withSchedule(SimpleScheduleBuilder.simpleSchedule()                       
                              .withIntervalInSeconds( interval )
                              .withRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY)                       
                         ).build(); 
                 
                  schedulerFactoryBean.getScheduler().rescheduleJob(triggerList.get(0).getKey(), trigger);
                  logger.info("success");  
              } catch (Exception ex) {
                  logger.error("rescheduleJob - " + id + " - " + newInterval + " - " + ex.toString());  
              }
	} 	      
}
