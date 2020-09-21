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
package com.ultracommerce.cms.admin.server.handler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.cms.field.domain.FieldDefinition;
import com.ultracommerce.cms.field.domain.FieldDefinitionImpl;
import com.ultracommerce.cms.field.domain.FieldGroup;
import com.ultracommerce.cms.structure.domain.StructuredContent;
import com.ultracommerce.cms.structure.domain.StructuredContentField;
import com.ultracommerce.cms.structure.domain.StructuredContentFieldImpl;
import com.ultracommerce.cms.structure.domain.StructuredContentFieldXref;
import com.ultracommerce.cms.structure.domain.StructuredContentFieldXrefImpl;
import com.ultracommerce.cms.structure.domain.StructuredContentType;
import com.ultracommerce.cms.structure.domain.StructuredContentTypeImpl;
import com.ultracommerce.cms.structure.service.StructuredContentService;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.presentation.client.SupportedFieldType;
import com.ultracommerce.openadmin.dto.ClassMetadata;
import com.ultracommerce.openadmin.dto.ClassTree;
import com.ultracommerce.openadmin.dto.CriteriaTransferObject;
import com.ultracommerce.openadmin.dto.DynamicResultSet;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import com.ultracommerce.openadmin.dto.Property;
import com.ultracommerce.openadmin.server.dao.DynamicEntityDao;
import com.ultracommerce.openadmin.server.service.AdminEntityService;
import com.ultracommerce.openadmin.server.service.ValidationException;
import com.ultracommerce.openadmin.server.service.handler.CustomPersistenceHandlerAdapter;
import com.ultracommerce.openadmin.server.service.handler.DynamicEntityRetriever;
import com.ultracommerce.openadmin.server.service.persistence.module.InspectHelper;
import com.ultracommerce.openadmin.server.service.persistence.module.RecordHelper;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by jfischer
 */
@Component("ucStructuredContentTypeCustomPersistenceHandler")
public class StructuredContentTypeCustomPersistenceHandler extends CustomPersistenceHandlerAdapter implements DynamicEntityRetriever {

    private final Log LOG = LogFactory.getLog(StructuredContentTypeCustomPersistenceHandler.class);

    @Resource(name="ucStructuredContentService")
    protected StructuredContentService structuredContentService;

    @Resource(name = "ucDynamicFieldPersistenceHandlerHelper")
    protected DynamicFieldPersistenceHandlerHelper dynamicFieldUtil;

    @PersistenceContext(unitName="ucPU")
    protected EntityManager em;

    @Resource(name = "ucAdminEntityService")
    protected AdminEntityService service;

    @Override
    public Boolean canHandleFetch(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        return
            StructuredContentType.class.getName().equals(ceilingEntityFullyQualifiedClassname) &&
            persistencePackage.getCustomCriteria() != null &&
            persistencePackage.getCustomCriteria().length > 0 &&
            persistencePackage.getCustomCriteria()[0].equals("constructForm");
    }

    @Override
    public Boolean canHandleAdd(PersistencePackage persistencePackage) {
        return canHandleFetch(persistencePackage);
    }

    @Override
    public Boolean canHandleInspect(PersistencePackage persistencePackage) {
        return canHandleFetch(persistencePackage);
    }

    @Override
    public Boolean canHandleRemove(PersistencePackage persistencePackage) {
        return false;
    }

    @Override
    public Boolean canHandleUpdate(PersistencePackage persistencePackage) {
        return canHandleFetch(persistencePackage);
    }

    @Override
    public DynamicResultSet inspect(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, InspectHelper helper) throws ServiceException {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        try {
            String structuredContentTypeId = persistencePackage.getCustomCriteria()[3];
            StructuredContentType structuredContentType = structuredContentService.findStructuredContentTypeById(Long.valueOf(structuredContentTypeId));
            ClassMetadata metadata = new ClassMetadata();
            metadata.setCeilingType(StructuredContentType.class.getName());
            ClassTree entities = new ClassTree(StructuredContentTypeImpl.class.getName());
            metadata.setPolymorphicEntities(entities);
            Property[] properties = dynamicFieldUtil.buildDynamicPropertyList(structuredContentType.getStructuredContentFieldTemplate().getFieldGroups(), StructuredContentTypeImpl.class);
            metadata.setProperties(properties);
            DynamicResultSet results = new DynamicResultSet(metadata);

            return results;
        } catch (Exception e) {
            throw new ServiceException("Unable to perform inspect for entity: "+ceilingEntityFullyQualifiedClassname, e);
        }
    }


    @Override
    public DynamicResultSet fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        try {
            String structuredContentId = persistencePackage.getCustomCriteria()[1];
            Entity entity = fetchEntityBasedOnId(structuredContentId, null);
            DynamicResultSet results = new DynamicResultSet(new Entity[]{entity}, 1);

            return results;
        } catch (Exception e) {
            throw new ServiceException("Unable to perform fetch for entity: "+ceilingEntityFullyQualifiedClassname, e);
        }
    }

    @Override
    public Entity fetchEntityBasedOnId(String structuredContentId, List<String> dirtyFields) throws Exception {
        StructuredContent structuredContent = structuredContentService.findStructuredContentById(Long.valueOf(structuredContentId));
        //Make sure the fieldmap is refreshed from the database based on any changes introduced in addOrUpdate()
        em.refresh(structuredContent);
        return fetchDynamicEntity(structuredContent, dirtyFields, true);
    }

    @Override
    public String getFieldContainerClassName() {
        return StructuredContent.class.getName();
    }

    @Override
    public Entity fetchDynamicEntity(Serializable root, List<String> dirtyFields, boolean includeId) throws Exception {
        StructuredContent structuredContent = (StructuredContent) root;
        Map<String, StructuredContentFieldXref> structuredContentFieldMap = structuredContent.getStructuredContentFieldXrefs();
        Entity entity = new Entity();
        entity.setType(new String[]{StructuredContentType.class.getName()});
        List<Property> propertiesList = new ArrayList<Property>();
        for (FieldGroup fieldGroup : structuredContent.getStructuredContentType().getStructuredContentFieldTemplate().getFieldGroups()) {
            for (FieldDefinition def : fieldGroup.getFieldDefinitions()) {
                Property property = new Property();
                property.setName(def.getName());
                String value = null;
                if (!MapUtils.isEmpty(structuredContentFieldMap)) {
                    StructuredContentFieldXref structuredContentFieldXref = structuredContentFieldMap.get(def.getName());
                    if (structuredContentFieldXref != null) {
                        StructuredContentField structuredContentField = structuredContentFieldXref.getStructuredContentField();
                        if (structuredContentField != null) {
                            value = structuredContentField.getValue();
                        }
                    }
                }
                property.setValue(value);
                if (!CollectionUtils.isEmpty(dirtyFields) && dirtyFields.contains(property.getName())) {
                    property.setIsDirty(true);
                }

                if (SupportedFieldType.ADDITIONAL_FOREIGN_KEY.equals(def.getFieldType()) && StringUtils.isNotEmpty(value)) {
                    // we need to look up the display value
                    String display = service.getForeignEntityName(def.getAdditionalForeignKeyClass(), value);
                    property.setDisplayValue(display);
                }
                propertiesList.add(property);
            }
        }
        if (includeId) {
            Property property = new Property();
            propertiesList.add(property);
            property.setName("id");
            property.setValue(String.valueOf(structuredContent.getId()));
        }

        entity.setProperties(propertiesList.toArray(new Property[]{}));

        return entity;
    }

    /**
     * Invoked when {@link StructuredContent} is saved in order to fill out the dynamic form for the structured content type
     */
    @Override
    public Entity update(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        return addOrUpdate(persistencePackage, dynamicEntityDao, helper);
    }

    /**
     * Invoked when {@link StructuredContent} is saved in order to fill out the dynamic form for the structured content type
     */
    @Override
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        return addOrUpdate(persistencePackage, dynamicEntityDao, helper);
    }

    protected Entity addOrUpdate(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        try {
            String structuredContentId = persistencePackage.getCustomCriteria()[1];
            StructuredContent structuredContent = structuredContentService.findStructuredContentById(Long.valueOf(structuredContentId));

            Property[] properties = dynamicFieldUtil.buildDynamicPropertyList(structuredContent.getStructuredContentType().getStructuredContentFieldTemplate().getFieldGroups(), StructuredContentType.class);
            Map<String, FieldMetadata> md = new HashMap<String, FieldMetadata>();
            for (Property property : properties) {
                md.put(property.getName(), property.getMetadata());
            }

            boolean validated = helper.validate(persistencePackage.getEntity(), new StructuredContentTypeImpl(), md);
            if (!validated) {
                throw new ValidationException(persistencePackage.getEntity(), "Structured Content dynamic fields failed validation");
            }

            List<String> templateFieldNames = new ArrayList<String>(20);
            for (FieldGroup group : structuredContent.getStructuredContentType().getStructuredContentFieldTemplate().getFieldGroups()) {
                for (FieldDefinition def : group.getFieldDefinitions()) {
                    templateFieldNames.add(def.getName());
                }
            }
            Map<String, String> dirtyFieldsOrigVals = new HashMap<String, String>();
            List<String> dirtyFields = new ArrayList<String>();
            Map<String, StructuredContentFieldXref> structuredContentFieldMap =
                    structuredContent.getStructuredContentFieldXrefs();
            for (Property property : persistencePackage.getEntity().getProperties()) {
                if (property.getEnabled() && templateFieldNames.contains(property.getName())) {
                    StructuredContentFieldXref scXref = structuredContentFieldMap.get(property.getName());
                    if (scXref != null && scXref.getStructuredContentField() != null) {
                        StructuredContentField structuredContentField = scXref.getStructuredContentField();
                        boolean isDirty = (structuredContentField.getValue() == null && property.getValue() != null) ||
                                (structuredContentField.getValue() != null && property.getValue() == null);
                        if (isDirty || (structuredContentField.getValue() != null && property.getValue() != null &&
                                !structuredContentField.getValue().trim().equals(property.getValue().trim()))) {
                            dirtyFields.add(property.getName());
                            dirtyFieldsOrigVals.put(property.getName(), structuredContentField.getValue());
                            structuredContentField.setValue(property.getValue());
                            scXref = dynamicEntityDao.merge(scXref);
                        }
                    } else {
                        StructuredContentField structuredContentField = new StructuredContentFieldImpl();
                        structuredContentField.setFieldKey(property.getName());
                        structuredContentField.setValue(property.getValue());

                        StructuredContentFieldXref scfx = new StructuredContentFieldXrefImpl();
                        scfx.setStructuredContent(structuredContent);
                        scfx.setKey(property.getName());
                        scfx.setStrucuturedContentField(structuredContentField);

                        scfx = dynamicEntityDao.persist(scfx);
                        dirtyFields.add(property.getName());
                    }
                }
            }
            List<String> removeItems = new ArrayList<String>();
            for (String key : structuredContentFieldMap.keySet()) {
                if (persistencePackage.getEntity().findProperty(key)==null) {
                    removeItems.add(key);
                }
            }
            if (removeItems.size() > 0) {
                for (String removeKey : removeItems) {
                    structuredContentFieldMap.remove(removeKey);
                }
            }

            Collections.sort(dirtyFields);
            Entity entity = fetchEntityBasedOnId(structuredContentId, dirtyFields);

            for (Entry<String, String> entry : dirtyFieldsOrigVals.entrySet()) {
                entity.getPMap().get(entry.getKey()).setOriginalValue(entry.getValue());
                entity.getPMap().get(entry.getKey()).setOriginalDisplayValue(entry.getValue());
            }

            return entity;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Unable to perform fetch for entity: "+ceilingEntityFullyQualifiedClassname, e);
        }
    }
}
