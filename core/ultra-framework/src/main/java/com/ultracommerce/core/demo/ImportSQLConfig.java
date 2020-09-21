/*
 * #%L
 * UltraCommerce Framework
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
package com.ultracommerce.core.demo;

import com.ultracommerce.common.condition.ConditionalOnUltraModule;
import com.ultracommerce.common.demo.AutoImportPersistenceUnit;
import com.ultracommerce.common.demo.AutoImportSql;
import com.ultracommerce.common.demo.AutoImportStage;
import com.ultracommerce.common.demo.DemoCondition;
import com.ultracommerce.common.demo.ImportCondition;
import com.ultracommerce.common.demo.MTCondition;
import com.ultracommerce.common.module.UltraModuleRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;

/**
 * @author Jeff Fischer
 */
@Configuration("ucCoreData")
@Conditional(ImportCondition.class)
public class ImportSQLConfig {

    @Bean
    public AutoImportSql ucFrameworkSecurityData() {
        return new AutoImportSql(AutoImportPersistenceUnit.UL_PU,"config/bc/sql/load_admin_permissions.sql,config/bc/sql/load_admin_roles.sql,config/bc/sql/load_admin_menu.sql", AutoImportStage.PRIMARY_FRAMEWORK_SECURITY);
    }

    @Bean
    @ConditionalOnUltraModule(UltraModuleRegistration.UltraModuleEnum.ENTERPRISE)
    public AutoImportSql ucApproverOnlySecurityData() {
        return new AutoImportSql(AutoImportPersistenceUnit.UL_PU, "config/bc/sql/load_admin_approver_only_role.sql", AutoImportStage.PRIMARY_POST_MODULE_SECURITY);
    }

    @Bean
    @Conditional(DemoCondition.class)
    public AutoImportSql ucFrameworkPreBasicData() {
        return new AutoImportSql(AutoImportPersistenceUnit.UL_PU,"config/bc/sql/demo/load_catalog_data.sql,config/bc/sql/demo/load_catalog_i18n_data_ES.sql," +
                "config/bc/sql/demo/load_catalog_i18n_data_FR.sql", AutoImportStage.PRIMARY_PRE_BASIC_DATA);
    }

    @Bean
    @Conditional({MTCondition.class, DemoCondition.class})
    public AutoImportSql ucFrameworkLateData() {
        return new AutoImportSql(AutoImportPersistenceUnit.UL_PU,"config/bc/sql/demo/fix_catalog_data.sql", AutoImportStage.PRIMARY_LATE);
    }
    
    @Bean
    @Conditional({AssetFoldersExistCondition.class, DemoCondition.class})
    public AutoImportSql ucAssetFolderData() {
        return new AutoImportSql(AutoImportPersistenceUnit.UL_PU,"config/bc/sql/demo/populate_asset_folders.sql", AutoImportStage.PRIMARY_POST_BASIC_DATA);
    }

    @Bean
    @Conditional({AssetFoldersExistCondition.class, GiftCardAndCustomerCreditExistCondition.class, DemoCondition.class})
    public AutoImportSql ucAssetFolderGiftCardData() {
        return new AutoImportSql(AutoImportPersistenceUnit.UL_PU,"config/bc/sql/demo/populate_asset_folders_gift_cards.sql", AutoImportStage.PRIMARY_POST_BASIC_DATA);
    }
    
    public static class AssetFoldersExistCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return ClassUtils.isPresent("com.ultracommerce.enterprise.foldering.admin.domain.AssetFolder", context.getClassLoader());
        }
        
    }

    public static class GiftCardAndCustomerCreditExistCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return ClassUtils.isPresent("com.ultracommerce.accountcredit.profile.core.domain.GiftCardAccount", context.getClassLoader());
        }

    }
}
