package uk.ac.ed.notify.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import uk.ac.ed.notify.channel.DeliveryAddress;
import uk.ac.ed.notify.entity.Notification;

import javax.annotation.PostConstruct;

@Service
@ConditionalOnProperty("uk.ac.ed.notify.sms.twillio.accountSid")
public class TwillioOutboundSmsService implements OutboundSmsService {

    private static final String FROM_NUMBER_PROPERTY_PREFIX = "uk.ac.ed.notify.sms.twillio";
    private static final String FROM_NUMBER_PROPERTY_SUFFIX = "fromNumber";

    @Value("${uk.ac.ed.notify.sms.twillio.accountSid:}")
    private String accountSid;

    @Value("${uk.ac.ed.notify.sms.twillio.authToken:}")
    private String authToken;

    @Autowired
    private Environment environment;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void init() {

        if (isFullyConfigured()) {

            logger.info("Initializing the Twillio API because account credentials were " +
                            "provided in the configuration;  the '{}' bean will be ENABLED",
                    getClass().getSimpleName());
            Twilio.init(accountSid, authToken);

        } else {
            logger.info("Ignoring the Twillio API because account credentials WERE NOT " +
                            "provided in the configuration;  the '{}' bean will be DISABLED",
                    getClass().getSimpleName());
        }

    }

    @Override
    public void send(DeliveryAddress deliveryAddress, Notification notification) {

        if (!isFullyConfigured()) {
            logger.error("Improper attempt to use the {} to send notification '{}' where configuration is missing",
                    getClass().getSimpleName(), notification.getNotificationId());
            return;
        }

        final String fromNumber = selectFromNumber(notification);
        logger.debug("Using fromNumber='{}' to send notification='{}' to username='{}'",
                fromNumber, notification.getNotificationId(), deliveryAddress.getUsername());
        if (fromNumber == null) {
            throw new IllegalStateException(
                    "Could not evaluate a fromNumber for notification:  " + notification.getNotificationId());
        }

        final String toNumber = US_DIALING_PREFIX + normalizePhoneNumber(deliveryAddress.getValue());
        final Message message = Message.creator(
                new PhoneNumber(toNumber),
                new PhoneNumber(US_DIALING_PREFIX + normalizePhoneNumber(fromNumber)),
                notification.getBody()
        ).create();

        /*
         * TODO:  Enhance!
         *
         * I'm confident there's more we will want to do with this switch statement, once
         * we understand the possible statuses, what they mean, and how we should deal with them...
         */
        switch (message.getStatus()) {
            case FAILED:
                logger.warn("FAILED to send SMS message '{}' to phoneNumber '{}' with sid='{}';  received status='{}'",
                        notification.getBody(), deliveryAddress.getValue(), message.getSid(), message.getStatus());
                break;
            default:
                logger.debug("Sent SMS message '{}' to phoneNumber '{}' with sid='{}';  received status='{}'",
                        notification.getBody(), deliveryAddress.getValue(), message.getSid(), message.getStatus());
                break;
        }

    }

    private boolean isFullyConfigured() {
        return StringUtils.isNotBlank(accountSid)
                && StringUtils.isNotBlank(authToken);
    }

    private String selectFromNumber(Notification notification) {

        String rslt; // default

        /*
         * First try the group-specific fromNumber
         */

        final String groupPropertyName =
                FROM_NUMBER_PROPERTY_PREFIX + "." + formatGroupPart(notification) + "." + FROM_NUMBER_PROPERTY_SUFFIX;
        rslt = environment.getProperty(groupPropertyName);

        /*
         * If we don't have a specific fromNumber for the group, use the default/general number.
         */

        if (rslt == null) {
            rslt = environment.getProperty(FROM_NUMBER_PROPERTY_PREFIX + "." + FROM_NUMBER_PROPERTY_SUFFIX);
        }

        return rslt;

    }

    private String formatGroupPart(Notification notification) {
        return notification.getNotificationGroupName().replaceAll("\\W", "");
    }

}
