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

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;


@Configuration
@EnableJpaRepositories(basePackages = "uk.ac.ed.notify.timetabling.repository", entityManagerFactoryRef = "timetablingEntityManagerFactory", transactionManagerRef = "timetablingTransactionManager")
@EnableTransactionManagement
public class TimetablingDataSourceConfiguration
{
  @Bean
  @ConfigurationProperties(prefix = "datasource.timetabling")
  public DataSource timetablingDataSource()
  {
    return DataSourceBuilder.create().build();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean timetablingEntityManagerFactory(final EntityManagerFactoryBuilder builder)
  {
    return builder
        .dataSource(timetablingDataSource())
        .packages("uk.ac.ed.notify.timetabling.entity")
        .persistenceUnit("timetablingPersistenceUnit")
        .build();
  }

  @Bean
  public JpaTransactionManager timetablingTransactionManager(@Qualifier("timetablingEntityManagerFactory") final EntityManagerFactory factory)
  {
    return new JpaTransactionManager(factory);
  }
}
