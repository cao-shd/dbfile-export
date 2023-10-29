package space.caoshd.common.dynamic_ds.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DataSourceRegister {
    private static final Logger log = LoggerFactory.getLogger(DataSourceRegister.class);
    @Autowired
    private DynamicDataSource dynamicDataSource;

    public void setDynamicDataSource(DynamicDataSource dynamicDataSource) {
        this.dynamicDataSource = dynamicDataSource;
    }

    public void register(String alias, DataSource dataSource) {
        log.info("Register Dynamic Datasource: [{}]", alias);
        dynamicDataSource.addTargetDataSources(alias, dataSource);
    }

}
