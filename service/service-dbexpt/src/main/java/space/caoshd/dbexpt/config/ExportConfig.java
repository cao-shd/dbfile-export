package space.caoshd.dbexpt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@EnableAsync
@EnableScheduling
@Configuration
@PropertySource(
    value = "classpath:dbfile-export.properties",
    encoding = "utf-8"
)
public class ExportConfig {

    @Value("${file-export.thread-pool.core-pool-size:2}")
    private int corePoolSize;
    @Value("${file-export.thread-pool.max-pool-size:4}")
    private int maxPoolSize;
    @Value("${file-export.thread-pool.queue-capacity:0}")
    private int queueCapacity;
    @Value("${file-export.thread-pool.thread-name-prefix:file-export}")
    private String namePrefix;

    @Bean
    public ThreadPoolTaskExecutor fileExportThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程数
        executor.setCorePoolSize(corePoolSize);
        // 配置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        // 配置队列大小
        executor.setQueueCapacity(queueCapacity);
        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(namePrefix);
        //执行初始化
        executor.initialize();
        return executor;
    }

}
