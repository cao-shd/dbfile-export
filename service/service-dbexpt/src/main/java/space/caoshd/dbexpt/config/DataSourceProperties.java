package space.caoshd.dbexpt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties
@PropertySource(
    value = "classpath:datasource.properties",
    encoding = "utf-8"
)
public class DataSourceProperties {
    private Map<String, String> datasource = new HashMap<>();

    public Map<String, String> getDatasource() {
        return datasource;
    }

    public void setDatasource(Map<String, String> datasource) {
        this.datasource = datasource;
    }

}
