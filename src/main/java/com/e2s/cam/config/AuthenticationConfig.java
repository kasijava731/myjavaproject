package com.e2s.cam.config;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "authenticationEntityManagerFactory", transactionManagerRef = "authenticationTransactionManager", basePackages = {
		"com.e2s.cam.usersRepository" })
public class AuthenticationConfig {
	@Bean(name = "authenticationDataSource")
	@ConfigurationProperties(prefix = "authentication.datasource")
	public DataSource UserDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "authenticationEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("authenticationDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.e2s.cam.userentity").persistenceUnit("authentication")
				.build();
	}

	@Bean(name = "authenticationTransactionManager")
	public PlatformTransactionManager userTransactionManager(
			@Qualifier("authenticationEntityManagerFactory") EntityManagerFactory authenticationEntityManagerFactory) {
		return new JpaTransactionManager(authenticationEntityManagerFactory);
	}
}