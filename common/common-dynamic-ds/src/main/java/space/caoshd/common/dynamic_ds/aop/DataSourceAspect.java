package space.caoshd.common.dynamic_ds.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import space.caoshd.common.dynamic_ds.annotation.DataSourceAlias;
import space.caoshd.common.dynamic_ds.annotation.DataSourceSwitch;
import space.caoshd.common.dynamic_ds.datasource.DataSourceHolder;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

// 优先级要设置在事务切面执行之前
@Order(1)
@Aspect
@Component
public class DataSourceAspect {

    public static final Logger log = LoggerFactory.getLogger(DataSourceAspect.class);

    @Pointcut("@annotation(space.caoshd.common.dynamic_ds.annotation.DataSourceSwitch)")
    public void pointcut() {
    }

    @Before(value = "pointcut()")
    public void before(JoinPoint joinPoint) {
        String dataSourceAlias = calcDataSourceAlias(joinPoint);
        if (StringUtils.hasText(dataSourceAlias)) {
            log.info("Switch datasource: [{}]", dataSourceAlias);
            DataSourceHolder.setDataSource(dataSourceAlias);
        }
    }

    @After(value = "pointcut()")
    public void after() {
        DataSourceHolder.clearDataSource();
        log.info("Switch default datasource");
    }
    private String calcDataSourceAlias(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            DataSourceAlias annotation = parameters[i].getAnnotation(DataSourceAlias.class);
            if (annotation != null) {
                return (String) joinPoint.getArgs()[i];
            }
        }
        DataSourceSwitch switchSource = method.getAnnotation(DataSourceSwitch.class);
        return switchSource.value();
    }

}