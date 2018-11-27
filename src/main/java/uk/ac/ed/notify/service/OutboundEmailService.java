package uk.ac.ed.notify.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uk.ac.ed.notify.channel.DeliveryAddress;
import uk.ac.ed.notify.entity.Notification;

import javax.mail.internet.MimeMessage;

@Service
public class OutboundEmailService {

    @Value("${uk.ac.ed.notify.email.fromAddress:noreply@apereo.org}")
    private String fromAddress;

    @Autowired
    private JavaMailSender mailSender;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Async
    public void send(DeliveryAddress deliveryAddress, Notification notification) {

        try {
            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.addTo(deliveryAddress.getValue());
            helper.setFrom(fromAddress);
            helper.setSubject(notification.getTitle());
            helper.setText(notification.getBody());

            logger.debug("Sending notification '{}' by email to username '{}' at emailAddress='{}'",
                    notification.getNotificationId(), deliveryAddress.getUsername(), deliveryAddress.getValue());

            mailSender.send(helper.getMimeMessage());
        } catch (Exception e) {
            logger.error("Failed to send verification email to the following deliveryAddress:  {}", deliveryAddress, e);
        }

    }

}
