package com.drop4life.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

	@Bean
	public DataSource dataSource() {

	    DriverManagerDataSource dataSource =
	            new DriverManagerDataSource();

	    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

	    String dbUrl = System.getenv("DROP4LIFE_DB_URL");
	    String dbUsername = System.getenv("DROP4LIFE_DB_USERNAME");
	    String dbPassword = System.getenv("DROP4LIFE_DB_PASSWORD");

	    if (dbUrl == null || dbUrl.isBlank()) {
	        throw new IllegalStateException(
	                "Database URL is missing. Set DROP4LIFE_DB_URL.");
	    }

	    if (dbUsername == null || dbUsername.isBlank()) {
	        throw new IllegalStateException(
	                "Database username is missing. Set DROP4LIFE_DB_USERNAME.");
	    }

	    if (dbPassword == null || dbPassword.isBlank()) {
	        throw new IllegalStateException(
	                "Database password is missing. Set DROP4LIFE_DB_PASSWORD.");
	    }

	    dataSource.setUrl(dbUrl);
	    dataSource.setUsername(dbUsername);
	    dataSource.setPassword(dbPassword);

	    return dataSource;
	}

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {

        LocalSessionFactoryBean sessionFactory =
                new LocalSessionFactoryBean();

        sessionFactory.setDataSource(dataSource);

        sessionFactory.setConfigLocation(
                new org.springframework.core.io.ClassPathResource(
                        "hibernate.cfg.xml"));

        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager(
            LocalSessionFactoryBean sessionFactory,
            DataSource dataSource) {

        HibernateTransactionManager transactionManager =
                new HibernateTransactionManager(
                        sessionFactory.getObject());

        transactionManager.setDataSource(dataSource);

        return transactionManager;
    }
}