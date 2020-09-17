/*
 * #%L
 * UltraCommerce CMS Module
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
package com.ultracommerce.cms.demo;

import com.ultracommerce.common.demo.AutoImportPersistenceUnit;
import com.ultracommerce.common.demo.AutoImportSql;
import com.ultracommerce.common.demo.AutoImportStage;
import com.ultracommerce.common.demo.DemoCondition;
import com.ultracommerce.common.demo.ImportCondition;
import com.ultracommerce.common.demo.MTCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jeff Fischer
 */
@Configuration("ucCMSData")
@Conditional(ImportCondition.class)
public class ImportSQLConfig {

    public static final int BASIC_DATA_SPECIAL = AutoImportStage.PRIMARY_PRE_BASIC_DATA + 200;

    @Bean
    public AutoImportSql ucLocaleData() {
        return new AutoImportSql(AutoImportPersistenceUnit.UL_PU,"config/bc/sql/demo/load_locale.sql", BASIC_DATA_SPECIAL);
    }

    @Bean
    @Conditional(DemoCondition.class)
    public AutoImportSql ucCMSBasicData() {
        return new AutoImportSql(AutoImportPersistenceUnit.UL_PU,"config/bc/sql/demo/load_content_structure.sql," +
                "config/bc/sql/demo/load_content_data.sql,config/bc/sql/demo/load_content_structure_i18n.sql," +
                "config/bc/sql/demo/load_content_data_i18n.sql", BASIC_DATA_SPECIAL);
    }

    @Bean
    @Conditional({MTCondition.class, DemoCondition.class})
    public AutoImportSql ucCMSLateData() {
        return new AutoImportSql(AutoImportPersistenceUnit.UL_PU,"config/bc/sql/demo/fix_static_asset_data.sql", AutoImportStage.PRIMARY_LATE);
    }
}
