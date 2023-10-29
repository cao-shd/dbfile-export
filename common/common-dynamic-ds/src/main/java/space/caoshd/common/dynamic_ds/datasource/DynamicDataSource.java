package space.caoshd.common.dynamic_ds.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
@Primary
@Component
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Autowired
    private DataSource dataSource;

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.getDataSource();
    }

    public void addTargetDataSources(Object key, DataSource dataSource) {
        Map<Object, DataSource> resolvedDataSources = super.getResolvedDataSources();
        Map<Object, Object> targetDataSources = new HashMap<>(resolvedDataSources);
        targetDataSources.put(key, dataSource);
        this.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    public void afterPropertiesSet() {
        Map<Object, Object> targetDataSource = new HashMap<>();
        targetDataSource.put("dataSource", dataSource);
        super.setTargetDataSources(targetDataSource);
        super.setDefaultTargetDataSource(dataSource);
        super.afterPropertiesSet();
    }

}