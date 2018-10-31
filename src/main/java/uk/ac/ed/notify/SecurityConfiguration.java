package uk.ac.ed.notify;

import org.apache.commons.lang3.StringUtils;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import uk.ac.ed.notify.config.RemoteUserAuthenticationFilter;
import uk.ac.ed.notify.repository.UiUserRepository;
import uk.ac.ed.notify.service.UiUserDetailsService;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${hub.protocol}")
    private String hubProtocol;

    @Value("${hub.server}")
    private String hubServer;

    @Value("${hub.context:}")
    private String hubContext;

    @Value("${hub.casServiceUri:/login/cas}") // Endpoint in this app
    private String hubCasServiceUri;

    @Value("${cas.protocol:http}")
    private String casProtocol;

    @Value("${cas.server:}")
    private String casServer;

    @Value("${cas.context:/cas}")
    private String casContext;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UiUserRepository uiUserRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        logger.info("Configuring HttpSecurity");

        http = http
                .addFilterBefore(remoteUserAuthenticationFilter(), RequestHeaderAuthenticationFilter.class);

        // Optional casAuthenticationFilter
        logger.info("casServer = {}", casServer);
        if (StringUtils.isNotBlank(casServer)) {
            logger.info("Enabling optional CAS support");
            http = http
                    .httpBasic()
                    .authenticationEntryPoint(casAuthenticationEntryPoint())
                    .and()
                    .addFilterBefore(casAuthenticationFilter(), RequestHeaderAuthenticationFilter.class);
        }

        http
                .csrf().disable()
                .authorizeRequests().anyRequest().authenticated()
                .antMatchers("/office365NewEmailCallback/**").permitAll()
                .antMatchers("/healthcheck/**").permitAll()
                .antMatchers("/scheduled-tasks", "/publishers", "/subscribers", "/topic-subscriptions/**").hasRole("SYSSUPPORT")
                .antMatchers("/topic/**").hasRole("USRSUPPORT")
                .antMatchers("/").hasRole("GROUP");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        if (StringUtils.isNotBlank(casServer)) {
            auth.authenticationProvider(casAuthenticationProvider());
        }
        auth.authenticationProvider(preauthAuthProvider())
                .parentAuthenticationManager(authenticationManager);
    }

    @Override
    protected AuthenticationManager authenticationManager() {
        final List<AuthenticationProvider> providers = new ArrayList<>();
        if (StringUtils.isNotBlank(casServer)) {
            providers.add(casAuthenticationProvider());
        }
        providers.add(preauthAuthProvider());
        return new ProviderManager(providers);
    }

    @Bean
    public UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper() {
        UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper =
                new UserDetailsByNameServiceWrapper<>();
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
    public RemoteUserAuthenticationFilter remoteUserAuthenticationFilter() {
        RemoteUserAuthenticationFilter filter = new RemoteUserAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    /*
     * Optional CAS Authentication
     *
     * See https://www.baeldung.com/spring-security-cas-sso
     */

    private String casLoginUrl() {
        return casProtocol + "://" + casServer + casContext + "/login";
    }

    @Bean
    @ConditionalOnProperty(value="cas.server")
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(hubProtocol + "://" + hubServer + hubContext + hubCasServiceUri);
        serviceProperties.setSendRenew(false);
        return serviceProperties;
    }

    @Bean
    @Primary
    @ConditionalOnProperty(value="cas.server")
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
        CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
        entryPoint.setLoginUrl(casLoginUrl());
        entryPoint.setServiceProperties(serviceProperties());
        return entryPoint;
    }

    @Bean
    @ConditionalOnProperty(value="cas.server")
    public TicketValidator ticketValidator() {
        return new Cas20ServiceTicketValidator(casProtocol + "://" + casServer + casContext);
    }

    @Bean
    @ConditionalOnProperty(value="cas.server")
    public CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setServiceProperties(serviceProperties());
        provider.setTicketValidator(ticketValidator());
        provider.setUserDetailsService(new UiUserDetailsService(uiUserRepository));
        provider.setKey("CAS_PROVIDER_LOCALHOST_9000");
        return provider;
    }

    @Bean
    @ConditionalOnProperty(value="cas.server")
    public CasAuthenticationFilter casAuthenticationFilter() {
        CasAuthenticationFilter filter = new CasAuthenticationFilter();
        filter.setServiceProperties(serviceProperties());
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(hubCasServiceUri));
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

}
