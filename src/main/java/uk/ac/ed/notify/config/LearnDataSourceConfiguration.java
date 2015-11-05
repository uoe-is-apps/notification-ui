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
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;


@Configuration
@EnableJpaRepositories(basePackages = "uk.ac.ed.notify.learn.repository", entityManagerFactoryRef = "learnEntityManagerFactory", transactionManagerRef = "learnTransactionManager")
@EnableTransactionManagement
public class LearnDataSourceConfiguration
{
  @Bean
  @ConfigurationProperties(prefix = "datasource.learn")
  @Primary
  public DataSource learnDataSource()
  {
    return DataSourceBuilder.create().build();
  }

  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean learnEntityManagerFactory(final EntityManagerFactoryBuilder builder)
  {
    return builder
        .dataSource(learnDataSource())
        .packages("uk.ac.ed.notify.learn.entity")
        .persistenceUnit("learnPersistenceUnit")
        .build();
  }

  @Bean
  @Primary
  public JpaTransactionManager learnTransactionManager(@Qualifier("learnEntityManagerFactory") final EntityManagerFactory factory)
  {
    return new JpaTransactionManager(factory);
  }
}
