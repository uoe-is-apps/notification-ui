package uk.ac.ed.notify.config;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import uk.ac.ed.notify.job.Office365PullJob;
import uk.ac.ed.notify.job.OutboundEmailJob;
import uk.ac.ed.notify.job.OutboundSmsJob;
import uk.ac.ed.notify.spring.AutowiringSpringBeanJobFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import uk.ac.ed.notify.job.NotificationTidyupJob;

/**
 * Created by rgood on 05/10/15.
 */
@Configuration
@ConditionalOnProperty(name = "quartz.enabled")
public class SchedulerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerConfig.class);

    @Autowired
    private Set<Trigger> triggers;

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(
            @Qualifier("notifyDataSource") DataSource dataSource,
            JobFactory jobFactory) throws IOException {

        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // this allows to update triggers in DB when updating settings in config file:
        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);

        factory.setQuartzProperties(quartzProperties());
        factory.setTriggers(triggers.stream().toArray(Trigger[]::new));
        return factory;

    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    @Bean
    public JobDetailFactoryBean notificationTidyupJobDetail() {
        return createJobDetail(NotificationTidyupJob.class);

    }

    @Bean(name = "notificationTidyupJobTrigger")
    public SimpleTriggerFactoryBean notificationTidyupJobTrigger(@Qualifier("notificationTidyupJobDetail") JobDetail jobDetail,
                                                     @Value("${notificationTidyupJob.frequency}") long frequency) {
        return createTrigger(jobDetail, frequency);
    }    
    
    @Bean
    public JobDetailFactoryBean office365PullJobDetail() {
        return createJobDetail(Office365PullJob.class);

    }

    @Bean(name = "office365PullJobTrigger")
    public SimpleTriggerFactoryBean office365PullJobTrigger(@Qualifier("office365PullJobDetail") JobDetail jobDetail,
                                                        @Value("${office365PullJob.frequency}") long frequency) {
        return createTrigger(jobDetail, frequency);
    }

    @Bean
    public JobDetailFactoryBean outboundEmailJobDetail() {
        return createJobDetail(OutboundEmailJob.class);

    }

    @Bean(name = "outboundEmailJobTrigger")
    public SimpleTriggerFactoryBean outboundEmailJobTrigger(@Qualifier("outboundEmailJobDetail") JobDetail jobDetail,
                                                                 @Value("${outboundEmailJobDetail.frequency}") long frequency) {
        return createTrigger(jobDetail, frequency);
    }

    @Bean
    public JobDetailFactoryBean outboundSmsJobDetail() {
        return createJobDetail(OutboundSmsJob.class);

    }

    @Bean(name = "outboundSmsJobTrigger")
    public SimpleTriggerFactoryBean outboundSmsJobTrigger(@Qualifier("outboundSmsJobDetail") JobDetail jobDetail,
                                                            @Value("${outboundSmsJobDetail.frequency}") long frequency) {
        return createTrigger(jobDetail, frequency);
    }

    private static JobDetailFactoryBean createJobDetail(Class jobClass) {
        LOGGER.info("Creating JobDetailFactoryBean based on class '{}'", jobClass);
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        // job has to be durable to be stored in DB:
        factoryBean.setDurability(true);
        return factoryBean;
    }

    private static SimpleTriggerFactoryBean createTrigger(JobDetail jobDetail, long pollFrequencyMs) {
        LOGGER.info("Creating SimpleTriggerFactoryBean based for JobDetail '{}' with frequency '{}'", jobDetail, pollFrequencyMs);
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(0L);
        factoryBean.setRepeatInterval(pollFrequencyMs);
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        // in case of misfire, ignore all missed triggers and continue :
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
        return factoryBean;
    }
}
