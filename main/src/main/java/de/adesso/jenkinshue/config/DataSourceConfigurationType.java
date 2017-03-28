package de.adesso.jenkinshue.config;

import java.util.Properties;

import javax.sql.DataSource;

/**
 * 
 * @author wennier
 *
 */
public interface DataSourceConfigurationType {

	public DataSource dataSource();

	public Properties getJpaProperties();
	
}
