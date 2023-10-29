package space.caoshd.dbexpt.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import space.caoshd.common.dynamic_ds.datasource.DataSourceRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Configuration
public class DynamicDataSourceRegister implements InitializingBean {

    @Autowired
    DataSourceProperties dataSourceProperties;

    @Autowired
    DataSourceRegister dataSourceRegister;

    @Override
    public void afterPropertiesSet() {
        Map<String, Properties> aliasDataSourceProperties = new HashMap<>();
        dataSourceProperties.getDatasource().forEach((propertyKey, propertyValue) -> {
            // get alias
            String dataSourceAlias = calcDataSourceAlias(propertyKey);
            Properties properties = getAndPut(aliasDataSourceProperties, dataSourceAlias);
            // get name
            String propertyName = calcPropertyName(propertyKey);
            properties.put(propertyName, propertyValue);
        });

        // register all datasource
        aliasDataSourceProperties.forEach((alias, properties) -> {
            DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
            dataSource.configFromPropety(properties);
            dataSourceRegister.register(alias, dataSource);
        });

    }

    private static String calcDataSourceAlias(String propertyKey) {
        int lastPointIndex = propertyKey.lastIndexOf('.');
        return propertyKey.substring(0, lastPointIndex);
    }

    private static String calcPropertyName(String propertyKey) {
        int lastPointIndex = propertyKey.lastIndexOf('.');
        return "druid." + propertyKey.substring(lastPointIndex + 1);
    }

    private static Properties getAndPut(Map<String, Properties> aliasDataSourceProperties, String alias) {
        if (aliasDataSourceProperties.containsKey(alias)) {
            return aliasDataSourceProperties.get(alias);
        }

        Properties properties = new Properties();
        aliasDataSourceProperties.put(alias, properties);
        return properties;
    }

}
