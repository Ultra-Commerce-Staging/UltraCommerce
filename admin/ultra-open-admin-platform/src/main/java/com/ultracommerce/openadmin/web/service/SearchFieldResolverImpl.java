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

package com.ultracommerce.openadmin.web.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.openadmin.dto.ClassMetadata;
import com.ultracommerce.openadmin.server.domain.PersistencePackageRequest;
import com.ultracommerce.openadmin.server.service.AdminEntityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @see SearchFieldResolver
 * @author Andre Azzolini (apazzolini)
 */
@Service("ucSearchFieldResolver")
public class SearchFieldResolverImpl implements SearchFieldResolver {
    protected static final Log LOG = LogFactory.getLog(SearchFieldResolverImpl.class);

    @Resource(name = "ucAdminEntityService")
    protected AdminEntityService service;
    
    @Override
    public String resolveField(String className) throws ServiceException {
        PersistencePackageRequest ppr = PersistencePackageRequest.standard()
                .withCeilingEntityClassname(className);
        ClassMetadata md = service.getClassMetadata(ppr).getDynamicResultSet().getClassMetaData();
        
        if (md.getPMap().containsKey("name")) {
            return "name";
        }

        if (md.getPMap().containsKey("friendlyName")) {
            return "friendlyName";
        }

        if (md.getPMap().containsKey("templateName")) {
            return "templateName";
        }
        
        return "id";
    }


}
