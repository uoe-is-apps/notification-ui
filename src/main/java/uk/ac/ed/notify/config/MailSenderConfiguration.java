package uk.ac.ed.notify.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailSenderConfiguration {

    @Value("${uk.ac.ed.notify.email.host:localhost}")
    private String emailHost;

    @Value("${uk.ac.ed.notify.email.port:25}")
    private int emailPort;

    @Value("${uk.ac.ed.notify.email.protocol:smtp}")
    private String emailProtocol;

    @Bean
    public JavaMailSender javaMailSender() {
        final JavaMailSenderImpl rslt = new JavaMailSenderImpl();
        rslt.setHost(emailHost);
        rslt.setPort(emailPort);
        rslt.setProtocol(emailProtocol);
        return rslt;
    }

}
