package uk.ac.ed.notify.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import uk.ac.ed.notify.channel.DeliveryAddress;
import uk.ac.ed.notify.entity.Notification;

import javax.annotation.PostConstruct;

@Service
@ConditionalOnProperty("uk.ac.ed.notify.sms.twillio.accountSid")
public class TwillioOutboundSmsService implements OutboundSmsService {

    @Value("${uk.ac.ed.notify.sms.twillio.accountSid:}")
    private String accountSid;

    @Value("${uk.ac.ed.notify.sms.twillio.authToken:}")
    private String authToken;

    @Value("${uk.ac.ed.notify.sms.twillio.fromNumber:}")
    private String fromNumber;

    private String normalizedFromNumber;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void init() {

        if (isFullyConfigured()) {

            normalizedFromNumber = normalizePhoneNumber(fromNumber);

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

        final String toNumber = US_DIALING_PREFIX + normalizePhoneNumber(deliveryAddress.getValue());
        final Message message = Message.creator(
                new PhoneNumber(toNumber),
                new PhoneNumber(US_DIALING_PREFIX + normalizedFromNumber),
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
                && StringUtils.isNotBlank(authToken)
                && StringUtils.isNotBlank(fromNumber);
    }

}
