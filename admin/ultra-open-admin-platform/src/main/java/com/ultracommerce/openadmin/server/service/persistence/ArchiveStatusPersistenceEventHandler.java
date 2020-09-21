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
package com.ultracommerce.openadmin.server.service.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.persistence.Status;
import com.ultracommerce.common.presentation.client.PersistencePerspectiveItemType;
import com.ultracommerce.openadmin.dto.AdornedTargetList;
import com.ultracommerce.openadmin.dto.CriteriaTransferObject;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import com.ultracommerce.openadmin.server.service.persistence.extension.ArchiveStatusPersistenceEventHandlerExtensionManager;
import com.ultracommerce.openadmin.server.service.persistence.module.EmptyFilterValues;
import com.ultracommerce.openadmin.server.service.persistence.module.criteria.FieldPath;
import com.ultracommerce.openadmin.server.service.persistence.module.criteria.FieldPathBuilder;
import com.ultracommerce.openadmin.server.service.persistence.module.criteria.FilterMapping;
import com.ultracommerce.openadmin.server.service.persistence.module.criteria.Restriction;
import com.ultracommerce.openadmin.server.service.persistence.module.criteria.predicate.PredicateProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

/**
 * Adds {@link FilterMapping} to the {@link CriteriaTransferObject}'s {@link CriteriaTransferObject#getAdditionalFilterMappings()}
 * in order to exclude by default any entities that are archived.
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("ucArchiveStatusPersistenceEventHandler")
public class ArchiveStatusPersistenceEventHandler extends PersistenceManagerEventHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(ArchiveStatusPersistenceEventHandler.class);

    @Resource(name = "ucArchiveStatusPersistenceEventHandlerExtensionManager")
    protected ArchiveStatusPersistenceEventHandlerExtensionManager extensionManager;
    
    @Override
    public PersistenceManagerEventHandlerResponse preFetch(PersistenceManager persistenceManager, PersistencePackage persistencePackage, CriteriaTransferObject cto) throws ServiceException {
        try {
            Class<?>[] entityClasses = persistenceManager.getDynamicEntityDao()
                    .getAllPolymorphicEntitiesFromCeiling(Class.forName(persistencePackage.getCeilingEntityFullyQualifiedClassname()));
            AtomicBoolean isArchivable = new AtomicBoolean(false);
            for (Class<?> entity : entityClasses) {
                AtomicBoolean test = new AtomicBoolean(true);
                extensionManager.getProxy().isArchivable(entity, test);
                if (!test.get()) {
                    isArchivable.set(false);
                    break;
                }

                if (Status.class.isAssignableFrom(entity)) {
                    isArchivable.set(true);
                }
            }
            if (isArchivable.get() && !persistencePackage.getPersistencePerspective().getShowArchivedFields()) {
                String targetPropertyName = "archiveStatus.archived";
                if (persistencePackage.getPersistencePerspectiveItems().containsKey(PersistencePerspectiveItemType.ADORNEDTARGETLIST)) {
                    AdornedTargetList atl = (AdornedTargetList) persistencePackage.getPersistencePerspectiveItems().get(PersistencePerspectiveItemType.ADORNEDTARGETLIST);
                    targetPropertyName = atl.getTargetObjectPath() + "." + targetPropertyName;
                }
                FilterMapping filterMapping = new FilterMapping()
                    .withFieldPath(new FieldPath().withTargetProperty(targetPropertyName))
                    .withDirectFilterValues(new EmptyFilterValues())
                    .withRestriction(new Restriction()
                            .withPredicateProvider(new PredicateProvider<Character, Character>() {
                                @Override
                                public Predicate buildPredicate(CriteriaBuilder builder,
                                                                FieldPathBuilder fieldPathBuilder,
                                                                From root, String ceilingEntity,
                                                                String fullPropertyName, Path<Character> explicitPath,
                                                                List<Character> directValues) {
                                    return builder.or(builder.equal(explicitPath, 'N'), builder.isNull(explicitPath));
                                }
                            })
                    );
                cto.getAdditionalFilterMappings().add(filterMapping);
            }
            return new PersistenceManagerEventHandlerResponse().
                    withStatus(PersistenceManagerEventHandlerResponse.PersistenceManagerEventHandlerResponseStatus.HANDLED);
        } catch (ClassNotFoundException e) {
            LOG.error("Could not find the class " + persistencePackage.getCeilingEntityFullyQualifiedClassname() + " to "
                    + "compute polymorphic entity types for. Assuming that the entity is not archivable");
            return new PersistenceManagerEventHandlerResponse().
                    withStatus(PersistenceManagerEventHandlerResponse.PersistenceManagerEventHandlerResponseStatus.NOT_HANDLED);
        }
    }
    
    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
    
}
