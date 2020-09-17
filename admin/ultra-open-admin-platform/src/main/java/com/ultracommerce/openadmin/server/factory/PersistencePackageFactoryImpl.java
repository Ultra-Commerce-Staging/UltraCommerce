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
package com.ultracommerce.openadmin.server.factory;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import com.ultracommerce.common.exception.ExceptionHelper;
import com.ultracommerce.common.presentation.client.OperationType;
import com.ultracommerce.common.presentation.client.PersistencePerspectiveItemType;
import com.ultracommerce.common.util.dao.DynamicDaoHelper;
import com.ultracommerce.common.util.dao.DynamicDaoHelperImpl;
import com.ultracommerce.openadmin.dto.OperationTypes;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import com.ultracommerce.openadmin.dto.PersistencePerspective;
import com.ultracommerce.openadmin.dto.SectionCrumb;
import com.ultracommerce.openadmin.server.domain.PersistencePackageRequest;
import com.ultracommerce.openadmin.server.security.domain.AdminSection;
import com.ultracommerce.openadmin.server.security.service.navigation.AdminNavigationService;
import com.ultracommerce.openadmin.server.service.persistence.PersistenceManager;
import com.ultracommerce.openadmin.server.service.persistence.PersistenceManagerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

/**
 * @author Andre Azzolini (apazzolini)
 */
@Service("ucPersistencePackageFactory")
public class PersistencePackageFactoryImpl implements PersistencePackageFactory {

    @Resource(name = "ucAdminNavigationService")
    protected AdminNavigationService adminNavigationService;

    protected DynamicDaoHelper dynamicDaoHelper = new DynamicDaoHelperImpl();

    @Override
    public PersistencePackage create(PersistencePackageRequest request) {
        PersistencePerspective persistencePerspective = new PersistencePerspective();

        persistencePerspective.setAdditionalForeignKeys(request.getAdditionalForeignKeys());
        persistencePerspective.setAdditionalNonPersistentProperties(new String[] {});

        if (request.getForeignKey() != null) {
            persistencePerspective.addPersistencePerspectiveItem(PersistencePerspectiveItemType.FOREIGNKEY,
                    request.getForeignKey());
        }

        switch (request.getType()) {
            case STANDARD:
                persistencePerspective.setOperationTypes(getDefaultOperationTypes());
                break;

            case ADORNED:
                if (request.getAdornedList() == null) {
                    throw new IllegalArgumentException("ADORNED type requires the adornedList to be set");
                }

                persistencePerspective.setOperationTypes(getOperationTypes(OperationType.ADORNEDTARGETLIST));
                persistencePerspective.addPersistencePerspectiveItem(PersistencePerspectiveItemType.ADORNEDTARGETLIST,
                        request.getAdornedList());
                break;

            case MAP:
                if (request.getMapStructure() == null) {
                    throw new IllegalArgumentException("MAP type requires the mapStructure to be set");
                }

                persistencePerspective.setOperationTypes(getOperationTypes(OperationType.MAP));
                persistencePerspective.addPersistencePerspectiveItem(PersistencePerspectiveItemType.MAPSTRUCTURE,
                        request.getMapStructure());
                break;
        }

        if (request.getOperationTypesOverride() != null) {
            persistencePerspective.setOperationTypes(request.getOperationTypesOverride());
        }

        PersistencePackage pp = new PersistencePackage();
        pp.setCeilingEntityFullyQualifiedClassname(request.getCeilingEntityClassname());
        if (!StringUtils.isEmpty(request.getSecurityCeilingEntityClassname())) {
            pp.setSecurityCeilingEntityFullyQualifiedClassname(request.getSecurityCeilingEntityClassname());
        }
        if (!ArrayUtils.isEmpty(request.getSectionCrumbs())) {
            SectionCrumb[] converted = new SectionCrumb[request.getSectionCrumbs().length];
            int index = 0;
            for (SectionCrumb crumb : request.getSectionCrumbs()) {
                SectionCrumb temp = new SectionCrumb();
                String originalSectionIdentifier = crumb.getSectionIdentifier();
                String sectionAsClassName;
                try {
                    sectionAsClassName = getClassNameForSection(crumb.getSectionIdentifier());
                } catch (Exception e) {
                    sectionAsClassName = request.getCeilingEntityClassname();
                }
                if (sectionAsClassName != null && !sectionAsClassName.equals(originalSectionIdentifier)) {
                    temp.setOriginalSectionIdentifier(originalSectionIdentifier);
                }
                temp.setSectionIdentifier(sectionAsClassName);

                temp.setSectionId(crumb.getSectionId());
                converted[index] = temp;
                index++;
            }
            pp.setSectionCrumbs(converted);
        } else {
            pp.setSectionCrumbs(new SectionCrumb[0]);
        }
        pp.setSectionEntityField(request.getSectionEntityField());
        pp.setFetchTypeFullyQualifiedClassname(null);
        pp.setPersistencePerspective(persistencePerspective);
        pp.setCustomCriteria(request.getCustomCriteria());
        pp.setCsrfToken(null);
        pp.setRequestingEntityName(request.getRequestingEntityName());
        pp.setValidateUnsubmittedProperties(request.isValidateUnsubmittedProperties());
        pp.setIsTreeCollection(request.isTreeCollection());
        pp.setAddOperationInspect(request.isAddOperationInspect());

        if (request.getEntity() != null) {
            pp.setEntity(request.getEntity());
        }

        for (Map.Entry<String, PersistencePackageRequest> subRequest : request.getSubRequests().entrySet()) {
            pp.getSubPackages().put(subRequest.getKey(), create(subRequest.getValue()));
        }

        return pp;
    }

    protected OperationTypes getDefaultOperationTypes() {
        OperationTypes operationTypes = new OperationTypes();
        operationTypes.setFetchType(OperationType.BASIC);
        operationTypes.setRemoveType(OperationType.BASIC);
        operationTypes.setAddType(OperationType.BASIC);
        operationTypes.setUpdateType(OperationType.BASIC);
        operationTypes.setInspectType(OperationType.BASIC);
        return operationTypes;
    }

    protected OperationTypes getOperationTypes(OperationType nonInspectOperationType) {
        OperationTypes operationTypes = new OperationTypes();
        operationTypes.setFetchType(nonInspectOperationType);
        operationTypes.setRemoveType(nonInspectOperationType);
        operationTypes.setAddType(nonInspectOperationType);
        operationTypes.setUpdateType(nonInspectOperationType);
        operationTypes.setInspectType(OperationType.BASIC);
        return operationTypes;
    }

    protected String getClassNameForSection(String sectionKey) {
        try {
            AdminSection section = adminNavigationService.findAdminSectionByURI("/" + sectionKey);
            String className = (section == null) ? sectionKey : section.getCeilingEntity();

            if (className == null) {
                throw new RuntimeException("Could not determine the class related to the following Section: " + section.getName());
            }

            Class<?>[] entities = dynamicDaoHelper.getAllPolymorphicEntitiesFromCeiling(Class.forName(className), true, true);

            return entities[entities.length - 1].getName();
        } catch (ClassNotFoundException e) {
            throw ExceptionHelper.refineException(RuntimeException.class, RuntimeException.class, e);
        }
    }

    protected EntityManager getEntityManager(String className) {
        PersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager(className);
        return persistenceManager.getDynamicEntityDao().getStandardEntityManager();
    }
}
