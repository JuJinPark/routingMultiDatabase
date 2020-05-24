package hiworks.com.dynamicrouting.config;


import hiworks.com.dynamicrouting.domain.admin.Admin;
import hiworks.com.dynamicrouting.repository.admin.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;


@Configuration
@EnableJpaRepositories(basePackageClasses = AdminRepository.class, entityManagerFactoryRef = "adminEntityManager", transactionManagerRef = "adminTransactionManager")
@EnableTransactionManagement
public class MasterDatasourceConfiguration {

    @Autowired(required = false)
    private PersistenceUnitManager persistenceUnitManager;

    @Bean
    public LocalContainerEntityManagerFactoryBean adminEntityManager(final JpaProperties customerJpaProperties ) {
        EntityManagerFactoryBuilder builder =
                createEntityManagerFactoryBuilder(customerJpaProperties);

        return builder.dataSource(adminDataSource()).packages(Admin.class)
                .persistenceUnit("adminEntityManager").build();


//        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(adminDataSource());
//        em.setPackagesToScan("hiworks.com.dynamicrouting.domain.admin");
//        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        em.setJpaVendorAdapter(vendorAdapter);
//        final HashMap<String, Object> properties = new HashMap<String, Object>();
////        properties.put("hibernate.hbm2ddl.auto", customerJpaProperties.getProperty("hibernate.hbm2ddl.auto"));
//        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
//    properties.put("hibernate.physical_naming_strategy" , "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
//
//
//        em.setJpaPropertyMap(properties);
//
//        return em;
    }

    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties customerJpaProperties) {
        JpaVendorAdapter jpaVendorAdapter =
                createJpaVendorAdapter(customerJpaProperties);
        return new EntityManagerFactoryBuilder(jpaVendorAdapter,
                customerJpaProperties.getProperties(), this.persistenceUnitManager);
    }

    private JpaVendorAdapter createJpaVendorAdapter(JpaProperties jpaProperties) {
        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        return adapter;
    }

    @Bean
    public DataSource adminDataSource() {
        return DataSourceBuilder
                .create()
                .username("root")
                .password("password")
                .url("jdbc:mysql://192.168.99.100:3306/admin?serverTimezone=UTC&useLegacyDatetimeCode=false&autoReconnect=true&useUnicode=true&characterEncoding=utf-8")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }

    @Bean
    public PlatformTransactionManager adminTransactionManager(@Qualifier("adminEntityManager") final EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }
}
