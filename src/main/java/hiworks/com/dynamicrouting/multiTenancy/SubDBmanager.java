package hiworks.com.dynamicrouting.multiTenancy;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class SubDBmanager {

    private final Map<Object, Object> tenantDataSources = new ConcurrentHashMap<>();

    private AbstractRoutingDataSource multiTenantDataSource;

    public DataSource createMultiDataSource() throws SQLException {

        multiTenantDataSource = new MultiTenantDataSource();
        multiTenantDataSource.setTargetDataSources(tenantDataSources);
        multiTenantDataSource.setDefaultTargetDataSource(defaultDataSource());
        multiTenantDataSource.afterPropertiesSet();
        return multiTenantDataSource;
    }


    public void setCurrentTenant(String tenantId,String port,String partNo) throws SQLException {
        if (tenantIsAbsent(tenantId)) {
                String url = "jdbc:mysql://192.168.99.100:"+port+"/user"+partNo+"?serverTimezone=UTC&useLegacyDatetimeCode=false&autoReconnect=true&useUnicode=true&characterEncoding=utf-8";
                String username = "root";
                String password ="password";

                addTenant(tenantId, url, username, password);

        }
        ThreadLocalStorage.setSubDBName(tenantId);

    }

    public void addTenant(String tenantId, String url, String username, String password) throws SQLException {

        DataSource dataSource = DataSourceBuilder
                .create()
                .username(username)
                .password(password)
                .url(url)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();

        // Check that new connection is 'live'. If not - throw exception
        try(Connection c = dataSource.getConnection()) {
            tenantDataSources.put(tenantId, dataSource);
            multiTenantDataSource.afterPropertiesSet();
        }
    }

    public DataSource removeTenant(String tenantId) {
        Object removedDataSource = tenantDataSources.remove(tenantId);
        multiTenantDataSource.afterPropertiesSet();
        return (DataSource) removedDataSource;
    }

    public boolean tenantIsAbsent(String tenantId) {
        return !tenantDataSources.containsKey(tenantId);
    }


    private DriverManagerDataSource defaultDataSource() throws SQLException {
        DriverManagerDataSource defaultDataSource = new DriverManagerDataSource();
        defaultDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        defaultDataSource.setUrl("jdbc:mysql://192.168.99.100:3307/user01?serverTimezone=UTC&useLegacyDatetimeCode=false&autoReconnect=true&useUnicode=true&characterEncoding=utf-8");
        defaultDataSource.setUsername("root");
        defaultDataSource.setPassword("password");
        addTenant("3307-user01",defaultDataSource.getUrl(),"root","password");

        return defaultDataSource;

    }
}
