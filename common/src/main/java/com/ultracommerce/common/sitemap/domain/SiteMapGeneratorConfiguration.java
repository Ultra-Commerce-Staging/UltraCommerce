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

package com.ultracommerce.common.sitemap.domain;

import com.ultracommerce.common.sitemap.service.type.SiteMapChangeFreqType;
import com.ultracommerce.common.sitemap.service.type.SiteMapGeneratorType;
import com.ultracommerce.common.sitemap.service.type.SiteMapPriorityType;

import java.io.Serializable;

/**
 * Sample URL tag generated and controlled by this configuration.
 * 
 * <url>
 *   <loc>http://www.heatclinic.com/hot-sauces</loc>
 *   <lastmod>2009-11-07</lastmod>
 *   <changefreq>weekly</changefreq>
 *   <priority>0.5</priority>
 * </url>
 * 
 * @author bpolster
 */
public interface SiteMapGeneratorConfiguration extends Serializable {
    
    /**
     * Returns the SiteMapGeneratorConfiguration Id.
     * 
     * @return
     */
    public Long getId();

    /**
     * Sets the SiteMapGeneratorConfiguration Id.
     * 
     * @param id
     */
    public void setId(Long id);

    /**
     * Returns the "disabled" boolean.
     * 
     * @return
     */
    public Boolean isDisabled();

    /**
     * Sets the "disabled" boolean.
     * 
     * @param disabled
     */
    public void setDisabled(Boolean disabled);

    /**
     * Returns the list of SiteMapChangeFreqTypes.
     * 
     * @return
     */
    public SiteMapChangeFreqType getSiteMapChangeFreq();

    /**
     * Sets the list of SiteMapChangeFreqTypes.
     * 
     * @param siteMapChangeFreq
     */
    public void setSiteMapChangeFreq(SiteMapChangeFreqType siteMapChangeFreq);

    /**
     * Returns the SiteMapPriority.
     * 
     * @return
     */
    public SiteMapPriorityType getSiteMapPriority();

    /**
     * Sets the SiteMapPriority.  Must be a two digit value between 0.0 and 1.0.
     * 
     * @param siteMapPriority
     */
    public void setSiteMapPriority(SiteMapPriorityType siteMapPriority);

    /**
     * Returns the list of SiteMapGeneratorTypes.
     * 
     * @return
     */
    public SiteMapGeneratorType getSiteMapGeneratorType();
    
    /**
     * Sets the list of SiteMapGeneratorTypes.
     * 
     * @param siteMapGeneratorType
     */
    public void setSiteMapGeneratorType(SiteMapGeneratorType siteMapGeneratorType);

    /**
     * Returns the SiteMapConfiguration.
     * 
     * @return
     */
    public SiteMapConfiguration getSiteMapConfiguration();

    /**
     * Sets the SiteMapConfiguration.
     * 
     * @param siteMapConfiguration
     */
    public void setSiteMapConfiguration(SiteMapConfiguration siteMapConfiguration);

}
