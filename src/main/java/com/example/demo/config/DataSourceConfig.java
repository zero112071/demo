package com.example.demo.config;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 主資料庫設定
 */
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory", //
		transactionManagerRef = "transactionManager", //
		basePackages = { //
				"com.example.demo.coin.repository" //
		})
@Configuration
@EnableConfigurationProperties
public class DataSourceConfig extends DataSourceAutoConfiguration {

	/** 主資料庫 */
	public static final String PRIMARY_DATABASE = "primaryDatabase";

	private static transient final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

	@Autowired
	private HibernateProperties hibernateProperties;

	@Autowired
	private JpaProperties jpaProperties;

	@Primary
	@Bean(name = "dataSourceProperties")
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public DataSourceProperties dataSourceProperties() {
		DataSourceProperties dataSourceProperties = new DataSourceProperties();
		return dataSourceProperties;
	}

	@Primary
	@Bean(name = "hikariConfig")
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		HikariConfig hikariConfig = new HikariConfig();
		return hikariConfig;
	}

	@Primary
	@Bean(name = "dataSource")
	public DataSource dataSource(@Qualifier("hikariConfig") HikariConfig hikariConfig) {
		LOGGER.info("poolName: " + hikariConfig.getPoolName());
		LOGGER.info("jdbcUrl: " + hikariConfig.getJdbcUrl());
		LOGGER.info("username: " + hikariConfig.getUsername());
		LOGGER.info("driverClassName: " + hikariConfig.getDriverClassName());
		LOGGER.info("maximumPoolSize: " + hikariConfig.getMaximumPoolSize());
		LOGGER.info("connectionTimeout: " + hikariConfig.getConnectionTimeout());
		LOGGER.info("maxLifetime: " + hikariConfig.getMaxLifetime());
		LOGGER.info("autoCommit: " + hikariConfig.isAutoCommit());
		//
		LOGGER.info("jpaProperties: " + jpaProperties.getProperties());
		//
		DataSource dataSource = new HikariDataSource(hikariConfig);
		return dataSource;
	}

	@Primary
	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("dataSource") DataSource dataSource) {
		// 2020/02/05
		Map<String, Object> properties = hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(),
				new HibernateSettings());
		//
		return builder.dataSource(dataSource)//
				.packages( //
						"com.example.demo.coin.model" //

				).persistenceUnit(PRIMARY_DATABASE)//
				.properties(properties)//
				.build();
	}

	@Primary
	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	@Primary
	@Bean(name = "jdbcTemplate")
	public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

}
