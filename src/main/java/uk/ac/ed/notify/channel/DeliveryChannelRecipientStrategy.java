package uk.ac.ed.notify.channel;

import uk.ac.ed.notify.entity.Notification;
import uk.ac.ed.notify.job.OutboundEmailJob;

import java.util.Set;

/**
 * Adopters must provide a bean of this type to use either the {@link OutboundEmailJob} or the @link OutboundSmsJob.
 */
public interface DeliveryChannelRecipientStrategy {

    String EMERGENCY_TOPIC_NAME = "Emergency";

    Set<DeliveryAddress> getAddressesForNotificationAndChannel(Notification notification, DeliveryChannel channel);

}
