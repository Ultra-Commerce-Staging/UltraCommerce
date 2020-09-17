/*
 * #%L
 * UltraCommerce Open Admin Platform
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
package com.ultracommerce.openadmin.server.security.service.navigation;

import org.apache.commons.lang.StringUtils;
import com.ultracommerce.openadmin.server.dao.DynamicEntityDao;
import com.ultracommerce.openadmin.server.security.domain.AdminSection;
import com.ultracommerce.openadmin.server.security.domain.AdminUser;
import com.ultracommerce.openadmin.server.service.persistence.PersistenceManagerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Jeff Fischer
 */
@Component("ucPolymorphicEntityCheckSectionAuthorization")
public class PolymorphicEntitySectionAuthorizationImpl implements SectionAuthorization {

    @Override
    public boolean isUserAuthorizedToViewSection(AdminUser adminUser, AdminSection section) {
        String ceilingEntity = section.getCeilingEntity();
        if (StringUtils.isBlank(ceilingEntity)) {
            return true;
        }

        try {
            DynamicEntityDao dynamicEntityDao = getDynamicEntityDao(ceilingEntity);

            //Only display this section if there are 1 or more entities relative to the ceiling
            //for this section that are qualified to be created by the admin
            return dynamicEntityDao.getAllPolymorphicEntitiesFromCeiling(
                    Class.forName(ceilingEntity), false).length > 0;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected DynamicEntityDao getDynamicEntityDao(String ceilingEntityName) {
        return PersistenceManagerFactory.getPersistenceManager(ceilingEntityName).getDynamicEntityDao();
    }

}
