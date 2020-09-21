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
package com.ultracommerce.common.enumeration.service;

import javax.annotation.Resource;

import com.ultracommerce.common.enumeration.dao.DataDrivenEnumerationDao;
import com.ultracommerce.common.enumeration.domain.DataDrivenEnumeration;
import com.ultracommerce.common.enumeration.domain.DataDrivenEnumerationValue;
import org.springframework.stereotype.Service;


@Service("ucDataDrivenEnumerationService")
public class DataDrivenEnumerationServiceImpl implements DataDrivenEnumerationService {

    @Resource(name = "ucDataDrivenEnumerationDao")
    protected DataDrivenEnumerationDao dao;

    @Override
    public DataDrivenEnumeration findEnumByKey(String enumKey) {
        return dao.readEnumByKey(enumKey);
    }
    
    @Override
    public DataDrivenEnumerationValue findEnumValueByKey(String enumKey, String enumValueKey) {
        return dao.readEnumValueByKey(enumKey, enumValueKey);
    }

}
