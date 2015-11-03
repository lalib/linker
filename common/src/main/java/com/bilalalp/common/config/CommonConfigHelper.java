package com.bilalalp.common.config;

import com.bilalalp.common.constant.CommonDatabaseConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class CommonConfigHelper {

    @Autowired
    private Environment environment;

    public String getJdbcUrl() {
        return getStringProperty(CommonDatabaseConstant.JDBC_URL);
    }

    public String getJdbcPassword() {
        return getStringProperty(CommonDatabaseConstant.JDBC_PASSWORD);
    }

    public String getJdbcUsername() {
        return getStringProperty(CommonDatabaseConstant.JDBC_USERNAME);
    }

    public String getJdbcDriverClassName() {
        return getStringProperty(CommonDatabaseConstant.JDBC_DRIVER_CLASS_NAME);
    }

    public Integer getJdbcMaxPoolSize() {
        return getIntProperty(CommonDatabaseConstant.JDBC_MAX_POOL_SIZE);
    }

    public Integer getInitialPoolSize() {
        return getIntProperty(CommonDatabaseConstant.JDBC_INITIAL_POOL_SIZE);
    }

    public Integer getJdbcIncrementSize() {
        return getIntProperty(CommonDatabaseConstant.JDBC_INCREMENT_SIZE);
    }

    public Integer getJdbcConnectionTestPeriod() {
        return getIntProperty(CommonDatabaseConstant.JDBC_CONNECTION_TEST_PERIOD);
    }

    public Integer getJdbcMaxIdleTime() {
        return getIntProperty(CommonDatabaseConstant.JDBC_MAX_IDLE_TIME);
    }

    public Integer getJdbcUnreturnedTimeOut() {
        return getIntProperty(CommonDatabaseConstant.JDBC_UNRETURNED_TIMEOUT);
    }

    public Boolean getJdbcAutoCommit() {
        return getBooleanProperty(CommonDatabaseConstant.JDBC_AUTO_COMMIT);
    }

    public Integer getJdbcNumHelperThreads() {
        return getIntProperty(CommonDatabaseConstant.JDBC_NUM_HELPER_THREADS);
    }

    public Integer getJdbcMaxStatements() {
        return getIntProperty(CommonDatabaseConstant.JDBC_MAX_STATEMENTS);
    }

    public Integer getJdbcMaxStatementsPerConnection() {
        return getIntProperty(CommonDatabaseConstant.JDBC_MAX_STATEMENTS_PER_CONNECTION);
    }

    public Boolean getJdbcDebugUnreturnedConnectionStateTraces() {
        return getBooleanProperty(CommonDatabaseConstant.JDBC_DEBUG_UNRETURNED_CONNECTION_STACK_TRACES);
    }

    public Integer getJdbcMinPoolSize() {
        return getIntProperty(CommonDatabaseConstant.JDBC_MIN_POOL_SIZE);
    }


    public String getHibernateHbm2DDLAuto() {
        return getStringProperty(CommonDatabaseConstant.HIBERNATE_HBM2DDL_AUTO);
    }

    public Boolean getHibernateShowSql() {
        return getBooleanProperty(CommonDatabaseConstant.HIBERNATE_SHOW_SQL);
    }

    public Boolean getHibernateFormatSql() {
        return getBooleanProperty(CommonDatabaseConstant.HIBERNATE_FORMAT_SQL);
    }

    public Boolean gethibernateGenerateStatistics() {
        return getBooleanProperty(CommonDatabaseConstant.HIBERNATE_GENERATE_STATISTICS);
    }

    public Integer getHibernateMaxFetchDepth() {
        return getIntProperty(CommonDatabaseConstant.HIBERNATE_MAX_FETCH_DEPTH);
    }

    public Integer getHibernateDefaultBatchFetchSize() {
        return getIntProperty(CommonDatabaseConstant.HIBERNATE_DEFAULT_BATCH_FETCH_SIZE);
    }

    public Integer getHibernateBatchSize() {
        return getIntProperty(CommonDatabaseConstant.HIBERNATE_JDBC_BATCH_SIZE);
    }

    public String getHibernateCacheRegionFactoryClass() {
        return getStringProperty(CommonDatabaseConstant.HIBERNATE_CACHE_REGION_FACTORY_CLASS);
    }

    public Boolean getHibernateCacheUseQueryCache() {
        return getBooleanProperty(CommonDatabaseConstant.HIBERNATE_CACHE_USE_QUERY_CACHE);
    }

    public Boolean getHibernateCacheUseSecondLevelCache() {
        return getBooleanProperty(CommonDatabaseConstant.HIEBRNATE_CACHE_USE_SECOND_LEVEL_CACHE);
    }

    public String getHibernateDefaultSchema() {
        return environment.getProperty(CommonDatabaseConstant.HIBERNATE_DEFAULT_SCHEMA);
    }

    public String getHibernateEjbInterceptor() {
        return environment.getProperty(CommonDatabaseConstant.HIBERNATE_EJB_INTERCEPTOR);
    }

    private Integer getIntProperty(final String propertyName) {
        return environment.getProperty(propertyName, Integer.class);
    }

    private String getStringProperty(final String propertyName) {
        return environment.getProperty(propertyName);
    }

    private Boolean getBooleanProperty(final String propertyName) {
        return environment.getProperty(propertyName, Boolean.class);
    }
}