package uk.ac.ed.notify;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
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
import uk.ac.ed.notify.job.LearnPullJob;
import uk.ac.ed.notify.job.Office365PullJob;
import uk.ac.ed.notify.job.Office365PushSubscriptionJob;
import uk.ac.ed.notify.spring.AutowiringSpringBeanJobFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by rgood on 05/10/15.
 */
@Configuration
@ConditionalOnProperty(name = "quartz.enabled")
public class SchedulerConfig {

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext)
    {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource,
                                                     JobFactory jobFactory,
                                                     @Qualifier("learnPullJobTrigger") Trigger learnPullJobTrigger,
                                                     @Qualifier("office365PullJobTrigger") Trigger office365PullJobTrigger,
                                                     @Qualifier("office365PushSubscriptionJobTrigger") Trigger office365PushSubscriptionJobTrigger) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // this allows to update triggers in DB when updating settings in config file:
        factory.setOverwriteExistingJobs(true);
        //factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);

        factory.setQuartzProperties(quartzProperties());
        factory.setTriggers(new Trigger[] {learnPullJobTrigger,office365PullJobTrigger,office365PushSubscriptionJobTrigger});

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
    public JobDetailFactoryBean learnPullJobDetail() {
        return createJobDetail(LearnPullJob.class);

    }

    @Bean(name = "learnPullJobTrigger")
    public SimpleTriggerFactoryBean learnPullJobTrigger(@Qualifier("learnPullJobDetail") JobDetail jobDetail,
                                                     @Value("${learnPullJob.frequency}") long frequency) {
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
    public JobDetailFactoryBean office365PushSubscriptionJobDetail() {
        return createJobDetail(Office365PushSubscriptionJob.class);

    }

    @Bean(name = "office365PushSubscriptionJobTrigger")
    public SimpleTriggerFactoryBean office365PushSubscriptionJobTrigger(@Qualifier("office365PushSubscriptionJobDetail") JobDetail jobDetail,
                                                        @Value("${office365PushSubscriptionJob.frequency}") long frequency) {
        return createTrigger(jobDetail, frequency);
    }

    private static JobDetailFactoryBean createJobDetail(Class jobClass) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        // job has to be durable to be stored in DB:
        factoryBean.setDurability(true);
        return factoryBean;
    }

    private static SimpleTriggerFactoryBean createTrigger(JobDetail jobDetail, long pollFrequencyMs) {
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
