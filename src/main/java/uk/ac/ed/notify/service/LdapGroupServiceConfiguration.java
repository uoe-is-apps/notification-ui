package uk.ac.ed.notify.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class LdapGroupServiceConfiguration {

    @Value("${java.naming.ldap.derefAliases}")
    private String derefAliases;

    @Bean
    @ConditionalOnProperty(value="ldap.contextSource.url")
    public GroupService ldapGroupService() {
        return new LdapGroupService();
    }

    @Bean
    @ConditionalOnProperty(value="ldap.contextSource.url")
    public LdapTemplate ldapTemplate(ContextSource contextSource) {
        return new LdapTemplate(contextSource);
    }

    @Bean
    @ConfigurationProperties(prefix="ldap.contextSource")
    @ConditionalOnProperty(value="ldap.contextSource.url")
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        Map<String,Object> baseEnvironmentProperties = new HashMap<>();
        baseEnvironmentProperties.put("java.naming.ldap.derefAliases", derefAliases);
        contextSource.setBaseEnvironmentProperties(baseEnvironmentProperties);
        return contextSource;
    }

}
