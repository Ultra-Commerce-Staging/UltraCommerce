/*
 * #%L
 * UltraCommerce Admin Module
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
package com.ultracommerce.admin.server.service.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.persistence.Status;
import com.ultracommerce.core.search.domain.SearchFacet;
import com.ultracommerce.core.search.domain.SearchFacetRange;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import com.ultracommerce.openadmin.dto.PersistencePerspective;
import com.ultracommerce.openadmin.server.dao.DynamicEntityDao;
import com.ultracommerce.openadmin.server.service.handler.CustomPersistenceHandlerAdapter;
import com.ultracommerce.openadmin.server.service.persistence.module.RecordHelper;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * When deleting a {@link SearchFacet}, we want to make sure it is a soft delete.
 *
 * @author Nathan Moore (nathandmoore)
 */
@Component("ucSearchFacetCustomPersistenceHandler")
public class SearchFacetCustomPersistenceHandler extends CustomPersistenceHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(SearchFacetCustomPersistenceHandler.class);

    @Override
    public Boolean canHandleRemove(PersistencePackage persistencePackage) {
        return persistencePackage.getCeilingEntityFullyQualifiedClassname() != null
                && persistencePackage.getCeilingEntityFullyQualifiedClassname().equals(SearchFacet.class.getName());
    }

    @Override
    public void remove(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper)
            throws ServiceException {
        Entity entity = persistencePackage.getEntity();

        try {
            SearchFacet adminInstance = getAdminInstance(persistencePackage, dynamicEntityDao, helper, entity);
            if (Status.class.isAssignableFrom(adminInstance.getClass())) {
                ((Status) adminInstance).setArchived('Y');
                dynamicEntityDao.merge(adminInstance);
            }
        } catch (Exception e) {
            throw new ServiceException("Unable to remove entity for " + entity.getType()[0], e);
        }
    }

    protected SearchFacet getAdminInstance(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper,
                                                Entity entity) throws ClassNotFoundException {
        PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
        Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(SearchFacetRange.class.getName(), persistencePerspective);
        Object primaryKey = helper.getPrimaryKey(entity, adminProperties);
        String type = entity.getType()[0];
        SearchFacet adminInstance = (SearchFacet) dynamicEntityDao.retrieve(Class.forName(type), primaryKey);

        return adminInstance;
    }
}
