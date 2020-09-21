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
package com.ultracommerce.core.util.service;

import com.ultracommerce.core.util.dao.CodeTypeDao;
import com.ultracommerce.core.util.domain.CodeType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

@Service("ucCodeTypeService")
@Deprecated
public class CodeTypeServiceImpl implements CodeTypeService {

    @Resource(name="ucCodeTypeDao")
    protected CodeTypeDao codeTypeDao;

    @Override
    @Transactional("ucTransactionManager")
    public void deleteCodeType(CodeType codeTypeId) {
        codeTypeDao.delete(codeTypeId);
    }

    @Override
    public List<CodeType> findAllCodeTypes() {
        return codeTypeDao.readAllCodeTypes();
    }

    @Override
    public CodeType lookupCodeTypeById(Long codeTypeId) {
        return codeTypeDao.readCodeTypeById(codeTypeId);
    }

    @Override
    public List<CodeType> lookupCodeTypeByKey(String key) {
        return codeTypeDao.readCodeTypeByKey(key);
    }

    @Override
    @Transactional("ucTransactionManager")
    public CodeType save(CodeType codeType) {
        return codeTypeDao.save(codeType);
    }

}
