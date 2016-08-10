/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import uk.ac.ed.notify.repository.NotificationRepositoryImpl;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by rgood on 23/10/2015.
 */
@EnableAutoConfiguration
@EntityScan({"uk.ac.ed.notify.entity","uk.ac.ed.notify.learn.entity"})
@EnableJpaRepositories({"uk.ac.ed.notify.repository","uk.ac.ed.notify.learn.repository"})
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

}

