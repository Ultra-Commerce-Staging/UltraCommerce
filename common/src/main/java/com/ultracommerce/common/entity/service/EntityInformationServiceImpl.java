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
import com.ultracommerce.common.extension.ExtensionResultHolder;
import com.ultracommerce.common.site.domain.Catalog;
import com.ultracommerce.common.site.domain.Site;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author bpolster
 *
 */
@Service("ucEntityInformationService")
public class EntityInformationServiceImpl implements EntityInformationService {

    @Resource(name = "ucEntityInformationServiceExtensionManager")
    protected EntityInformationServiceExtensionManager extensionManager;

    public EntityInformationDto buildEntityInformationForObject(Object o) {
        EntityInformationDto dto = createEntityInformationDto(o);
        extensionManager.updateEntityInformationDto(dto, o);
        return dto;
    }

    public EntityInformationDto buildBasicEntityInformationForObject(Object o) {
        EntityInformationDto dto = createEntityInformationDto(o);
        extensionManager.updateBasicEntityInformationDto(dto, o);
        return dto;
    }

    @Override
    public Long getBaseProfileIdForSite(Site site) {
        ExtensionResultHolder<Long> erh = new ExtensionResultHolder<Long>();
        extensionManager.getBaseProfileIdForSite(site, erh);
        return erh.getResult();
    }

    @Override
    public String getTypeForSite(Site site) {
        ExtensionResultHolder<String> erh = new ExtensionResultHolder<String>();
        extensionManager.getTypeForSite(site, erh);
        return erh.getResult();
    }

    @Override
    public Set<Long> getChildSiteIdsForProfile(Site profile) {
        ExtensionResultHolder<Set<Long>> erh = new ExtensionResultHolder<>();
        extensionManager.getProxy().getChildSiteIdsForProfile(profile, erh);
        return erh.getResult();
    }

    @Override
    public Site getParentSiteForProfile(Site profile) {
        ExtensionResultHolder<Site> erh = new ExtensionResultHolder<Site>();
        extensionManager.getParentSiteForProfile(profile, erh);
        return erh.getResult();
    }

    @Override
    public boolean getOkayToUseSiteDiscriminator(Object o) {
        ExtensionResultHolder<Boolean> erh = new ExtensionResultHolder<Boolean>();
        erh.setResult(Boolean.FALSE);
        extensionManager.getOkayToUseSiteDiscriminator(o, erh);
        return erh.getResult();
    }

    @Override
    public Long getDefaultCatalogIdForSite(Site site) {
        ExtensionResultHolder<Long> erh = new ExtensionResultHolder<Long>();
        extensionManager.getDefaultCatalogIdForSite(site, erh);
        return erh.getResult();
    }

    @Override
    public List<Catalog> findAllCatalogs() {
        ExtensionResultHolder<List<Catalog>> erh = new ExtensionResultHolder<List<Catalog>>();
        extensionManager.findAllCatalogs(erh);
        return erh.getResult();
    }

    /**
     * Factory method for instantiating the {@link EntityInformationDto}
     * @return
     */
    protected EntityInformationDto createEntityInformationDto(Object o) {
        return new EntityInformationDto();
    }
}
