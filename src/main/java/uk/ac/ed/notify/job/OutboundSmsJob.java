package uk.ac.ed.notify.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ed.notify.channel.DeliveryAddress;
import uk.ac.ed.notify.channel.DeliveryChannel;
import uk.ac.ed.notify.channel.DeliveryChannelRecipientStrategy;
import uk.ac.ed.notify.entity.AuditActions;
import uk.ac.ed.notify.entity.Notification;
import uk.ac.ed.notify.entity.UserNotificationAudit;
import uk.ac.ed.notify.repository.NotificationRepository;
import uk.ac.ed.notify.repository.UserNotificationAuditRepository;
import uk.ac.ed.notify.service.OutboundSmsService;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DisallowConcurrentExecution
public class OutboundSmsJob implements Job {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserNotificationAuditRepository auditRepository;

    @Autowired(required = false)
    private DeliveryChannelRecipientStrategy deliveryChannelRecipientStrategy;

    @Autowired
    private OutboundSmsService smsService;

    @Autowired
    private UserNotificationAuditRepository userNotificationAuditRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        // Find notifications that need outbound sms processing
        final List<Notification> activeNotifications = notificationRepository.findActiveNotifications();
        final Set<Notification> unsent = new HashSet<>();
        activeNotifications.forEach(notification -> {
            final List<UserNotificationAudit> audit =
                    auditRepository.findByNotificationIdAndAction(notification.getNotificationId(), AuditActions.TEXT_NOTIFICATION);
            // An empty list means the notification needs processing...
            if (audit.isEmpty()) {
                unsent.add(notification);
            }
        });

        logger.trace("Found the following active, unsent notifications:  {}", unsent);

        unsent.forEach(notification -> sendOutboundTexts(notification));

    }

    private void sendOutboundTexts(Notification notification) {

        if (deliveryChannelRecipientStrategy == null) {
            logger.warn("Application attempted to send outbound texts, but the communicationPreferencesService bean is null");
            return;
        }

        logger.debug("Sending outbound texts for the following notification:  {}", notification);

        final Set<DeliveryAddress> numbers =
                deliveryChannelRecipientStrategy.getAddressesForNotificationAndChannel(notification, DeliveryChannel.SMS);
        logger.debug("Found the following phone numbers for notification '{}':  {}", notification.getNotificationId(), numbers);

        numbers.forEach(address -> smsService.send(address, notification));

        try {
            final UserNotificationAudit userNotificationAudit = new UserNotificationAudit();
            userNotificationAudit.setAction(AuditActions.TEXT_NOTIFICATION);
            userNotificationAudit.setAuditDate(new Date());
            userNotificationAudit.setPublisherId(notification.getPublisherId());
            userNotificationAudit.setNotificationId(notification.getNotificationId());
            userNotificationAudit.setAuditDescription(new ObjectMapper().writeValueAsString(notification));
            userNotificationAuditRepository.save(userNotificationAudit);
        } catch (Exception e) {
            logger.error("Failed to record texting notification '{}' in the audit trail", notification.getNotificationId());
        }

    }

}
