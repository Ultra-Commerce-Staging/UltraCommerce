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
package com.ultracommerce.common.site.domain;

import java.io.Serializable;

/**
 * Defines the explicit join between a {@link Site} and {@link Catalog}
 * 
 * @author Phillip Verheyden (phillipuniverse)
 * @see {@link Site#getCatalogXrefs()}
 * @see {@link Catalog#getSiteXrefs()}
 */
public interface SiteCatalogXref extends Serializable {
    
    public Site getSite();
    
    public void setSite(Site site);
    
    public Catalog getCatalog();
    
    public void setCatalog(Catalog catalog);

    Long getId();

    void setId(Long id);

}
