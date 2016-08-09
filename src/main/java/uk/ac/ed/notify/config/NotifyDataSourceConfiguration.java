/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ed.notify.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import uk.ac.ed.notify.repository.NotificationRepositoryImpl;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "uk.ac.ed.notify.repository", entityManagerFactoryRef = "notifyEntityManagerFactory", transactionManagerRef = "notifyTransactionManager")
@EnableTransactionManagement
public class NotifyDataSourceConfiguration
{
  @Bean(name = "notifyDataSource")
  @ConfigurationProperties(prefix = "datasource.notify")
  public DataSource notifyDataSource()
  {
    return DataSourceBuilder.create().build();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean notifyEntityManagerFactory(final EntityManagerFactoryBuilder builder)
  {
    return builder
        .dataSource(notifyDataSource())
        .packages("uk.ac.ed.notify.entity")
        .persistenceUnit("notifyPersistenceUnit")
        .build();
  }

  @Bean
  public JpaTransactionManager notifyTransactionManager(@Qualifier("notifyEntityManagerFactory") final EntityManagerFactory factory)
  {
    return new JpaTransactionManager(factory);
  }
  
  @Bean(name = "notificationRepositoryImpl")
  public NotificationRepositoryImpl notificationRepositoryImpl() {
	  
	  return new NotificationRepositoryImpl();
  }
}
