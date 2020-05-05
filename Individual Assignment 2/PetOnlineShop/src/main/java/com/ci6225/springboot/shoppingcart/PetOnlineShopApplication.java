package com.ci6225.springboot.shoppingcart;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@SpringBootApplication

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
public class PetOnlineShopApplication {
	
	@Autowired
	private Environment env;
	public static void main(String[] args) {
		SpringApplication.run(PetOnlineShopApplication.class, args);
	}
	
	@Bean(name="dataSource")
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.password"));
		
		return dataSource;
	}
	
	@Autowired
	@Bean(name="sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource) throws Exception{
		Properties prop = new Properties();
		
		prop.put("hiberate.dialet", env.getProperty("spring.jpa.properties.hibernate.dialect"));
		prop.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
		prop.put("current_session_context_class", env.getProperty("spring.jpa.properties.hibernate.current_session_context_class"));
		LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
		
		factory.setPackagesToScan(new String[] {""});
		factory.setDataSource(dataSource);
		factory.setHibernateProperties(prop);
		factory.afterPropertiesSet();
		
		SessionFactory sf = factory.getObject();
		return sf;
	}
	
	@Autowired
	@Bean(name="transactionManager")
	public HibernateTransactionManager getTransactionManager(SessionFactory ssFactory) {
		HibernateTransactionManager transactionManager =new HibernateTransactionManager(ssFactory);
		return transactionManager;
	}
	

}