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
package com.ultracommerce.common.entity.service;

import com.ultracommerce.common.entity.dto.EntityInformationDto;
import com.ultracommerce.common.extension.AbstractExtensionHandler;
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.extension.ExtensionResultStatusType;
import com.ultracommerce.common.site.domain.Catalog;
import com.ultracommerce.common.site.domain.Site;

import java.util.List;
import java.util.Set;

public class AbstractEntityInformationServiceExtensionHandler extends AbstractExtensionHandler
        implements EntityInformationServiceExtensionHandler {

    @Override
    public ExtensionResultStatusType updateEntityInformationDto(EntityInformationDto dto, Object entityInstance) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType updateBasicEntityInformationDto(EntityInformationDto dto, Object entityInstance) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType getBaseProfileIdForSite(Site site, ExtensionResultHolder<Long> erh) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType getTypeForSite(Site site, ExtensionResultHolder<String> erh) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType getChildSiteIdsForProfile(Site profile, ExtensionResultHolder<Set<Long>> erh) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType getParentSiteForProfile(Site profile, ExtensionResultHolder<Site> erh) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType getOkayToUseSiteDiscriminator(Object o, ExtensionResultHolder<Boolean> erh) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override public ExtensionResultStatusType getDefaultCatalogIdForSite(Site site, ExtensionResultHolder<Long> erh) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType findAllCatalogs(ExtensionResultHolder<List<Catalog>> erh) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
}
