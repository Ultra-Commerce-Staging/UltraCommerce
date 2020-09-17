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
import com.ultracommerce.common.site.domain.Catalog;
import com.ultracommerce.common.site.domain.Site;
import java.util.List;
import java.util.Set;

/**
 * Class that provides guidance to modules that need to react when running in a multi-tenant mode.   Especially, 
 * when an entity is associated with a Profile or Catalog.
 * 
 * @author bpolster
 *
 */
public interface EntityInformationService {

    /**
     * Given an object, populates {@link EntityInformationDto} with the associated site, profile, and catalog ids.
     * 
     * This method does nothing by default and instead relies on the {@link EntityInformationServiceExtensionManager} to
     * populate the correct values.     Designed for use with the commercial "workflow" and "multi-tenant" modules.
     * 
     * @param o
     * @return
     */
    EntityInformationDto buildEntityInformationForObject(Object o);

    EntityInformationDto buildBasicEntityInformationForObject(Object o);

    /**
     * Given a Site object, returns the Base Profile Id if one exists.    This method provides a hook
     * for Ultra MultiTenant functionality
     * 
     * @param site
     * @return
     */
    Long getBaseProfileIdForSite(Site site);

    String getTypeForSite(Site site);

    Set<Long> getChildSiteIdsForProfile(Site profile);

    /**
     * Given a profile {@link Site} object, returns the parent site, if one exists. This method provides a hook
     * for Ultra MultiTenant functionality.
     *
     * @param profile
     * @return
     */
    Site getParentSiteForProfile(Site profile);

    /**
     * Given an entity instance, returns true if the object has access to a Site Discriminator.
     * 
     * @param o
     * @return
     */
    boolean getOkayToUseSiteDiscriminator(Object o);

    /**
     * Returns all of the catalogs, using multi-tenant mode
     *
     * @return
     */
    List<Catalog> findAllCatalogs();

    /**
     * Given a Site object, returns the default catalog id. This method provides a hook for Ultra MultiTenant functionality
     *
     * @param site
     * @return the default Catalog id
     */
    Long getDefaultCatalogIdForSite(Site site);

}
