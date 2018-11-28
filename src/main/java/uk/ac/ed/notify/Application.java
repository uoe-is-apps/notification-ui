package uk.ac.ed.notify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.velocity.VelocityAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created by rgood on 18/09/2015.
 */
@SpringBootApplication
@EntityScan("uk.ac.ed.notify")
@ComponentScan({"uk.ac.ed.notify"})
@EnableAsync // For sending emails & SMS
@EnableOAuth2Resource
// Prevents ClassNotFoundException: org.apache.velocity.runtime.log.CommonsLogLogChute
// https://stackoverflow.com/questions/32067759/spring-boot-starter-cache-velocity-is-missing
@EnableAutoConfiguration(exclude = VelocityAutoConfiguration.class)
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /* */
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return container -> {

            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error.html");
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error.html");
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error.html");

            container.addErrorPages(error401Page, error404Page, error500Page);
        };
    }

}
