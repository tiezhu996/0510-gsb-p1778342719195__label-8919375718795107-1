package com.fruitshop.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@MapperScan("com.fruitshop.mapper")
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class MyBatisConfig {

    @Value("${MYSQL_HOST:mysql}")
    private String mysqlHost;

    @Value("${MYSQL_PORT:3306}")
    private String mysqlPort;

    @Value("${MYSQL_DATABASE:fruitshop}")
    private String mysqlDatabase;

    @Value("${SPRING_DATASOURCE_USERNAME:fruitshop}")
    private String mysqlUsername;

    @Value("${SPRING_DATASOURCE_PASSWORD:fruitshop123}")
    private String mysqlPassword;

    @Value("${SPRING_DATASOURCE_URL:}")
    private String datasourceUrl;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // 优先使用 SPRING_DATASOURCE_URL
        String jdbcUrl = datasourceUrl;
        if (jdbcUrl == null || jdbcUrl.isEmpty()) {
            jdbcUrl = String.format(
                    "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true",
                    mysqlHost, mysqlPort, mysqlDatabase
            );
        }
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(mysqlUsername);
        config.setPassword(mysqlPassword);
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(20);
        config.setIdleTimeout(30000);
        config.setPoolName("FruitShopHikariCP");
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(30000);
        return new HikariDataSource(config);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setTypeAliasesPackage("com.fruitshop.entity");
        sessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml")
        );

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogImpl(org.apache.ibatis.logging.slf4j.Slf4jImpl.class);
        sessionFactory.setConfiguration(configuration);

        return sessionFactory.getObject();
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
