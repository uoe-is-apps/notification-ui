package uk.ac.ed.notify.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ed.notify.channel.DeliveryAddress;
import uk.ac.ed.notify.channel.DeliveryChannel;
import uk.ac.ed.notify.entity.AuditActions;
import uk.ac.ed.notify.entity.Notification;
import uk.ac.ed.notify.entity.UserNotificationAudit;
import uk.ac.ed.notify.repository.NotificationRepository;
import uk.ac.ed.notify.repository.UserNotificationAuditRepository;
import uk.ac.ed.notify.channel.DeliveryChannelRecipientStrategy;
import uk.ac.ed.notify.service.OutboundEmailService;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DisallowConcurrentExecution
public class OutboundEmailJob implements Job {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserNotificationAuditRepository auditRepository;

    @Autowired(required = false)
    private DeliveryChannelRecipientStrategy deliveryChannelRecipientStrategy;

    @Autowired
    private OutboundEmailService emailService;

    @Autowired
    private UserNotificationAuditRepository userNotificationAuditRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        // Find notifications that need outbound email processing
        final List<Notification> activeNotifications = notificationRepository.findActiveNotifications();
        final Set<Notification> unsent = new HashSet<>();
        activeNotifications.forEach(notification -> {
            final List<UserNotificationAudit> audit =
                    auditRepository.findByNotificationIdAndAction(notification.getNotificationId(), AuditActions.EMAIL_NOTIFICATION);
            // An empty list means the notification needs processing...
            if (audit.isEmpty()) {
                unsent.add(notification);
            }
        });

        logger.trace("Found the following active, unsent notifications:  {}", unsent);

        unsent.forEach(notification -> sendOutboundEmails(notification));

    }

    private void sendOutboundEmails(Notification notification) {

        if (deliveryChannelRecipientStrategy == null) {
            logger.warn("Application attempted to send outbound emails, but the communicationPreferencesService bean is null");
            return;
        }

        logger.debug("Sending outbound emails for the following notification:  {}", notification);

        final Set<DeliveryAddress> emails =
                deliveryChannelRecipientStrategy.getAddressesForNotificationAndChannel(notification, DeliveryChannel.EMAIL);
        logger.debug("Found the following email addresses for notification '{}':  {}", notification.getNotificationId(), emails);

        // Does not
        emails.forEach(address -> emailService.send(address, notification));

        try {
            final UserNotificationAudit userNotificationAudit = new UserNotificationAudit();
            userNotificationAudit.setAction(AuditActions.EMAIL_NOTIFICATION);
            userNotificationAudit.setAuditDate(new Date());
            userNotificationAudit.setPublisherId(notification.getPublisherId());
            userNotificationAudit.setNotificationId(notification.getNotificationId());
            userNotificationAudit.setAuditDescription(new ObjectMapper().writeValueAsString(notification));
            userNotificationAuditRepository.save(userNotificationAudit);
        } catch (Exception e) {
            logger.error("Failed to record emailing notification '{}' in the audit trail", notification.getNotificationId());
        }

    }

}
