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
package com.ultracommerce.common.sitemap.domain.weave;

import com.ultracommerce.common.config.domain.AbstractModuleConfigurationAdminPresentation;
import com.ultracommerce.common.extensibility.jpa.SiteDiscriminatable;
import com.ultracommerce.common.extensibility.jpa.SiteDiscriminatableType;
import com.ultracommerce.common.presentation.AdminPresentationCollection;
import com.ultracommerce.common.sitemap.domain.SiteMapGeneratorConfiguration;
import com.ultracommerce.common.sitemap.domain.SiteMapGeneratorConfigurationImpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

/**
 * This class is meant as a template to provide overriding of the annotations on fields in 
 * <code>com.ultracommerce.common.sitemap.domain.SiteMapConfigurationImpl</code>.  This provides a
 * stop gap measure to allow someone to weave in the appropriate annotations in 4.0.x without forcing a schema change on those 
 * who prefer not to use it.  This should likely be removed in 4.1 for fixed annotations on the entity itself.
 * 
 * @author Jeff Fischer
 *
 */
@Deprecated
public abstract class OptionalEnterpriseSiteMapConfigurationSiteMapGeneratorConfigurationTemplate {

    @OneToMany(mappedBy = "siteMapConfiguration", targetEntity = SiteMapGeneratorConfigurationImpl.class, cascade = { CascadeType.ALL }, orphanRemoval = true)
    @AdminPresentationCollection(friendlyName = "SiteMapConfigurationImpl_Generator_Configurations",
            tab = AbstractModuleConfigurationAdminPresentation.TabName.General)
    @SiteDiscriminatable(type = SiteDiscriminatableType.SITE)
    protected List<SiteMapGeneratorConfiguration> siteMapGeneratorConfigurations = new ArrayList<SiteMapGeneratorConfiguration>();

}
