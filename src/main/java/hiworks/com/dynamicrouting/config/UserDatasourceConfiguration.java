package hiworks.com.dynamicrouting.config;


import hiworks.com.dynamicrouting.domain.admin.Admin;
import hiworks.com.dynamicrouting.domain.user.User;
import hiworks.com.dynamicrouting.multiTenancy.SubDBmanager;
import hiworks.com.dynamicrouting.repository.user.UserRepository;
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
import java.sql.SQLException;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(basePackageClasses = UserRepository.class, entityManagerFactoryRef = "userEntityManager", transactionManagerRef = "userTransactionManager")
@EnableTransactionManagement
public class UserDatasourceConfiguration {

    @Autowired(required = false)
    private PersistenceUnitManager persistenceUnitManager;


    @Bean
    public DataSource multiTenantDataSource(SubDBmanager subDBmanager) throws SQLException {
        return subDBmanager.createMultiDataSource();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean userEntityManager(final JpaProperties customerJpaProperties, @Qualifier("multiTenantDataSource") DataSource dataSource) {

        EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder(customerJpaProperties);

        return builder.dataSource(dataSource).packages(User.class)
                .persistenceUnit("userEntityManager").build();
    }

    @Bean
    @Primary
    public JpaTransactionManager userTransactionManager(@Qualifier("userEntityManager") final EntityManagerFactory factory) {
        return new JpaTransactionManager(factory);
    }

    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties customerJpaProperties) {
        JpaVendorAdapter jpaVendorAdapter = createJpaVendorAdapter();
        return new EntityManagerFactoryBuilder(jpaVendorAdapter, customerJpaProperties.getProperties(), this.persistenceUnitManager);
    }

    private JpaVendorAdapter createJpaVendorAdapter() {
        AbstractJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        return adapter;
    }
}
