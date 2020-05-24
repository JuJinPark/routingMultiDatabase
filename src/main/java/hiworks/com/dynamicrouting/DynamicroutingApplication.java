package hiworks.com.dynamicrouting;

import hiworks.com.dynamicrouting.domain.admin.Admin;
import hiworks.com.dynamicrouting.multiTenancy.SubDBmanager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

////
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class })
//@SpringBootApplication
public class DynamicroutingApplication {

	public static void main(String[] args) {
		SpringApplication.run(DynamicroutingApplication.class, args);
	}

	@Bean
	@Primary
	@ConfigurationProperties("app.customer.jpa")
	public JpaProperties customerJpaProperties() {
		return new JpaProperties();
	}


	@Bean
	public SubDBmanager SubDBManager(){
		return new SubDBmanager();
	}




}
