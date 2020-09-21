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

package com.ultracommerce.openadmin.server.service.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ultracommerce.common.config.service.SystemPropertiesService;
import com.ultracommerce.common.exception.ServiceException;
import com.ultracommerce.common.i18n.domain.Translation;
import com.ultracommerce.common.i18n.service.TranslationService;
import com.ultracommerce.common.sandbox.SandBoxHelper;
import com.ultracommerce.openadmin.dto.Entity;
import com.ultracommerce.openadmin.dto.FieldMetadata;
import com.ultracommerce.openadmin.dto.PersistencePackage;
import com.ultracommerce.openadmin.dto.PersistencePerspective;
import com.ultracommerce.openadmin.server.dao.DynamicEntityDao;
import com.ultracommerce.openadmin.server.service.persistence.module.RecordHelper;
import org.springframework.stereotype.Component;

import java.util.Map;

import javax.annotation.Resource;

/**
 * Custom persistence handler for Translations, it verifies on "add" that the combination of the 4 "key" fields
 * is not repeated (as in a software-enforced unique index, which is not utilized because of sandboxing and multitenancy concerns).
 * 
 * @author gdiaz
 */
@Component("ucTranslationCustomPersistenceHandler")
public class TranslationCustomPersistenceHandler extends CustomPersistenceHandlerAdapter {

    private final Log LOG = LogFactory.getLog(TranslationCustomPersistenceHandler.class);

    @Resource(name = "ucSystemPropertiesService")
    protected SystemPropertiesService spService;

    @Resource(name = "ucTranslationService")
    protected TranslationService translationService;

    @Resource(name="ucSandBoxHelper")
    protected SandBoxHelper sandBoxHelper;

    protected Boolean classMatches(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        return Translation.class.getName().equals(ceilingEntityFullyQualifiedClassname);
    }

    @Override
    public Boolean canHandleAdd(PersistencePackage persistencePackage) {
        return classMatches(persistencePackage);
    }

    @Override
    public Boolean canHandleUpdate(PersistencePackage persistencePackage) {
        return false;
    }

    @Override
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        Entity entity = persistencePackage.getEntity();
        try {
            // Get an instance of SystemProperty with the updated values from the form
            PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
            Translation adminInstance = (Translation) Class.forName(entity.getType()[0]).newInstance();
            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(Translation.class.getName(), persistencePerspective);
            adminInstance = (Translation) helper.createPopulatedInstance(adminInstance, entity, adminProperties, false);

            // We only want to check for duplicates during a save
            if (!sandBoxHelper.isReplayOperation()) {
                Translation res = translationService.getTranslation(adminInstance.getEntityType(), adminInstance.getEntityId(), adminInstance.getFieldName(), adminInstance.getLocaleCode());
                if (res != null) {
                    Entity errorEntity = new Entity();
                    errorEntity.addValidationError("localeCode", "translation.record.exists.for.locale");
                    return errorEntity;
                }
            }
            adminInstance = dynamicEntityDao.merge(adminInstance);
            return helper.getRecord(adminProperties, adminInstance, null, null);
        } catch (Exception e) {
            throw new ServiceException("Unable to perform add for entity: " + Translation.class.getName(), e);
        }
    }

}
