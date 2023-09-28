//package com.e2s.cam.multitenant;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import javax.sql.DataSource;
//
//import org.hibernate.MultiTenancyStrategy;
//import org.hibernate.cfg.Environment;
//import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
//import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
//import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.context.annotation.Primary;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import com.zaxxer.hikari.HikariDataSource;
//
//import jakarta.persistence.EntityManagerFactory;
//
//@Configuration
//@EnableTransactionManagement
//@ComponentScan("com.e2s.cam")
//@Lazy
//public class MultiTenancyJpaConfiguration {
//
//	@Autowired
//	private JpaProperties jpaProperties;
//
//
//	@Primary
//	@Bean(name = "dataSourcesMtApp")
//	public Map<String, DataSource> dataSourcesMtApp() {
//
//
//
//		
//		Map<String, DataSource> result = new HashMap<>();
//		
//		DataSourceBuilder<?> factory1 = DataSourceBuilder.create().url("jdbc:mysql://dev-e2s-db-master.c8rtglv0a4er.us-west-2.rds.amazonaws.com:3306/authentication?characterEncoding=utf8")
//				.username("e2s-pldev-us-appadmin").password("2a09CGiCPMqyy3qItCBRGgpnX.W1DxtG8KXI8RbEMGkcgnO2UzCxmfaAi")
//				.driverClassName("com.mysql.cj.jdbc.Driver");
//		DataSourceBuilder<?> factory2 = DataSourceBuilder.create().url("jdbc:mysql://dev-e2s-db-master.c8rtglv0a4er.us-west-2.rds.amazonaws.com:3306/proretention-7?characterEncoding=utf8")
//				.username("e2s-pldev-us-appadmin").password("2a09CGiCPMqyy3qItCBRGgpnX.W1DxtG8KXI8RbEMGkcgnO2UzCxmfaAi")
//				.driverClassName("com.mysql.cj.jdbc.Driver");
//		HikariDataSource ds = (HikariDataSource)factory1.build();
//        ds.setKeepaliveTime(40000);
//        ds.setMinimumIdle(1);
//        ds.setMaxLifetime(45000);
//        ds.setIdleTimeout(35000);
//		result.put("authentication", ds);
//		HikariDataSource ds1 = (HikariDataSource)factory2.build();
//		ds1.setKeepaliveTime(40000);
//		ds1.setMinimumIdle(1);
//		ds1.setMaxLifetime(45000);
//		ds1.setIdleTimeout(35000);
//		result.put("proretention-7", ds1);
//		
//		return result;
//	}
//
//	@Bean
//	public MultiTenantConnectionProvider multiTenantConnectionProvider() {
//		return new DataSourceBasedMultiTenantConnectionProviderImpl();
//	}
//
//	@Bean
//	public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
//		return new CurrentTenantIdentifierResolverImpl();
//	}
//
//	@Bean(name="entityManagerFactoryBean")
//	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
//			AbstractDataSourceBasedMultiTenantConnectionProviderImpl multiTenantConnectionProvider,
//			CurrentTenantIdentifierResolver currentTenantIdentifierResolver) {
//
//		Map<String, Object> hibernateProps = new LinkedHashMap<>();
//		hibernateProps.putAll(this.jpaProperties.getProperties());
//		hibernateProps.put("hibernate.multiTenancy", MultiTenancyStrategy.DATABASE);
//		hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
//		hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);
////		hibernateProps.put("hibernate.hbm2ddl.auto", "update");
//		hibernateProps.put("hibernate.dialact", "org.hibernate.dialect.MySQL8Dialect");
//		LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
//		result.setPackagesToScan("com.itdib");
//		result.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//		result.setJpaPropertyMap(hibernateProps);
//
//		return result;
//	}
//
//	@Bean
//	@Primary
//	public EntityManagerFactory entityManagerFactory(LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
//		return entityManagerFactoryBean.getObject();
//	}
//
//	@Bean
//	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//		return new JpaTransactionManager(entityManagerFactory);
//	}
//}