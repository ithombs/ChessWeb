package com.thombs.ChessWeb.Configurations;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.thombs.ChessWeb.Models.Chess.ChessMatchmaking;


@Configuration
@EnableTransactionManagement
@ComponentScan({"com.thombs.ChessWeb"})
@PropertySource(value = {"classpath:application.properties"})
public class HibernateConfiguration {
	 	@Autowired
	    private Environment environment;
	 
	    @Bean
	    public LocalSessionFactoryBean sessionFactory() {
	        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
	        sessionFactory.setDataSource(dataSource());
	        sessionFactory.setPackagesToScan(new String[] {"com.thombs.ChessWeb"});
	        sessionFactory.setHibernateProperties(hibernateProperties());
	        return sessionFactory;
	     }
	     
	    @Bean
	    public DataSource dataSource() {
	    	try{
	    		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		    	dataSource.setDriverClass(environment.getRequiredProperty("jdbc.driverClassName"));
		        dataSource.setJdbcUrl(environment.getRequiredProperty("jdbc.url"));
		        dataSource.setUser(environment.getRequiredProperty("jdbc.username"));
		        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
		        dataSource.setMaxPoolSize(10);
		        dataSource.setMaxStatements(50);
		        dataSource.setMinPoolSize(3);
		        //dataSource.setProperties(hibernateProperties());
		        return dataSource;
	    	}catch(Exception e){
	    		throw new RuntimeException(e);
	    	}
	    	
	    }
	     
	    private Properties hibernateProperties() {
	        Properties properties = new Properties();
	        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
	        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
	        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
	        properties.put("hibernate.c3p0.min_size", environment.getRequiredProperty("hibernate.c3p0.min_size"));
	        properties.put("hibernate.c3p0.max_size", environment.getRequiredProperty("hibernate.c3p0.max_size"));
	        properties.put("hibernate.c3p0.timeout", environment.getRequiredProperty("hibernate.c3p0.timeout"));
	        properties.put("hibernate.c3p0.max_statements", environment.getRequiredProperty("hibernate.c3p0.max_statements"));
	        return properties;        
	    }
	     
	    @Bean
	    @Autowired
	    public HibernateTransactionManager transactionManager(SessionFactory s) {
	       HibernateTransactionManager txManager = new HibernateTransactionManager();
	       txManager.setSessionFactory(s);
	       return txManager;
	    }
}
