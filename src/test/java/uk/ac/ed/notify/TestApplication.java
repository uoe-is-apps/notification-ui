/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import uk.ac.ed.notify.repository.NotificationRepositoryImpl;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
/**
 * Created by rgood on 23/10/2015.
 */
@EnableAutoConfiguration

@EntityScan({"uk.ac.ed.notify.entity","uk.ac.ed.notify.timetabling.entity","uk.ac.ed.notify.idm.entity"})

@EnableJpaRepositories(
        {"uk.ac.ed.notify.repository"})

@ComponentScan("uk.ac.ed.notify.service")
public class TestApplication {

    @Autowired
    DataSource dataSource;

    @Bean(name = "notificationRepositoryImpl")
    public NotificationRepositoryImpl notificationRepositoryImpl() {

        return new NotificationRepositoryImpl();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(final EntityManagerFactoryBuilder builder)
    {
        Map jpaProperties = new HashMap();
        jpaProperties.put("hibernate.hbm2ddl.auto","create");
        return builder
                .dataSource(dataSource)
                .packages("uk.ac.ed.notify.entity")
                .persistenceUnit("notifyPersistenceUnit")
                .properties(jpaProperties)
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
   
    @Value("${java.naming.ldap.derefAliases}")
    String derefAliases;
    @Bean
    @ConfigurationProperties(prefix="ldap.contextSource")
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        Map<String,Object> baseEnvironmentProperties = new HashMap<>();
        baseEnvironmentProperties.put("java.naming.ldap.derefAliases",derefAliases);
        contextSource.setBaseEnvironmentProperties(baseEnvironmentProperties);
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate(ContextSource contextSource) {
        return new LdapTemplate(contextSource);
    }    
}

