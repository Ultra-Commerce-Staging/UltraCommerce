/*
 * #%L
 * UltraCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2016 Ultra Commerce
 * %%
 * Licensed under the Ultra Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.ultracommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Ultra in which case
 * the Ultra End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.ultracommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Ultra Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */
package com.ultracommerce.common.extensibility.jpa;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.demo.AutoImportSql;
import com.ultracommerce.common.demo.CompositeAutoImportSql;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 *   This class allows us to override Persistence Unit properties on a per-environment basis. Spring's
 *   PersistenceUnitManager allows us to pass in a list of PersistenceUnitPostProcessors.  This class will allow us to override
 *   or add custom JPA properties to the Persistence Unit.  This is useful when different environments have different requirements
 *   for JPA properties.  The best example of this is SQL Dialect.  You may want to use a dialect such as Oracle for production
 *   and test environments, and perhaps HSQLDB for local and integration testing.  You can set the dialect property using
 *   Spring profiles and properties.  The keys will be the same in each environment, but the values would be defined in the
 *   environment-specific properties files.  If you want the property to be added only to certain environments,
 *   add the value "null" to the properties file.  For example:
 *
 *
 *   <bean id="ucPersistenceUnitManager" class="com.ultracommerce.profile.extensibility.jpa.MergePersistenceUnitManager">
 *       <property name="persistenceXmlLocations">
 *           <list>
 *               <value>classpath*:/META-INF/mycompany_persistence.xml</value>
 *           </list>
 *        </property>
 *        <property name="persistenceUnitPostProcessors">
 *            <list>
 *                <bean class="com.ultracommerce.common.extensibility.jpa.JPAPropertiesPersistenceUnitPostProcessor">
 *                    <property name="persistenceUnitProperties">
 *                        <map>
 *                            <!-- Notice that the value will be replaced by property substitution from an environment
 *                                 specific file. Also note that the Persistence Unit name is prepended to the key and value to allow for configuration of
 *                                 multiple Persistence Units. This needs to be keyed this way in the properties file. The prepended persistence 
 *                                 unit name will be removed from the property name when it is added to the Persistence Unit. -->
 *                            <entry key="ucPU.hibernate.dialect" value="${ucPU.hibernate.dialect}"/>
 *                        </map>
 *                    </property>
 *                </bean>
 *            </list>
 *        </property>
 *    </bean>
 *
 */
public class JPAPropertiesPersistenceUnitPostProcessor implements org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor {

    private static final Log LOG = LogFactory.getLog(JPAPropertiesPersistenceUnitPostProcessor.class);
    
    protected Map<String, String> persistenceUnitProperties = new HashMap<>();
    protected Map<String, String> overrideProperties = new HashMap<>();

    @Resource(name="ucCompositeAutoImportSql")
    protected CompositeAutoImportSql compositeAutoImportSql;

    @Value("${ucPU.hibernate.hbm2ddl.auto}")
    protected String ucPUHibernateHbm2ddlAuto;
    @Value("${ucPU.hibernate.dialect}")
    protected String ucPUHibernateDialect;
    @Value("${ucPU.hibernate.show_sql}")
    protected String ucPUHibernateShow_sql;
    @Value("${ucPU.hibernate.cache.use_second_level_cache}")
    protected String ucPUHibernateCacheUse_second_level_cache;
    @Value("${ucPU.hibernate.cache.use_query_cache}")
    protected String ucPUHibernateCacheUse_query_cache;
    @Value("${ucPU.hibernate.hbm2ddl.import_files}")
    protected String ucPUHibernateHbm2ddlImport_files;
    @Value("${ucPU.hibernate.hbm2ddl.import_files_sql_extractor}")
    protected String ucPUHibernateHbm2ddlImport_files_sql_extractor;
    @Value("${ucPU.hibernate.implicit_naming_strategy}")
    protected String ucPUHibernateNamingImplicitStrategy;
    @Value("${ucPU.hibernate.physical_naming_strategy}")
    protected String ucPUHibernateNamingPhysicalStrategy;

    @Value("${ucCMSStorage.hibernate.hbm2ddl.auto}")
    protected String ucCMSStorageHibernateHbm2ddlAuto;
    @Value("${ucCMSStorage.hibernate.dialect}")
    protected String ucCMSStorageHibernateDialect;
    @Value("${ucCMSStorage.hibernate.show_sql}")
    protected String ucCMSStorageHibernateShow_sql;
    @Value("${ucCMSStorage.hibernate.cache.use_second_level_cache}")
    protected String ucCMSStorageHibernateCacheUse_second_level_cache;
    @Value("${ucCMSStorage.hibernate.cache.use_query_cache}")
    protected String ucCMSStorageHibernateCacheUse_query_cache;
    @Value("${ucCMSStorage.hibernate.hbm2ddl.import_files}")
    protected String ucCMSStorageHibernateHbm2ddlImport_files;
    @Value("${ucCMSStorage.hibernate.hbm2ddl.import_files_sql_extractor}")
    protected String ucCMSStorageHibernateHbm2ddlImport_files_sql_extractor;
    @Value("${ucCMSStorage.hibernate.implicit_naming_strategy}")
    protected String ucCMSStorageHibernateNamingImplicitStrategy;
    @Value("${ucCMSStorage.hibernate.physical_naming_strategy}")
    protected String ucCMSStorageHibernateNamingPhysicalStrategy;

    @Value("${ucSecurePU.hibernate.hbm2ddl.auto}")
    protected String ucSecurePUHibernateHbm2ddlAuto;
    @Value("${ucSecurePU.hibernate.dialect}")
    protected String ucSecurePUHibernateDialect;
    @Value("${ucSecurePU.hibernate.show_sql}")
    protected String ucSecurePUHibernateShow_sql;
    @Value("${ucSecurePU.hibernate.cache.use_second_level_cache}")
    protected String ucSecurePUHibernateCacheUse_second_level_cache;
    @Value("${ucSecurePU.hibernate.cache.use_query_cache}")
    protected String ucSecurePUHibernateCacheUse_query_cache;
    @Value("${ucSecurePU.hibernate.hbm2ddl.import_files}")
    protected String ucSecurePUHibernateHbm2ddlImport_files;
    @Value("${ucSecurePU.hibernate.hbm2ddl.import_files_sql_extractor}")
    protected String ucSecurePUHibernateHbm2ddlImport_files_sql_extractor;
    @Value("${ucSecurePU.hibernate.implicit_naming_strategy}")
    protected String ucSecurePUHibernateNamingImplicitStrategy;
    @Value("${ucSecurePU.hibernate.physical_naming_strategy}")
    protected String ucSecurePUHibernateNamingPhysicalStrategy;

    @PostConstruct
    public void populatePresetProperties() {
        if (!ucPUHibernateHbm2ddlAuto.startsWith("${")) persistenceUnitProperties.put("ucPU.hibernate.hbm2ddl.auto", ucPUHibernateHbm2ddlAuto);
        if (!ucPUHibernateDialect.startsWith("${")) persistenceUnitProperties.put("ucPU.hibernate.dialect", ucPUHibernateDialect);
        if (!ucPUHibernateShow_sql.startsWith("${")) persistenceUnitProperties.put("ucPU.hibernate.show_sql", ucPUHibernateShow_sql);
        if (!ucPUHibernateCacheUse_second_level_cache.startsWith("${")) persistenceUnitProperties.put("ucPU.hibernate.cache.use_second_level_cache", ucPUHibernateCacheUse_second_level_cache);
        if (!ucPUHibernateCacheUse_query_cache.startsWith("${")) persistenceUnitProperties.put("ucPU.hibernate.cache.use_query_cache", ucPUHibernateCacheUse_query_cache);
        if (!ucPUHibernateHbm2ddlImport_files.startsWith("${") && !"null".equals(ucPUHibernateHbm2ddlImport_files)) {
            persistenceUnitProperties.put("ucPU.hibernate.hbm2ddl.import_files", ucPUHibernateHbm2ddlImport_files);
        } else {
            String autoImportSql = compositeAutoImportSql.compileSqlFilePathList("ucPU");
            if (LOG.isInfoEnabled()) {
                Map<String, List<AutoImportSql>> loggingMap = compositeAutoImportSql.constructAutoImportSqlMapForPU("ucPU");
                LOG.info("Auto-importing the following SQL files in order:");
                for (String stage : loggingMap.keySet()) {
                    LOG.info(stage);
                    for (AutoImportSql sqlFile : loggingMap.get(stage)) {
                        LOG.info("[order:" + sqlFile.getOrder() + "] " + sqlFile.getSqlFilePath());
                    }
                }
            }

            if (!StringUtils.isEmpty(autoImportSql)) {
                persistenceUnitProperties.put("ucPU.hibernate.hbm2ddl.import_files", autoImportSql);
            }
        }
        if (!ucPUHibernateHbm2ddlImport_files_sql_extractor.startsWith("${")) persistenceUnitProperties.put("ucPU.hibernate.hbm2ddl.import_files_sql_extractor", ucPUHibernateHbm2ddlImport_files_sql_extractor);
        if (!ucPUHibernateNamingImplicitStrategy.startsWith("${")) persistenceUnitProperties.put("ucPU.hibernate.implicit_naming_strategy", ucPUHibernateNamingImplicitStrategy);
        if (!ucPUHibernateNamingPhysicalStrategy.startsWith("${")) persistenceUnitProperties.put("ucPU.hibernate.physical_naming_strategy", ucPUHibernateNamingPhysicalStrategy);

        if (!ucCMSStorageHibernateHbm2ddlAuto.startsWith("${")) persistenceUnitProperties.put("ucCMSStorage.hibernate.hbm2ddl.auto", ucCMSStorageHibernateHbm2ddlAuto);
        if (!ucCMSStorageHibernateDialect.startsWith("${")) persistenceUnitProperties.put("ucCMSStorage.hibernate.dialect", ucCMSStorageHibernateDialect);
        if (!ucCMSStorageHibernateShow_sql.startsWith("${")) persistenceUnitProperties.put("ucCMSStorage.hibernate.show_sql", ucCMSStorageHibernateShow_sql);
        if (!ucCMSStorageHibernateCacheUse_second_level_cache.startsWith("${")) persistenceUnitProperties.put("ucCMSStorage.hibernate.cache.use_second_level_cache", ucCMSStorageHibernateCacheUse_second_level_cache);
        if (!ucCMSStorageHibernateCacheUse_query_cache.startsWith("${")) persistenceUnitProperties.put("ucCMSStorage.hibernate.cache.use_query_cache", ucCMSStorageHibernateCacheUse_query_cache);
        if (!ucCMSStorageHibernateHbm2ddlImport_files.startsWith("${")) persistenceUnitProperties.put("ucCMSStorage.hibernate.hbm2ddl.import_files", ucCMSStorageHibernateHbm2ddlImport_files);
        if (!ucCMSStorageHibernateHbm2ddlImport_files_sql_extractor.startsWith("${")) persistenceUnitProperties.put("ucCMSStorage.hibernate.hbm2ddl.import_files_sql_extractor", ucCMSStorageHibernateHbm2ddlImport_files_sql_extractor);
        if (!ucCMSStorageHibernateNamingImplicitStrategy.startsWith("${")) persistenceUnitProperties.put("ucCMSStorage.hibernate.implicit_naming_strategy", ucCMSStorageHibernateNamingImplicitStrategy);
        if (!ucCMSStorageHibernateNamingPhysicalStrategy.startsWith("${")) persistenceUnitProperties.put("ucCMSStorage.hibernate.physical_naming_strategy", ucCMSStorageHibernateNamingPhysicalStrategy);

        if (!ucSecurePUHibernateHbm2ddlAuto.startsWith("${")) persistenceUnitProperties.put("ucSecurePU.hibernate.hbm2ddl.auto", ucSecurePUHibernateHbm2ddlAuto);
        if (!ucSecurePUHibernateDialect.startsWith("${")) persistenceUnitProperties.put("ucSecurePU.hibernate.dialect", ucSecurePUHibernateDialect);
        if (!ucSecurePUHibernateShow_sql.startsWith("${")) persistenceUnitProperties.put("ucSecurePU.hibernate.show_sql", ucSecurePUHibernateShow_sql);
        if (!ucSecurePUHibernateCacheUse_second_level_cache.startsWith("${")) persistenceUnitProperties.put("ucSecurePU.hibernate.cache.use_second_level_cache", ucSecurePUHibernateCacheUse_second_level_cache);
        if (!ucSecurePUHibernateCacheUse_query_cache.startsWith("${")) persistenceUnitProperties.put("ucSecurePU.hibernate.cache.use_query_cache", ucSecurePUHibernateCacheUse_query_cache);
        if (!ucSecurePUHibernateHbm2ddlImport_files.startsWith("${")) persistenceUnitProperties.put("ucSecurePU.hibernate.hbm2ddl.import_files", ucSecurePUHibernateHbm2ddlImport_files);
        if (!ucSecurePUHibernateHbm2ddlImport_files_sql_extractor.startsWith("${")) persistenceUnitProperties.put("ucSecurePU.hibernate.hbm2ddl.import_files_sql_extractor", ucSecurePUHibernateHbm2ddlImport_files_sql_extractor);
        if (!ucSecurePUHibernateNamingImplicitStrategy.startsWith("${")) persistenceUnitProperties.put("ucSecurePU.hibernate.implicit_naming_strategy", ucSecurePUHibernateNamingImplicitStrategy);
        if (!ucSecurePUHibernateNamingPhysicalStrategy.startsWith("${")) persistenceUnitProperties.put("ucSecurePU.hibernate.physical_naming_strategy", ucSecurePUHibernateNamingPhysicalStrategy);

        persistenceUnitProperties.putAll(overrideProperties);
    }
    
    @Override
    public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
        if (persistenceUnitProperties != null) {
            String puName = pui.getPersistenceUnitName() + ".";
            Set<String> keys = persistenceUnitProperties.keySet();
            Properties props = pui.getProperties();

            for (String key : keys) {
                if (key.startsWith(puName)){
                    String value = persistenceUnitProperties.get(key);
                    String newKey = key.substring(puName.length());
                    if ("null".equalsIgnoreCase(value)){
                        props.remove(newKey);
                    } else if (value != null && ! "".equals(value)) {
                        props.put(newKey, value);
                    }
                }
            }
            pui.setProperties(props);
        }
    }
    
    public void setPersistenceUnitProperties(Map<String, String> properties) {
        this.overrideProperties = properties;
    }
}
