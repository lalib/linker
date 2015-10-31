package com.bilalalp.common.config;

import com.bilalalp.common.constant.CommonDatabaseConstant;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.cfg.Environment;
import org.hibernate.jpa.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

@ComponentScan(value = {"com.bilalalp.common"})
@PropertySource(value = {"classpath:application.properties"})
@Configuration
@EnableJpaRepositories(basePackages = "com.bilalalp.common.repository")
public class CommonConfig {

    @Autowired
    private CommonConfigHelper commonConfigHelper;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws PropertyVetoException {
        final LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(dataSource());
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        localContainerEntityManagerFactoryBean.setJpaDialect(new HibernateJpaDialect());
        localContainerEntityManagerFactoryBean.setPackagesToScan(CommonDatabaseConstant.PACKAGES_TO_SCAN);
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(getJpaPropertyMap());
        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager() throws PropertyVetoException {
        final JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return jpaTransactionManager;
    }

    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        final ComboPooledDataSource basicDataSource = new ComboPooledDataSource();
        basicDataSource.setJdbcUrl(commonConfigHelper.getJdbcUrl());
        basicDataSource.setPassword(commonConfigHelper.getJdbcPassword());
        basicDataSource.setUser(commonConfigHelper.getJdbcUsername());
        basicDataSource.setDriverClass(commonConfigHelper.getJdbcDriverClassName());
        basicDataSource.setMaxPoolSize(commonConfigHelper.getJdbcMaxPoolSize());
        basicDataSource.setInitialPoolSize(commonConfigHelper.getInitialPoolSize());
        basicDataSource.setAcquireIncrement(commonConfigHelper.getJdbcIncrementSize());
        basicDataSource.setIdleConnectionTestPeriod(commonConfigHelper.getJdbcConnectionTestPeriod());
        basicDataSource.setMaxIdleTime(commonConfigHelper.getJdbcMaxIdleTime());
        basicDataSource.setUnreturnedConnectionTimeout(commonConfigHelper.getJdbcUnreturnedTimeOut());
        basicDataSource.setAutoCommitOnClose(commonConfigHelper.getJdbcAutoCommit());
        basicDataSource.setNumHelperThreads(commonConfigHelper.getJdbcNumHelperThreads());
        basicDataSource.setMaxStatements(commonConfigHelper.getJdbcMaxStatements());
        basicDataSource.setMaxStatementsPerConnection(commonConfigHelper.getJdbcMaxStatementsPerConnection());
        basicDataSource.setDebugUnreturnedConnectionStackTraces(commonConfigHelper.getJdbcDebugUnreturnedConnectionStateTraces());
        basicDataSource.setMinPoolSize(commonConfigHelper.getJdbcMinPoolSize());
        return basicDataSource;
    }

    private Map<String, Object> getJpaPropertyMap() {
        final Map<String, Object> jpaPropertyMap = new HashMap<>();
        jpaPropertyMap.put(Environment.IMPLICIT_NAMING_STRATEGY, new ImplicitNamingStrategyJpaCompliantImpl());
        jpaPropertyMap.put(Environment.PHYSICAL_NAMING_STRATEGY, new PhysicalNamingStrategyStandardImpl());
        jpaPropertyMap.put(Environment.HBM2DDL_AUTO, commonConfigHelper.getHibernateHbm2DDLAuto());
        jpaPropertyMap.put(Environment.SHOW_SQL, commonConfigHelper.getHibernateShowSql());
        jpaPropertyMap.put(Environment.FORMAT_SQL, commonConfigHelper.getHibernateFormatSql());
        jpaPropertyMap.put(Environment.GENERATE_STATISTICS, commonConfigHelper.gethibernateGenerateStatistics());
        jpaPropertyMap.put(Environment.MAX_FETCH_DEPTH, commonConfigHelper.getHibernateMaxFetchDepth());
        jpaPropertyMap.put(Environment.DEFAULT_BATCH_FETCH_SIZE, commonConfigHelper.getHibernateDefaultBatchFetchSize());
        jpaPropertyMap.put(Environment.STATEMENT_BATCH_SIZE, commonConfigHelper.getHibernateBatchSize());
        jpaPropertyMap.put(Environment.CACHE_REGION_FACTORY, commonConfigHelper.getHibernateCacheRegionFactoryClass());
        jpaPropertyMap.put(Environment.USE_QUERY_CACHE, commonConfigHelper.getHibernateCacheUseQueryCache());
        jpaPropertyMap.put(Environment.USE_SECOND_LEVEL_CACHE, commonConfigHelper.getHibernateCacheUseSecondLevelCache());
        jpaPropertyMap.put(Environment.DEFAULT_SCHEMA, commonConfigHelper.getHibernateDefaultSchema());
        jpaPropertyMap.put(AvailableSettings.INTERCEPTOR, commonConfigHelper.getHibernateEjbInterceptor());
        return jpaPropertyMap;
    }
}