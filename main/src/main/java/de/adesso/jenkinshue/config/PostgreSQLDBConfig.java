package de.adesso.jenkinshue.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * 
 * @author wennier
 *
 */
@Configuration
@Profile("postgre")
public class PostgreSQLDBConfig implements DataSourceConfigurationType {

	@Value("${sql}")
	private boolean sql;
	
	@Value("${spring.jpa.hibernate.hbm2ddl.auto}")
	private String hbm2ddlAuto;
	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setShowSql(sql);
		hibernateJpaVendorAdapter.setGenerateDdl(sql);
		hibernateJpaVendorAdapter.setDatabase(Database.POSTGRESQL);
		return hibernateJpaVendorAdapter;
	}

	@Override
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		return dataSource;
	}
	
	@Override
	public Properties getJpaProperties() {
		Properties jpaProperties = new Properties();
		jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		jpaProperties.setProperty("hibernate.format_sql", "false");
		jpaProperties.setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
		return jpaProperties;
	}

}
