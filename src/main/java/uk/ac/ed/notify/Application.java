package uk.ac.ed.notify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;
import org.springframework.cloud.security.oauth2.sso.EnableOAuth2Sso;
import org.springframework.cloud.security.oauth2.sso.OAuth2SsoConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uk.ac.ed.notify.config.RemoteUserAuthenticationFilter;
import uk.ac.ed.notify.repository.UiUserRepository;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by rgood on 18/09/2015.
 */
@SpringBootApplication
@EntityScan("uk.ac.ed.notify")
@ComponentScan({"uk.ac.ed.notify"})
@EnableOAuth2Resource
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Configuration
    protected static class LoginConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        UiUserRepository uiUserRepository;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.addFilterBefore(remoteUserAuthenticationFilter(), RequestHeaderAuthenticationFilter.class)
                    .authenticationProvider(
                            preauthAuthProvider())
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/office365NewEmailCallback/**","/office365NewSubscriptionCallback/**","/office365RenewSubscriptionCallback/**").permitAll()
                    .anyRequest().authenticated()
            .antMatchers("/").hasRole("EMERGENCY");
        }
        
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(preauthAuthProvider());
        }

        @Bean
        public UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper() {
            UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper =
                    new UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken>();
            wrapper.setUserDetailsService(new UiUserDetailsService(uiUserRepository));
            return wrapper;
        }

        @Bean
        public PreAuthenticatedAuthenticationProvider preauthAuthProvider() {
            PreAuthenticatedAuthenticationProvider preauthAuthProvider =
                    new PreAuthenticatedAuthenticationProvider();
            preauthAuthProvider.setPreAuthenticatedUserDetailsService(userDetailsServiceWrapper());
            return preauthAuthProvider;
        }

        @Bean
        public RemoteUserAuthenticationFilter remoteUserAuthenticationFilter() throws Exception {
            RemoteUserAuthenticationFilter filter = new RemoteUserAuthenticationFilter();
            filter.setAuthenticationManager(authenticationManager());
            return filter;
        }


        @Autowired
        private AuthenticationManager authenticationManager;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.parentAuthenticationManager(authenticationManager);
        }
    }

}
