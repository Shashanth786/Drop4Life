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
        dataSource.setUrl("jdbc:mysql://localhost:3306/drop4life");
        dataSource.setUsername("root");

     // Read the MySQL password from a Windows environment variable
        String dbPassword = System.getenv("DROP4LIFE_DB_PASSWORD");

        if (dbPassword == null || dbPassword.isBlank()) {
            throw new IllegalStateException(
                    "MySQL password is missing. Set DROP4LIFE_DB_PASSWORD.");
        }

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