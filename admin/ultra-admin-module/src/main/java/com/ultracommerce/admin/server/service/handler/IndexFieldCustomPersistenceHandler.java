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
package com.ultracommerce.admin.server.service.handler;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.persistence.Status;
import com.ultracommerce.common.presentation.client.OperationType;
import com.ultracommerce.core.search.domain.IndexField;
import com.ultracommerce.core.search.domain.IndexFieldType;
import com.ultracommerce.core.search.domain.IndexFieldTypeImpl;
import com.ultracommerce.core.search.domain.solr.FieldType;
import com.ultracommerce.openadmin.dto.CriteriaTransferObject;
import com.ultracommerce.openadmin.dto.DynamicResultSet;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.FilterAndSortCriteria;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import com.ultracommerce.openadmin.dto.PersistencePerspective;
import com.ultracommerce.openadmin.server.dao.DynamicEntityDao;
import com.ultracommerce.openadmin.server.service.handler.CustomPersistenceHandlerAdapter;
import com.ultracommerce.openadmin.server.service.persistence.module.RecordHelper;
import com.ultracommerce.openadmin.server.service.persistence.module.criteria.FieldPath;
import com.ultracommerce.openadmin.server.service.persistence.module.criteria.FieldPathBuilder;
import com.ultracommerce.openadmin.server.service.persistence.module.criteria.FilterMapping;
import com.ultracommerce.openadmin.server.service.persistence.module.criteria.Restriction;
import com.ultracommerce.openadmin.server.service.persistence.module.criteria.predicate.PredicateProvider;
import org.springframework.stereotype.Component;

/**
 * @author Chad Harchar (charchar)
 */
@Component("ucIndexFieldCustomPersistenceHandler")
public class IndexFieldCustomPersistenceHandler extends CustomPersistenceHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(IndexFieldCustomPersistenceHandler.class);

    @Resource(name = "ucIndexFieldCustomPersistenceHandlerExtensionManager")
    protected IndexFieldCustomPersistenceHandlerExtensionManager extensionManager;

    @Override
    public Boolean canHandleAdd(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        return IndexField.class.getName().equals(ceilingEntityFullyQualifiedClassname);
    }

    @Override
    public Boolean canHandleUpdate(PersistencePackage persistencePackage) {
        return canHandleAdd(persistencePackage);
    }

    @Override
    public Boolean canHandleRemove(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();

        if (IndexField.class.getName().equalsIgnoreCase(ceilingEntityFullyQualifiedClassname) ||
                IndexFieldTypeImpl.class.getName().equalsIgnoreCase(ceilingEntityFullyQualifiedClassname)) {
            return true;
        }
        return super.canHandleRemove(persistencePackage);
    }

    @Override
    public Boolean canHandleFetch(PersistencePackage persistencePackage) {
        return canHandleAdd(persistencePackage);
    }

    @Override
    public void remove(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        Entity entity = persistencePackage.getEntity();
        try {
            PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(IndexField.class.getName(), persistencePerspective);
            Object primaryKey = helper.getPrimaryKey(entity, adminProperties);
            Serializable instance = dynamicEntityDao.retrieve(Class.forName(entity.getType()[0]),primaryKey);
            if (instance instanceof Status) {
                ((Status)instance).setArchived('Y');
                dynamicEntityDao.merge(instance);
                return;
            }

        } catch (Exception ex) {
            throw new ServiceException("Unable to perform remove for entity: " + entity.getType()[0], ex);

        }
        super.remove(persistencePackage, dynamicEntityDao, helper);
    }

    @Override
    public Entity update(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        Entity entity = persistencePackage.getEntity();
        try {
            PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(IndexField.class.getName(), persistencePerspective);
            Object primaryKey = helper.getPrimaryKey(entity, adminProperties);
            IndexField adminInstance = (IndexField) dynamicEntityDao.retrieve(Class.forName(entity.getType()[0]), primaryKey);

            return getEntity(persistencePackage, dynamicEntityDao, helper, entity, adminProperties, adminInstance);
        } catch (Exception e) {
            throw new ServiceException("Unable to perform update for entity: " + IndexField.class.getName(), e);
        }
    }

    protected Entity getEntity(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper, Entity entity, Map<String, FieldMetadata> adminProperties, IndexField adminInstance) throws ServiceException {
        adminInstance = (IndexField) helper.createPopulatedInstance(adminInstance, entity, adminProperties, false);
        adminInstance = dynamicEntityDao.merge(adminInstance);

        ExtensionResultStatusType result = ExtensionResultStatusType.NOT_HANDLED;
        if (extensionManager != null) {
             result = extensionManager.getProxy().addtoSearchableFields(persistencePackage, adminInstance);
        }

        if (result.equals(ExtensionResultStatusType.NOT_HANDLED)) {
            // If there is no searchable field types then we need to add a default as String
            if (CollectionUtils.isEmpty(adminInstance.getFieldTypes())) {
                IndexFieldType indexFieldType = new IndexFieldTypeImpl();
                indexFieldType.setFieldType(FieldType.TEXT);
                indexFieldType.setIndexField(adminInstance);
                adminInstance.getFieldTypes().add(indexFieldType);
                adminInstance = dynamicEntityDao.merge(adminInstance);
            }
        }

        Entity adminEntity = helper.getRecord(adminProperties, adminInstance, null, null);

        return adminEntity;
    }

    @Override
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        Entity entity = persistencePackage.getEntity();
        try {
            PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
            IndexField adminInstance = (IndexField) Class.forName(entity.getType()[0]).newInstance();
            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(IndexField.class.getName(), persistencePerspective);
            return getEntity(persistencePackage, dynamicEntityDao, helper, entity, adminProperties, adminInstance);
        } catch (Exception e) {
            throw new ServiceException("Unable to perform add for entity: " + IndexField.class.getName(), e);
        }
    }

    @Override
    public DynamicResultSet fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto,
                                  DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {

        FilterAndSortCriteria fieldFsc = cto.getCriteriaMap().get("field");
        if (fieldFsc != null) {
            List<String> filterValues = fieldFsc.getFilterValues();
            cto.getCriteriaMap().remove("field");
            cto.getAdditionalFilterMappings().add(new FilterMapping()
                .withFieldPath(new FieldPath().withTargetProperty("field.friendlyName"))
                .withFilterValues(filterValues)
                .withSortDirection(fieldFsc.getSortDirection())
                .withRestriction(new Restriction()
                    .withPredicateProvider(new PredicateProvider() {
                        @Override
                        public Predicate buildPredicate(CriteriaBuilder builder, FieldPathBuilder fieldPathBuilder, From root,
                                                        String ceilingEntity, String fullPropertyName, Path explicitPath, List directValues) {
                            return builder.like(explicitPath.as(String.class), "%" + directValues.get(0) + "%");
                        }
                    })
                ));
        }

        DynamicResultSet resultSet = helper.getCompatibleModule(OperationType.BASIC).fetch(persistencePackage, cto);
        return resultSet;
    }

}
