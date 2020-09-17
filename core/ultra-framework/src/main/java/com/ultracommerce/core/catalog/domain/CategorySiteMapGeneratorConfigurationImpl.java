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
package com.ultracommerce.core.catalog.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.ultracommerce.common.presentation.AdminPresentation;
import com.ultracommerce.common.presentation.AdminPresentationClass;
import com.ultracommerce.common.presentation.AdminPresentationToOneLookup;
import com.ultracommerce.common.sitemap.domain.SiteMapGeneratorConfigurationImpl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * CategorySiteMapGenerator is controlled by this configuration.
 * 
 * @author Joshua Skorton (jskorton)
 */
@Entity
@Table(name = "UC_CAT_SITE_MAP_GEN_CFG")
@AdminPresentationClass(friendlyName = "CategorySiteMapGeneratorConfigurationImpl")
public class CategorySiteMapGeneratorConfigurationImpl extends SiteMapGeneratorConfigurationImpl implements CategorySiteMapGeneratorConfiguration {

    private static final long serialVersionUID = 1L;

    @ManyToOne(targetEntity = CategoryImpl.class)
    @JoinColumn(name = "ROOT_CATEGORY_ID", nullable = false)
    @AdminPresentation(friendlyName = "CategorySiteMapGeneratorConfigurationImpl_Root_Category")
    @AdminPresentationToOneLookup()
    protected Category rootCategory;

    @Column(name = "STARTING_DEPTH", nullable = false)
    @AdminPresentation(friendlyName = "CategorySiteMapGeneratorConfigurationImpl_Starting_Depth")
    protected int startingDepth = 1;

    @Column(name = "ENDING_DEPTH", nullable = false)
    @AdminPresentation(friendlyName = "CategorySiteMapGeneratorConfigurationImpl_Ending_Depth")
    protected int endingDepth = 1;

    @Override
    public Category getRootCategory() {
        return rootCategory;
    }

    @Override
    public void setRootCategory(Category rootCategory) {
        this.rootCategory = rootCategory;
    }

    @Override
    public int getStartingDepth() {
        return startingDepth;
    }

    @Override
    public void setStartingDepth(int startingDepth) {
        this.startingDepth = startingDepth;
    }

    @Override
    public int getEndingDepth() {
        return endingDepth;
    }

    @Override
    public void setEndingDepth(int endingDepth) {
        this.endingDepth = endingDepth;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        CategorySiteMapGeneratorConfigurationImpl rhs = (CategorySiteMapGeneratorConfigurationImpl) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.rootCategory, rhs.rootCategory)
                .append(this.startingDepth, rhs.startingDepth)
                .append(this.endingDepth, rhs.endingDepth)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(rootCategory)
                .append(startingDepth)
                .append(endingDepth)
                .toHashCode();
    }
}
