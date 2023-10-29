package space.caoshd.common.dynamic_ds.datasource;

public class DataSourceHolder {

    // 线程本地环境
    private static final ThreadLocal<String> dataSources = new InheritableThreadLocal<>();

    // 设置数据源
    public static void setDataSource(String datasource) {
        dataSources.set(datasource);
    }

    //获取数据源
    public static String getDataSource() {
        return dataSources.get();
    }

    //清除数据源
    public static void clearDataSource() {
        dataSources.remove();
    }
}