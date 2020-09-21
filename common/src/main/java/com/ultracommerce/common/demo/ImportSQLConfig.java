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
package com.ultracommerce.common.demo;

import com.ultracommerce.common.condition.ConditionalOnUltraModule;
import com.ultracommerce.common.module.UltraModuleRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jeff Fischer
 */
@Configuration("ucCommonData")
@Conditional(ImportCondition.class)
public class ImportSQLConfig {

    @Bean
    @Conditional(DemoCondition.class)
    public AutoImportSql ucCommonPreBasicData() {
        return new AutoImportSql(AutoImportPersistenceUnit.UL_PU,"config/bc/sql/demo/load_i18n_countries.sql", AutoImportStage.PRIMARY_PRE_BASIC_DATA);
    }

    @Bean
    public AutoImportSql ucCommonBasicData() {
        return new AutoImportSql(AutoImportPersistenceUnit.UL_PU,"config/bc/sql/demo/load_admin_users.sql,config/bc/sql/demo/load_sitemap_data.sql", AutoImportStage.PRIMARY_BASIC_DATA);
    }

    @Bean
    @ConditionalOnUltraModule(UltraModuleRegistration.UltraModuleEnum.MULTI_TENANT_SINGLE_SCHEMA)
    public AutoImportSql ucCommonLateData() {
        return new AutoImportSql(AutoImportPersistenceUnit.UL_PU,"config/bc/sql/demo/fix_admin_user_data.sql,config/bc/sql/demo/fix_system_property_data.sql", AutoImportStage.PRIMARY_LATE);
    }

}
