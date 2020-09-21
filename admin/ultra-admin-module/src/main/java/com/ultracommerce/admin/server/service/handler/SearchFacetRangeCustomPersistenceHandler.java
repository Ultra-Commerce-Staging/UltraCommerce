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

package com.ultracommerce.admin.server.service.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.persistence.Status;
import com.ultracommerce.core.search.domain.SearchFacetRange;
import com.ultracommerce.core.search.domain.SearchFacetRangeImpl;
import com.ultracommerce.openadmin.dto.CriteriaTransferObject;
import com.ultracommerce.openadmin.dto.DynamicResultSet;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.FilterAndSortCriteria;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import com.ultracommerce.openadmin.dto.PersistencePerspective;
import com.ultracommerce.openadmin.server.dao.DynamicEntityDao;
import com.ultracommerce.openadmin.server.service.handler.CustomPersistenceHandlerAdapter;
import com.ultracommerce.openadmin.server.service.persistence.module.PersistenceModule;
import com.ultracommerce.openadmin.server.service.persistence.module.RecordHelper;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author gdiaz
 */
@Component("ucSearchFacetRangeCustomPersistenceHandler")
public class SearchFacetRangeCustomPersistenceHandler extends CustomPersistenceHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(SearchFacetRangeCustomPersistenceHandler.class);

    private static String[] sortFields = new String[] { "embeddablePriceList.priceList", "minValue", "maxValue" };

    @Override
    public Boolean canHandleFetch(PersistencePackage persistencePackage) {
        return canHandle(persistencePackage);
    }

    @Override
    public Boolean canHandleRemove(PersistencePackage persistencePackage) {
        return canHandle(persistencePackage);
    }

    public Boolean canHandle(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        return SearchFacetRangeImpl.class.getName().equals(ceilingEntityFullyQualifiedClassname);
    }

    @Override
    public DynamicResultSet fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        addDefaultSort(cto);

        PersistenceModule myModule = helper.getCompatibleModule(persistencePackage.getPersistencePerspective().getOperationTypes().getFetchType());
        DynamicResultSet results = myModule.fetch(persistencePackage, cto);
        return results;
    }

    protected void addDefaultSort(CriteriaTransferObject cto) {
        boolean userSort = false;
        for (FilterAndSortCriteria fasc : cto.getCriteriaMap().values()) {
            if (fasc.getSortDirection() != null) {
                userSort = true;
                break;
            }
        }

        if (!userSort) {
            for (int i = 0; i < sortFields.length; i++) {
                String string = sortFields[i];
                FilterAndSortCriteria fsc = cto.getCriteriaMap().get(string);
                if (fsc == null) {
                    fsc = new FilterAndSortCriteria(string, i);
                    cto.add(fsc);
                }
                fsc.setSortAscending(true);
            }
        }
    }

    @Override
    public void remove(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper)
            throws ServiceException {
        Entity entity = persistencePackage.getEntity();

        try {
            SearchFacetRange adminInstance = getAdminInstance(persistencePackage, dynamicEntityDao, helper, entity);
            if (Status.class.isAssignableFrom(adminInstance.getClass())) {
                ((Status) adminInstance).setArchived('Y');
                dynamicEntityDao.merge(adminInstance);
            }
        } catch (Exception e) {
            throw new ServiceException("Unable to remove entity for " + entity.getType()[0], e);
        }
    }

    protected SearchFacetRange getAdminInstance(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper,
                                       Entity entity) throws ClassNotFoundException {
        PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
        Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(SearchFacetRange.class.getName(), persistencePerspective);
        Object primaryKey = helper.getPrimaryKey(entity, adminProperties);
        String type = entity.getType()[0];
        SearchFacetRange adminInstance = (SearchFacetRange) dynamicEntityDao.retrieve(Class.forName(type), primaryKey);

        return adminInstance;
    }
}
