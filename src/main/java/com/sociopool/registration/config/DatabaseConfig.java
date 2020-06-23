package com.sociopool.registration.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class DatabaseConfig {
    @Value("${spring.datasource.isJndi}")
    private boolean isJndi;

    @Value("${spring.datasource.jndiName}")
    private String jndiName;

    @Autowired
    Environment environment;

    private DataSource getDataSource(String name) {
        JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
        return dataSourceLookup.getDataSource(name);
    }

    @Bean(name = "Datasource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDataSource() {
        if (isJndi) {
            return getDataSource(jndiName);
        }
        log.info("Initializing Datasource bean inside DatabaseConfig...");

        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.password"));
        return dataSource;
    }

    @Bean(name = "NamedJdbcTemplateSqlServer")
    public NamedParameterJdbcTemplate getNamedJdbcTemplate(@Qualifier("Datasource") DataSource ds) {
        log.info("Initializing NamedJdbcTemplate bean inside DatabaseConfig..." + ds);
        return new NamedParameterJdbcTemplate(ds);
    }
    
    @Bean(name = "JdbcTemplateSqlServer")
    public JdbcTemplate getJdbcTemplate(@Qualifier("Datasource") DataSource ds) {
        log.info("Initializing JdbcTemplate bean inside DatabaseConfig..." + ds);
        return new JdbcTemplate(ds);
    }

	/*
	 * @Bean(name = "TransactionManager") public PlatformTransactionManager
	 * getTransactionManager(@Qualifier("Datasource") DataSource ds) {
	 * log.info("Initializing TransactionManager bean inside DatabaseConfig..." +
	 * ds); return new DataSourceTransactionManager(ds); }
	 */

}
