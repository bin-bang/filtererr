package model.filtererr.config;


import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "model.filtererr.mapper.targetmapper",sqlSessionFactoryRef = "targetSqlSessionFactory")
public class TargetDataSourceConfig {

    @Value("${spring.datasource.druid.targetdb.url}")
    private String dbUrl;

    @Value("${spring.datasource.druid.targetdb.username}")
    private String username;

    @Value("${spring.datasource.druid.targetdb.password}")
    private String password;

    @Value("${spring.datasource.druid.targetdb.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.druid.initial-size}")
    private int initialSize;

    @Value("${spring.datasource.druid.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.druid.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.druid.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.druid.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.druid.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.druid.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.druid.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.druid.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;


    @Value("${spring.datasource.druid.connectionProperties}")
    private String connectionProperties;

    @Value("${spring.datasource.druid.useGlobalDataSourceStat}")
    private boolean useGlobalDataSourceStat;

    @Value("${spring.datasource.druid.keepAlive}")
    private boolean keepAlive;

    @Primary
    @Bean(name = "targetDataSource")
    public DataSource masterDataSource(){
        DruidDataSource datasource = new DruidDataSource();

        datasource.setUrl(this.dbUrl);

        datasource.setUsername(this.username);

        datasource.setPassword(this.password);

        datasource.setDriverClassName(this.driverClassName);

        //configuration
        datasource.setInitialSize(initialSize);

        datasource.setMinIdle(minIdle);

        datasource.setMaxActive(this.maxActive);

        datasource.setMaxWait(this.maxWait);

        datasource.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);

        datasource.setMinEvictableIdleTimeMillis(this.minEvictableIdleTimeMillis);

        datasource.setValidationQuery(this.validationQuery);

        datasource.setTestWhileIdle(testWhileIdle);

        datasource.setTestOnBorrow(testOnBorrow);

        datasource.setTestOnReturn(this.testOnReturn);

        datasource.setPoolPreparedStatements(poolPreparedStatements);

        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);

        datasource.setUseGlobalDataSourceStat(useGlobalDataSourceStat);

        datasource.setConnectionProperties(connectionProperties);

        datasource.setKeepAlive(keepAlive);

        return datasource;
    }

    @Bean(name="targetSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("targetDataSource") DataSource dataSource) throws Exception {
        try {
            SqlSessionFactoryBean sessionFactoryBean=new SqlSessionFactoryBean();
            sessionFactoryBean.setDataSource(dataSource);
            sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                    .getResource("classpath:mybatis/mapper/targetmapper/TargetMapper.xml"));
            return sessionFactoryBean.getObject();
        }catch (Exception e){

        }
        return null;
    }
}
