package space.caoshd.common.service.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("space.caoshd.*.mapper")
public class MybatisConfig {}
